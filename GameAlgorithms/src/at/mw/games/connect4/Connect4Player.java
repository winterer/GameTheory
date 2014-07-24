package at.mw.games.connect4;

import at.mw.games.Player;

public enum Connect4Player implements Player<Connect4Player> {
	Red() {
		@Override
		public Connect4Player opponent() {
			return Yellow;
		}
	},
	Yellow() {
		@Override
		public Connect4Player opponent() {
			return Red;
		}
	}
}
