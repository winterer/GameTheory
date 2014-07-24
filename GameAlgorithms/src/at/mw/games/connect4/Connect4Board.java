package at.mw.games.connect4;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import at.mw.games.Board;

/**
 * @author mario_000
 * @see http 
 *      ://stackoverflow.com/questions/7044670/how-to-determine-game-end-in-tic
 *      -tac-toe/7046415#7046415
 */
public class Connect4Board implements Board<Connect4Player, Connect4Move> {
	static final int WIDTH = 7;
	static final int HEIGHT = 6;
	// bitmask corresponds to board as follows in 7x6 case:
	// . . . . . . . TOP
	// 5 12 19 26 33 40 47
	// 4 11 18 25 32 39 46
	// 3 10 17 24 31 38 45
	// 2 9 16 23 30 37 44
	// 1 8 15 22 29 36 43
	// 0 7 14 21 28 35 42 BOTTOM
	static final int H1 = HEIGHT + 1;
	static final int H2 = HEIGHT + 2;
	static final int SIZE = HEIGHT * WIDTH;
	static final int SIZE1 = H1 * WIDTH;
	static final long ALL1 = (1L << SIZE1) - 1L; // assumes SIZE1 < 63
	static final int COL1 = (1 << H1) - 1;
	static final long BOTTOM = ALL1 / COL1; // has bits i*H1 set
	static final long TOP = BOTTOM << HEIGHT;

	long color[]; // black and white bitboard
	private byte height[]; // holds bit index of lowest free square
	private int moves = 0;

	public Connect4Board() {
		color = new long[2];
		height = new byte[WIDTH];
		reset();
	}

	public void reset() {
		moves = 0;
		color[0] = color[1] = 0L;
		for (int i = 0; i < WIDTH; i++) {
			height[i] = (byte) (H1 * i);
		}
	}

	// return whether columns col has room
	final boolean isPlayable(Connect4Player player, int col) {
		return isLegal(color[player.ordinal()] | (1L << height[col]));
	}

	// return whether newboard lacks overflowing column
	private boolean isLegal(long newboard) {
		return (newboard & TOP) == 0;
	}

	// // return whether newboard is legal and includes a win
	// private boolean isLegalHasWon(long newboard) {
	// return isLegal(newboard) && hasWon(newboard);
	// }

	// return whether newboard includes a win
	private boolean hasWon(long newboard) {
		long y = newboard & (newboard >> HEIGHT);
		if ((y & (y >> 2 * HEIGHT)) != 0) // check diagonal \
			return true;
		y = newboard & (newboard >> H1);
		if ((y & (y >> 2 * H1)) != 0) // check horizontal -
			return true;
		y = newboard & (newboard >> H2); // check diagonal /
		if ((y & (y >> 2 * H2)) != 0)
			return true;
		y = newboard & (newboard >> 1); // check vertical |
		return (y & (y >> 2)) != 0;
	}

	public void unmake(Connect4Move move) {
		// color[nplies & 1] ^= 1L << --height[n];
		color[move.player.ordinal()] ^= 1L << --height[move.position];
		moves--;
	}

	public void make(Connect4Move move) {
		color[move.player.ordinal()] ^= 1L << height[move.position]++;
		moves++;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();

		for (int w = 0; w < WIDTH; w++)
			buf.append(" " + (w + 1));
		buf.append("\n");
		for (int h = HEIGHT - 1; h >= 0; h--) {
			for (int w = h; w < SIZE1; w += H1) {
				long mask = 1L << w;
				buf.append((color[0] & mask) != 0 ? " X"
						: (color[1] & mask) != 0 ? " 0" : " .");
			}
			buf.append("\n");
		}
		if (hasWon(color[0]))
			buf.append("X won\n");
		if (hasWon(color[1]))
			buf.append("O won\n");
		return buf.toString();
	}

	@Override
	public int evaluate(Connect4Player player) {
		final int maxScore = 10000 - moves;
		if (hasWon(color[player.ordinal()])) {
			return maxScore;
		} else if (hasWon(color[player.opponent().ordinal()])) {
			return -maxScore;
		}

		return 0;
	}

	@Override
	public Iterator<Connect4Move> generateMoves(Connect4Player player) {
		if (hasWon(color[player.opponent().ordinal()])) {
			return Collections.emptyIterator();
		}

		int[] moves = new int[WIDTH];
		int top = 0;
		for (int c = 0; c < WIDTH; c++) {
			if (isPlayable(player, c)) {
				moves[top++] = c;
			}
		}
		return new MoveIterator(moves, top, player);
	}
}

class MoveIterator implements Iterator<Connect4Move> {
	private final int[] moves;
	private final int length;
	private final Connect4Player player;
	private int top;

	public MoveIterator(int[] moves, int length, Connect4Player player) {
		this.moves = moves;
		this.length = length;
		this.player = player;

		top = 0;
	}

	@Override
	public boolean hasNext() {
		return top < length;
	}

	@Override
	public Connect4Move next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		return new Connect4Move(moves[top++], player);
	}
}