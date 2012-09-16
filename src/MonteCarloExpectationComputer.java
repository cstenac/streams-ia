import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * An expectation computer takes a board with some positioned numbers, 
 * and computes the expected score from this state of the board
 *
 * The sampling expectation computer makes N random tries: it chooses the
 * numbers that will be pulled, and will try all numbers at all possible positions,
 * and compute the best expectation for this try. IT then returns the average.
 * 
 * This class is not thread safe
 */
public class MonteCarloExpectationComputer {
	BoardScorer bs = new BoardScorer();
	
	List<List<Integer>> listPool = new ArrayList<List<Integer>>();
	
	private List<Integer> acquireList() {
		if (listPool.size() == 0) {
			return new ArrayList<Integer>();
		} else {
			return listPool.remove(listPool.size() - 1);
		}
	}
	private void releaseList(List<Integer> l) {
		l.clear();
		listPool.add(l);
	}
	
	
	private static String indent(int pos) {
		String s = "   ";
		for (int i = 0; i < pos; i++) {
			s += " ";
		}
		return s;
	}

	private double expectRandomTryRec(Main1AddBoard board, List<Integer> numbers, int idxToUse, int round) {
		if (idxToUse < 0) {
			System.out.println(indent(idxToUse) + idxToUse + "/" + numbers.size() + "  ERTR " + GameState.boardToString(board.board));
		}
		if (numbers.size() == idxToUse) {
			double score = (double)bs.score(board.board);
			if (idxToUse < 20) {
//				System.out.println(indent(idxToUse) + " WIN "  + GameState.boardToString(board.board) + " -> " + score);
			}
			return score;
			// We have finished ! Score this board
		}

		int number = numbers.get(idxToUse); 
		List<Integer> list = acquireList(); 
		getPossibleWinningPositions(board, number,list);

		if (idxToUse < 0) {
			System.out.println(indent(idxToUse) +  " CAN PUT " + number + " in " + list.size()  + " places");
		}
		if (list.size() == 0) {
			/* Simplification: if we can't find a winning move early on, consider that we have lost ... 
			 */
			if (round  < 14	) {
//				System.out.println(indent(idxToUse) + "  LOST, can't place " + number);
				releaseList(list);
				return 0.0;
			} else {
				getAllPossiblePositions(board, number, list);
			}
			/* REMOVED
			// Simplification: if we can't find a winning move, consider that we have lost ...
			if (idxToUse < 15) {
				System.out.println(indent(idxToUse) + "  LOST, can't place " + number);
			}
			releaseList(list);
			return 0.0;
			*/
		}
		
		/* Now, try placing this number everywhere ! */
		double max = 0;
		for (int i = 0; i < list.size(); i++) {
			int pos = list.get(i);
			/* Some pruning to avoid too much explosion */
			if (numbers.size() - idxToUse > 10) {
				if (number > 10 && pos < 3) continue;
				//if (number < 20 && pos >= 12) continue;
			}
			
			Main1AddBoard copy = new Main1AddBoard(board);
			copy.board[list.get(i)] = number;
			double next =  expectRandomTryRec(copy, numbers, idxToUse + 1, round + 1);
			max = Math.max(next, max);
		}
		//int size = list.size();
		releaseList(list);
		return max;//sum / size;
	}

	private double expectRandomTry(GameState gs, List<Integer> numbers, int round) {
		Main1AddBoard b = new Main1AddBoard(gs.board, 12); // Risk taken by the program: 12 + 8 --> with 8, we should definitely try to sort the additional series
		return expectRandomTryRec(b, numbers, 0, round);
	}

	Random r = new Random();

	List<Integer> pull(int[] stash, int nbToPull) {
	
//		System.out.println("PULLING? STASH IS " + Arrays.toString(stash));
		List<Integer> ret = new ArrayList<Integer>();
		for (int i = 0; i < nbToPull; i++) {
			while (true) {
				int idx = r.nextInt(40);
				if (stash[idx] != 0) {
					int pulled = stash[idx];
					if (pulled >= 20) pulled -= 10;
					ret.add(pulled);
					stash[idx] = 0;
					break;
				}
			}
		}
		return ret;
	}

