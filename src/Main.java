
public class Main {
	public static void main(String[] args) {
		/*{
		String b= "00-02-04-00-00-12-13-15-16-00-20-23-27-30-00-00-17-15-24-00";
		GameState gs = new GameState(b);
		int alreadyPlaced = 13;
		IAEngine iae = new IAEngine();
		
		int number = 7;
		int pos = iae.choosePosition(gs, number, 13);
		System.out.println("  CHOSE TO PUT " + number + " in " + pos);
		if (1 == 1) System.exit(0);
		}*/
		
		int alreadyPlaced = 0;
		
		/*
		alreadyPlaced = 15;
		String b = "01-02-04-05-00-00-13-15-15-16-20-00-26-30-00-10-13-00-19-11";
		GameState gs = new GameState(b);
		*/

		alreadyPlaced = 5;
		GameState gs = new GameState();
		gs.setRawNumberAtPos(13, 6);
		gs.setRawNumberAtPos(40, 13);
		gs.setRawNumberAtPos(2, 1);
		gs.setRawNumberAtPos(30, 10);
		gs.setRawNumberAtPos(4, 2);
		
		
		IAEngine iae = new IAEngine();
		
		for (int i = 0; i < (20 - alreadyPlaced); i++) {
			int number = gs.pullNextNumber();
			
			int pos = iae.choosePosition(gs, number, i + alreadyPlaced);
			System.out.println("  CHOSE TO PUT " + number + " in " + pos);
			gs.board[pos] = number;
			System.out.println("   NOW BOARD " + gs.boardToString());
		}
		System.out.println("FINAL BOARD " + gs.boardToString());
		System.out.println("FINAL SCORE " + new BoardScorer().score(gs.board));

		
		

	}
}
