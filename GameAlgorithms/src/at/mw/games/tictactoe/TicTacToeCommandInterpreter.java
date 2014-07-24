package at.mw.games.tictactoe;

import java.io.PrintStream;
import java.util.Scanner;

import at.mw.games.Board;
import at.mw.games.Engine;
import at.mw.games.StatisticsBoard;
import at.mw.games.engines.PrincipalVariationEngine;
import at.mw.games.util.CommandInterpreter;

public class TicTacToeCommandInterpreter extends
		CommandInterpreter<TicTacToePlayer, TicTacToeMove> {

	public TicTacToeCommandInterpreter(Scanner scanner, PrintStream stream) {
		super(scanner, stream);
	}

	@Override
	protected TicTacToeMove computeBestMove(
			Engine<TicTacToePlayer, TicTacToeMove> engine,
			TicTacToePlayer player) {
		((StatisticsBoard<?, ?>) board).resetStatistics();
		return engine.computeBestMove(player, 9);
	}

	@Override
	protected Board<TicTacToePlayer, TicTacToeMove> createBoard() {
		TicTacToeBoard board = new TicTacToeBoard();
		return new StatisticsBoard<>(board);
	}

	@Override
	protected Engine<TicTacToePlayer, TicTacToeMove> createEngine(
			Board<TicTacToePlayer, TicTacToeMove> board) {
		// return new AlphaBetaEngine<>(board);
		return new PrincipalVariationEngine<>(board);
	}

	@Override
	protected TicTacToePlayer initialPlayer() {
		return TicTacToePlayer.Cross;
	}

	@Override
	protected TicTacToeMove parseMove(String cmd) {
		int pos = Integer.parseInt(cmd);
		if ((pos >= 1) && (pos <= 9)) {
			return new TicTacToeMove(0x001 << (pos - 1), currentPlayer);
		}

		return null;
	}
}
