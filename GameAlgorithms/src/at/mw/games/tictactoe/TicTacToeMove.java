package at.mw.games.tictactoe;

import at.mw.games.Move;

public class TicTacToeMove implements Move {
	public final int bitMask;
	public final TicTacToePlayer player;

	public TicTacToeMove(int bitMask, TicTacToePlayer player) {
		this.bitMask = bitMask;
		this.player = player;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(3 * 3 + 3 * 2);

		char c = player == TicTacToePlayer.Cross ? 'X' : 'O';
		int v = 0x01;
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				if ((bitMask & v) != 0) {
					sb.append(c);
				} else {
					sb.append('#');
				}

				v <<= 1;
			}
			sb.append("\r\n");
		}

		return sb.toString();
	}
}
