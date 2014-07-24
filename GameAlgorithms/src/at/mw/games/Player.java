package at.mw.games;

public interface Player<P extends Player<P>> {
	P opponent();
}
