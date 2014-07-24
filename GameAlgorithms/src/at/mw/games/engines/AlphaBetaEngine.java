package at.mw.games.engines;

import java.util.Iterator;

import at.mw.games.Board;
import at.mw.games.Engine;
import at.mw.games.Move;
import at.mw.games.Player;

/**
 * {@link Engine} that uses a negamax algorithm in combination with alpha-beta
 * pruning.
 * 
 * @author mario
 *
 * @param <P>
 * @param <M>
 */
public class AlphaBetaEngine<P extends Player<P>, M extends Move> implements
		Engine<P, M> {
	private final Board<P, M> board;

	private M[] bestMoves;

	public AlphaBetaEngine(Board<P, M> board) {
		this.board = board;
	}

	@SuppressWarnings("unchecked")
	public M computeBestMove(P player, int depth) {
		bestMoves = (M[]) new Move[depth + 1];

		computeBestMove(player, depth, Integer.MIN_VALUE + 1,
				Integer.MAX_VALUE - 1);
		return depth > 0 ? bestMoves[depth] : null;
	}

	protected int computeBestMove(P player, int depth, int alpha, int beta) {
		if (depth == 0) {
			// maximum level of depth reached
			// TODO: dynamic depth?
			return evaluate(player);
		}

		int bestScore = alpha;
		M bestMove = null;

		Iterator<M> moves = generateMoves(player);
		if (!moves.hasNext()) {
			// no more moves
			return evaluate(player);
		}

		while (moves.hasNext()) {
			M move = moves.next();
			make(move);
			int score = -computeBestMove(player.opponent(), depth - 1, -beta,
					-bestScore);
			// int value = -compute(player.opponent(), depth - 1, -beta,
			// -maxValue);
			unmake(move);

			if (score > bestScore) {
				bestScore = score;
				bestMove = move;
				if (bestScore >= beta) {
					break;
				}
			}
		}

		bestMoves[depth] = bestMove;

		return bestScore;
	}

	protected void make(M move) {
		board.make(move);
	}

	protected void unmake(M move) {
		board.unmake(move);
	}

	protected Iterator<M> generateMoves(P player) {
		return board.generateMoves(player);
	}

	protected int evaluate(P player) {
		return board.evaluate(player);
	}
}
