package at.mw.games.tictactoe;

import at.mw.games.Player;

public enum TicTacToePlayer implements Player<TicTacToePlayer> {
	Circle() {
		@Override
		public TicTacToePlayer opponent() {
			return Cross;
		}
	},
	Cross() {
		@Override
		public TicTacToePlayer opponent() {
			return Circle;
		}
	};
}
