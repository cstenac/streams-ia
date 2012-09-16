import java.util.Arrays;


/**
 * Segregates the board into 1 main series and 1 additional series for elimination.
 * At the moment, no attempt is made to sort the additional series
 */
public class Main1AddBoard {
	public Main1AddBoard(int[] board, int mainSize) {
		this.board = board;
		addFirst = mainSize;
	}
	
	public int[] board;
	public int addFirst;
	
	public Main1AddBoard(Main1AddBoard orig) {
		this.board = Arrays.copyOf(orig.board, 20);
		this.addFirst = orig.addFirst;
	}
}
