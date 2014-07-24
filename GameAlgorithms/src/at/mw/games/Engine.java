package at.mw.games;

public interface Engine<P extends Player<P>, M extends Move> {
	public M computeBestMove(P player, int depth);
}
