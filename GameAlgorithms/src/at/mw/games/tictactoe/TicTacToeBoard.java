package at.mw.games.tictactoe;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import at.mw.games.Board;

public class TicTacToeBoard implements Board<TicTacToePlayer, TicTacToeMove> {
	static final int Row1 = 0x001 + 0x002 + 0x004;
	static final int Row2 = 0x008 + 0x010 + 0x020;
	static final int Row3 = 0x040 + 0x080 + 0x100;
	static final int Col1 = 0x001 + 0x008 + 0x040;
	static final int Col2 = 0x002 + 0x010 + 0x080;
	static final int Col3 = 0x004 + 0x020 + 0x100;
	static final int Diag1 = 0x001 + 0x010 + 0x100;
	static final int Diag2 = 0x004 + 0x010 + 0x040;
	static final int Max = 0x100;

	private final AtomicInteger cross = new AtomicInteger(0x000);
	private final AtomicInteger circle = new AtomicInteger(0x000);
	private int moves = 0;

	public void make(int pos, TicTacToePlayer player) {
		make(new TicTacToeMove(0x001 << pos, player));
	}

	public void unmake(int pos, TicTacToePlayer player) {
		unmake(new TicTacToeMove(0x001 << pos, player));
	}

	public void reset() {
		moves = 0;
		cross.set(0);
		circle.set(0);
	}

	@Override
	public void make(TicTacToeMove move) {
		// set pos bit
		AtomicInteger field = get(move.player);
		field.set(field.get() | move.bitMask);
		moves++;
	}

	@Override
	public void unmake(TicTacToeMove move) {
		// unset pos bit
		AtomicInteger field = get(move.player);
		field.set(field.get() & ~move.bitMask);
		moves--;
	}

	@Override
	public Iterator<TicTacToeMove> generateMoves(TicTacToePlayer player) {
		if (hasWon(get(player.opponent()).get())) {
			return Collections.emptyIterator();
		}

		return new MoveIterator(cross.get() | circle.get(), player);
	}

	@Override
	public int evaluate(TicTacToePlayer player) {
		// add time penalty
		// the later the winning move is made, the less the score
		// this encourages the engine to win early / lose late
		final int maxScore = 10000 - moves;
		return hasWon(get(player).get()) ? maxScore : (hasWon(get(
				player.opponent()).get()) ? -maxScore : 0);
	}

	private boolean hasWon(int field) {
		return ((field & Row1) == Row1) //
				|| ((field & Row2) == Row2) //
				|| ((field & Row3) == Row3) //
				|| ((field & Col1) == Col1) //
				|| ((field & Col2) == Col2) //
				|| ((field & Col3) == Col3) //
				|| ((field & Diag1) == Diag1) //
				|| ((field & Diag2) == Diag2);
	}

	private AtomicInteger get(TicTacToePlayer player) {
		switch (player) {
		case Circle:
			return circle;
		case Cross:
			return cross;
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(3 * 3 + 3 * 2);

		final int crossField = cross.get();
		final int circleField = circle.get();
		int v = 0x01;
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				if ((crossField & v) != 0) {
					sb.append('X');
				} else if ((circleField & v) != 0) {
					sb.append('O');
				} else
					sb.append('_');

				v <<= 1;
			}
			sb.append("\r\n");
		}

		return sb.toString();
	}
}

class MoveIterator implements Iterator<TicTacToeMove> {
	private final int field;
	private final TicTacToePlayer player;
	private int v;

	public MoveIterator(int field, TicTacToePlayer player) {
		this.field = field;
		this.player = player;

		v = 0x001;
		advanceToNextClearBit();
	}

	private void advanceToNextClearBit() {
		while (v <= TicTacToeBoard.Max && ((field & v) != 0)) {
			v <<= 1;
		}
	}

	@Override
	public boolean hasNext() {
		return v <= TicTacToeBoard.Max;
	}

	@Override
	public TicTacToeMove next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		int p = v;
		v <<= 1;
		advanceToNextClearBit();
		return new TicTacToeMove(p, player);
	}
}