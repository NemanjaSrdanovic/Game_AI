package server.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import server.exceptions.MapRuleException;
import server.game.RunningGames;
import server.player.Player;

/**
 * 
 * @author Nemanja Srdanovic
 *
 */
public class TooMuchMapsSentRuleCheck implements IRule {

	private static Logger logger = LoggerFactory.getLogger(TooMuchMapsSentRuleCheck.class);

	public TooMuchMapsSentRuleCheck() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void newGame() {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerPlayer(PlayerRegistration playerRegistration, String gameID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveHalfMap(Player player, HalfMap halfMap, String gameID) {
		mapAlreadySentRule(gameID, player);

	}

	@Override
	public void returnGameState(String gameID, String playerID) {
		// TODO Auto-generated method stub

	}

	private void mapAlreadySentRule(String gameID, Player player) {

		if (RunningGames.getGames().get(gameID).getGameController().getHalfMaps().get(player.getPlayerID()) != null) {

			logger.info("A map rule was broken by trying to send more than one half map by player with id: "
					+ player.getPlayerID());

			throw new MapRuleException(player, "Map already sent.",
					"The player id( " + player.getPlayerID() + " ) has already sent his map side.");

		}
	}
}
