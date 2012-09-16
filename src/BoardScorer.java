import java.util.ArrayList;
import java.util.List;


/** 
 * Computes the score of a board.
 * Warning: this class is not thread safe.
 */
public class BoardScorer {
	
	static int[] sizeToScore = new int[]{
			0, 
			0,1,3,5,7,9,11,15,20,25,30,35,40,50,60,70,85,100,150,300
	};
	
	List<Integer> doneSizes = new ArrayList<Integer>();
	public int score(int[] board) {
		doneSizes.clear();
//		System.out.println("SCORING " + GameState.boardToString(board));
		
		int curSeriesSize = 0;
		int curInSeries = -1;
		
		for (int i = 0; i < 20; i++) {
			int d = board[i];
			
			if (d >= curInSeries) {
				curSeriesSize ++;
				curInSeries = d;
			} else {
				doneSizes.add(curSeriesSize);
				curSeriesSize = 0;
				curInSeries = -1;
			}
		}
		int score = 0;
		for (int i = 0; i < doneSizes.size(); i++) {
			score += sizeToScore[doneSizes.get(i)];
//			System.out.println("Done " + doneSizes.get(i));
		}
		return score;
	}
}
