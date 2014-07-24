package at.mw.games.connect4;

import java.util.Scanner;

import at.mw.games.util.CommandInterpreter;

public class Connect4 {
	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)) {
			CommandInterpreter<?, ?> interpreter = new Connect4CommandInterpreter(
					scanner, System.out);
			interpreter.run();
		}
	}
}
