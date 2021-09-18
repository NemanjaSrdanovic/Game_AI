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
public class PlayerRuleCheck implements IRule {

	private static Logger logger = LoggerFactory.getLogger(PlayerRuleCheck.class);

	public PlayerRuleCheck() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void newGame() {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerPlayer(PlayerRegistration playerRegistration, String gameID) {
		allPlayersRegistredRule(gameID);

	}

	@Override
	public void receiveHalfMap(Player player, HalfMap halfMap, String gameID) {
		isPlayerRegistredRule(gameID, halfMap.getUniquePlayerID());

	}

	@Override
	public void returnGameState(String gameID, String playerID) {
		isPlayerRegistredRule(gameID, playerID);

	}

	private void isPlayerRegistredRule(String gameID, String playerID) {

		if (RunningGames.getGames().get(gameID).getGameController().getPlayers().get(playerID) == null) {

			logger.info("A player rule was broken by sending an unexcisting playerID [" + playerID + "] for game: "
					+ gameID);

			throw new GameRuleException(

					"Player not registered",
					"The provided playerID is not registered for the game with the provided gameID.");
		}
	}

	private void allPlayersRegistredRule(String gameID) {

		if (RunningGames.getGames().get(gameID).getGameController().getPlayers()
				.size() >= GameConstants.MAX_PLAYER_NUM_PER_GAME) {

			logger.info("A player rule was broken by trying to register more than two players for game: " + gameID);

			throw new GameRuleException("Max player number registred.",
					"The maximal number of players is already registred");
		}
	}
}
