package at.mw.games;

import java.util.Iterator;

public interface Board<P extends Player<P>, M extends Move> {

	void reset();

	void make(M move);

	void unmake(M move);

	Iterator<M> generateMoves(P player);

	int evaluate(P player);
}
