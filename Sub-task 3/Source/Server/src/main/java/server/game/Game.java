package server.game;

import MessagesBase.UniqueGameIdentifier;

/**
 * 
 * @author Nemanja Srdanovic
 *
 */
public class Game {

	private UniqueGameIdentifier gameIdentifier;
	private GameController gameController;
	private long runningTime;
	private int gameRound;

	public Game(UniqueGameIdentifier gameIdentifier) {

		this.gameIdentifier = gameIdentifier;
		this.gameController = new GameController();
		this.runningTime = System.currentTimeMillis();
		this.gameRound = 0;

	}

	public UniqueGameIdentifier getGameIdentifier() {
		return gameIdentifier;
	}

	public GameController getGameController() {
		return gameController;
	}

	public long getRunningTime() {
		return runningTime;
	}

	public int getGameRound() {
		return gameRound;
	}

	public void setGameRound(int gameRound) {
		this.gameRound = gameRound;
	}

}
