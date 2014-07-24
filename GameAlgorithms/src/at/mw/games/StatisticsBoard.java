package at.mw.games;

import java.util.Iterator;

public class StatisticsBoard<P extends Player<P>, M extends Move> implements
		Board<P, M> {

	private Board<P, M> board;
	private long moves;
	private long evaluations;

	public StatisticsBoard(Board<P, M> board) {
		this.board = board;
	}

	public void resetStatistics() {
		moves = 0;
		evaluations = 0;
	}

	@Override
	public void reset() {
		resetStatistics();
	}

	@Override
	public void make(M move) {
		moves++;
		board.make(move);
	}

	@Override
	public void unmake(M move) {
		board.unmake(move);
	}

	@Override
	public Iterator<M> generateMoves(P player) {
		return board.generateMoves(player);
	}

	@Override
	public int evaluate(P player) {
		evaluations++;
		return board.evaluate(player);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(board.toString());
		sb.append("board statistics:\r\n");
		sb.append("  moves:       " + moves + "\r\n");
		sb.append("  evaluations: " + evaluations + "\r\n");

		return sb.toString();
	}
}
