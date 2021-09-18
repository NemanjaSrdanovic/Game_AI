package server.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import server.exceptions.GameRuleException;
import server.game.RunningGames;
import server.player.Player;

/**
 * 
 * @author Nemanja Srdanovic
 *
 */
public class GameRuleCheck implements IRule {

	private static Logger logger = LoggerFactory.getLogger(GameRuleCheck.class);

	public GameRuleCheck() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void newGame() {
		isMaxGameNumberExceed();

	}

	@Override
	public void registerPlayer(PlayerRegistration playerRegistration, String gameID) {
		isGameExisting(gameID);

	}

	@Override
	public void receiveHalfMap(Player player, HalfMap halfMap, String gameID) {
		isGameExisting(gameID);

	}

	@Override
	public void returnGameState(String gameID, String playerID) {
		isGameExisting(gameID);

	}

	private void isGameExisting(String gameID) {

		if (RunningGames.getGames().get(gameID) == null) {

			logger.info("A game rule was broken by sending an unexcisting gameID.");

			throw new GameRuleException("Game id not found.", "The provided gameId is invalid.");
		}
	}

	private void isMaxGameNumberExceed() {

		if (RunningGames.getGames().size() == GameConstants.MAX_PARALLEL_GAME_NUM) {

			RunningGames.removeOldestGame();

		}

	}

}
