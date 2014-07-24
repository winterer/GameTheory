package at.mw.games.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Scanner;

import at.mw.games.Board;
import at.mw.games.Engine;
import at.mw.games.Move;
import at.mw.games.Player;

public abstract class CommandInterpreter<P extends Player<P>, M extends Move> {
	private final Scanner scanner;
	private final PrintWriter writer;
	protected Board<P, M> board;
	protected Engine<P, M> engine;
	protected P currentPlayer;
	protected final Deque<M> moves = new ArrayDeque<M>();

	public CommandInterpreter(Scanner scanner, PrintWriter writer) {
		this.scanner = scanner;
		this.writer = writer;
	}

	public CommandInterpreter(Scanner scanner, PrintStream stream) {
		this.scanner = scanner;
		this.writer = new PrintWriter(stream);
	}

	public void run() {
		init();

		String cmd = awaitInput(scanner);
		while (!"q".equalsIgnoreCase(cmd)) {
			interprete(cmd);
			cmd = awaitInput(scanner);
		}
	}

	protected void interprete(String cmd) {
		if ("n".equalsIgnoreCase(cmd)) {
			newGame();
		} else if ("m".equalsIgnoreCase(cmd)) {
			engineMove();
		} else if ("p".equalsIgnoreCase(cmd)) {
			printBoard();
		} else if ("u".equalsIgnoreCase(cmd)) {
			undoMove();
		} else if ("e".equalsIgnoreCase(cmd)) {
			evaluate();
		} else if ("moves".equalsIgnoreCase(cmd)) {
			showPossibleMoves();
		} else {
			try {
				humanMove(cmd);
			} catch (IllegalArgumentException ex) {
				println("unknown command: " + cmd);
			}
		}
	}

	private void showPossibleMoves() {
		println("possible moves:");
		Iterator<M> moves = board.generateMoves(currentPlayer);
		while (moves.hasNext()) {
			println("  " + moves.next());
		}
	}

	private void evaluate() {
		println("Evaluating current board");
		int result = board.evaluate(currentPlayer);
		println("Score for current player (" + currentPlayer + "): " + result);
	}

	private void undoMove() {
		println("Undoing last move");
		M lastMove = moves.pollLast();
		if (lastMove == null) {
			println("No moves to undo!");
			return;
		}

		board.unmake(lastMove);
		currentPlayer = currentPlayer.opponent();
		printBoard();
	}

	private void humanMove(String cmd) {
		M move = parseMove(cmd);
		if (move == null) {
			println("illegal move: " + cmd);
		} else {
			board.make(move);
			moves.add(move);
			currentPlayer = currentPlayer.opponent();
			printBoard();
		}
	}

	private void newGame() {
		init();
		println("board cleared");
	}

	private void engineMove() {
		println("computer moves...");
		M move = computeBestMove(engine, currentPlayer);
		if (move == null) {
			println("can't move!");
		} else {
			board.make(move);
			moves.add(move);
			currentPlayer = currentPlayer.opponent();
			printBoard();
		}
	}

	private void printBoard() {
		println("current board:");
		println(board);
		println("turn: " + currentPlayer);
	}

	protected void init() {
		board = createBoard();
		engine = createEngine(board);
		currentPlayer = initialPlayer();
	}

	protected void print(Object o) {
		writer.print(o);
	}

	protected void println(Object o) {
		writer.println(o);
	}

	protected void flush() {
		writer.flush();
	}

	protected abstract M computeBestMove(Engine<P, M> engine, P player);

	protected abstract M parseMove(String cmd);

	protected abstract P initialPlayer();

	protected abstract Engine<P, M> createEngine(Board<P, M> board);

	protected abstract Board<P, M> createBoard();

	private String awaitInput(Scanner scanner) {
		print("> ");
		flush();
		return scanner.next();
	}
}
