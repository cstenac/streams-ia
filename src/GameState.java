import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class GameState {
	public GameState() {
		board = new int[20];
		stash = new int[40];
		for (int i = 0; i < 40; i++) {
			stash[i] = i + 1;
		}
	}
	
	public GameState(String state) {
		board = new int[20];
		stash = new int[40];
		for (int i = 0; i < 40; i++) {
			stash[i] = i + 1;
		}
		String[] c = state.split("-");
		for (int i = 0; i < c.length; i++) {
			int number = Integer.parseInt(c[i]);
			if (number == 0) continue;
			board[i] = number;
			if (number >= 20) {
				stash[number + 10 - 1] = 0;
			} else if (number > 10 && stash[number  - 1] == 0) {
				stash[number + 10 - 1 ] = 0;
			} else {
				stash[number - 1] = 0;
			}
		}
	}
	public GameState(GameState other) {
		board = Arrays.copyOf(other.board, 20);
		stash = Arrays.copyOf(other.stash, 40);
	}
	
	private static Random r = new Random();

	public static int pull(int[] stash) {
		while (true) {
			int idx = r.nextInt(40);
			if (stash[idx] != 0) {
				int pulled = stash[idx];
				if (pulled >= 20) pulled -= 10;
				stash[idx] = 0;
				return pulled;
			}
		}
	}
	
	public void setRawNumberAtPos(int number, int pos) {
		stash[number - 1] = 0;
		board[pos] = number >= 20 ? number - 10 : number;
	}

	public int pullNextNumber() {
		return pull(stash);
	}

	public String boardToString() {
		return boardToString(board);
	}

	public static String boardToString(int[]board) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 20; i++) {
			if (i > 0) sb.append('-');
			if (board[i] < 10) sb.append('0');
			sb.append(board[i]);
		}
		return sb.toString();
	}

	public int[] board;

	public int[] stash;
}
