package server.game;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.rules.GameConstants;

/**
 *
 * 
 * @author Nemanja Srdanovic
 *
 */
public class RunningGames {

	private static Logger logger = LoggerFactory.getLogger(RunningGames.class);

	private static LinkedHashMap<String, Game> games;

	private RunningGames() {

	}

	static {
		games = new LinkedHashMap<String, Game>();
	}

	public static LinkedHashMap<String, Game> getGames() {
		return games;
	}

	private static void setGames(LinkedHashMap<String, Game> games) {
		RunningGames.games = games;
	}

	public static void removeExpiredGames() {

		LinkedHashMap<String, Game> newGameList = new LinkedHashMap<>();

		for (Entry<String, Game> entry : RunningGames.getGames().entrySet()) {

			if (!(System.currentTimeMillis()
					- entry.getValue().getRunningTime() >= GameConstants.TIME_MILLIS_AFTER_WHICH_GAME_EXPIRES)) {

				newGameList.put(entry.getKey(), entry.getValue());

			}

		}

		RunningGames.setGames(newGameList);

	}

	public static void removeOldestGame() {

		Entry<String, Game> entry = getGames().entrySet().iterator().next();

		getGames().remove(entry.getKey());

		logger.info("Oldest game removed. ID: {}", entry.getKey());

	}

}
