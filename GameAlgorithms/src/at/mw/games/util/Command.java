package at.mw.games.util;

public interface Command {
	public void execute(String[] args);

	public String help();
}