	public double expect(GameState gs, int nbToPull) {
		int round = 20 - nbToPull;
		
		double sum = 0;
		System.out.println(" Trying to score " + GameState.boardToString(gs.board));
		for (int i = 0; i < 300; i++) {
			int[] thisStash = Arrays.copyOf(gs.stash, 40);
			List<Integer> pulled = pull(thisStash, nbToPull); // TODO
//			System.out.println("  BEGINNING OF TEST " + i  + " -> " + pulled);
			double exp = expectRandomTry(gs, pulled, round);
//			System.out.println("  END OF TEST " + i + " -> " + exp);
			sum += exp;
		}
		return sum / 1000;
	}
	
	public List<Integer> getAllPossiblePositions(Main1AddBoard board, int number, List<Integer> possPosBuffer) {
		for (int i = 0; i < 20; i++) {
			if (board.board[i] == 0) {
				possPosBuffer.add(i);
			}
		}
		return possPosBuffer;
	}

	public List<Integer> getPossibleWinningPositions(Main1AddBoard board, int number, List<Integer> possPosBuffer) {
		/* Find the first position above number */
		int lastPosBelowOrEqualNumber = 0;
		int firstPosAboveNumber = -1;
		for (int i = 0; i <= board.addFirst; i++) {
			if (board.board[i] != 0 && board.board[i] <= number) {
				lastPosBelowOrEqualNumber = i;
			}
			if (board.board[i] > number) {
				firstPosAboveNumber = i;
				break;
			}
		}
//		System.out.println("Positioning " + number + " between " +lastPosBelowOrEqualNumber + " and " + firstPosAboveNumber + " or after " + 
//				board.addFirst);
	/* ALl empty positions strictly below are ok */
		for (int i = lastPosBelowOrEqualNumber; i < firstPosAboveNumber; i++ ){
			if (board.board[i] == 0) {
				possPosBuffer.add(i);
			}
		}

		for (int i = board.addFirst; i < 20; i++) {
			// Don't sort additional yet ...
			// So, as we don't sort additional, we only need to put *one* 
			// element from the additional list, as they are all equivalent ... !
			if (board.board[i] == 0) {
				possPosBuffer.add(i);
				break; // Woo
			}
		}
		return possPosBuffer;
	}

	/**
	 * Get the positions in the main series where this number can be placed.
	 * If the returned list is empty, then it's not possible to put this number
	 * in the main series
	 */
//	public List<Integer> getPossiblePositionsInMainSeries(Main1AddBoard board, int number) {
//		List<Integer> possPosBuffer = new ArrayList<Integer>();
//
//		/* Find the first position above number */
//		int firstPosAboveNumber = -1;
//		for (int i = 0; i < board.addFirst; i++) {
//			if (board.board[i] > number) {
//				firstPosAboveNumber = i;
//				break;
//			}
//		}
//		/* ALl empty positions strictly below are ok */
//		for (int i = 0; i < firstPosAboveNumber; i++ ){
//			if (board.board[i] == 0) {
//				possPosBuffer.add(i);
//			}
//		}
//		return possPosBuffer;
//	}
	/**
	 * Get the positions in the main series where this number can be placed.
	 * If the returned list is empty, then the additional series are full. 
	 */
//	public List<Integer> getPossiblePositionsInAdditionalSeries(Main1AddBoard board, int number) {
//		List<Integer> possPosBuffer = new ArrayList<Integer>();
//
//		for (int i = board.addFirst; i < 20; i++) {
//			// Don't sort additional yet ...
//			if (board.board[i] == 0) {
//				possPosBuffer.add(i);
//			}
//		}
//		return possPosBuffer;
//	}
}