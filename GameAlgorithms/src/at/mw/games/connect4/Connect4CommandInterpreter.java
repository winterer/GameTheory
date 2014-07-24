package at.mw.games.connect4;

import java.io.PrintStream;
import java.util.Scanner;

import at.mw.games.Board;
import at.mw.games.Engine;
import at.mw.games.StatisticsBoard;
import at.mw.games.engines.PrincipalVariationEngine;
import at.mw.games.util.CommandInterpreter;

public class Connect4CommandInterpreter extends
		CommandInterpreter<Connect4Player, Connect4Move> {

	public Connect4CommandInterpreter(Scanner scanner, PrintStream stream) {
		super(scanner, stream);
	}

	@Override
	protected Connect4Move computeBestMove(
			Engine<Connect4Player, Connect4Move> engine, Connect4Player player) {
		((StatisticsBoard<?, ?>) board).resetStatistics();
		return engine.computeBestMove(player, 11);
	}

	@Override
	protected Connect4Move parseMove(String cmd) {
		int pos = Integer.parseInt(cmd);
		if ((pos >= 1) && (pos <= Connect4Board.WIDTH)) {
			return new Connect4Move(pos - 1, currentPlayer);
		}

		return null;
	}

	@Override
	protected Connect4Player initialPlayer() {
		return Connect4Player.Red;
	}

	@Override
	protected Engine<Connect4Player, Connect4Move> createEngine(
			Board<Connect4Player, Connect4Move> board) {
		// return new AlphaBetaEngine<>(board);
		return new PrincipalVariationEngine<>(board);
	}

	@Override
	protected Board<Connect4Player, Connect4Move> createBoard() {
		Connect4Board board = new Connect4Board();
		return new StatisticsBoard<>(board);
	}

}
