
public class IAEngine {
	public int choosePosition(GameState gs, int number, int currentRound) {
		int bestPos = -1;
		double bestScore = 0.0;
		System.out.println("**** IA START FOR ROUND " + currentRound + ": " + gs.boardToString());
		for (int i = 0; i < 20; i++) {
			if (gs.board[i] == 0) {
				GameState ngs = new GameState(gs);
				ngs.board[i] = number;
				System.out.println("Trying to put " + number + " at " + i);
				double val = new MonteCarloExpectationComputer().expect(ngs, 20 - currentRound -1);
				System.out.println("Tried to put " + number + " at " + i + " exp is " + val);
				if (val > bestScore) {
					bestScore = val;
					bestPos = i;
				}
			}
		}
		// oooh :(
		if (bestPos == -1) {
			bestPos = 0;
		}
		return bestPos;
	}
}
