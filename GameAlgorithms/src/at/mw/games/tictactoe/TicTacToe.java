package at.mw.games.tictactoe;

import java.util.Scanner;

import at.mw.games.util.CommandInterpreter;

public class TicTacToe {
	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)) {
			CommandInterpreter<?, ?> interpreter = new TicTacToeCommandInterpreter(
					scanner, System.out);
			interpreter.run();
		}
		// board.make(0, TicTacToePlayer.Cross);
		// board.make(1, TicTacToePlayer.Circle);
		// board.make(3, TicTacToePlayer.Cross);
		// board.make(2, TicTacToePlayer.Circle);
		//
		// System.out.println(board);
		//
		// TicTacToePlayer player = TicTacToePlayer.Circle;
		// TicTacToeMove m = engine.compute(player, 9);
		//
		// while (m != null) {
		// board.make(m);
		// System.out.println(board);
		// player = player.opponent();
		// m = engine.compute(player, 9);
		// }
	}
}
