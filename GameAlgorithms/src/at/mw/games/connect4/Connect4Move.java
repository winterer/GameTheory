package at.mw.games.connect4;

import at.mw.games.Move;

public class Connect4Move implements Move {
	public final Connect4Player player;
	public final int position;

	public Connect4Move(int position, Connect4Player player) {
		this.position = position;
		this.player = player;
	}

	@Override
	public String toString() {
		return Integer.toString(position + 1);
	}
}
