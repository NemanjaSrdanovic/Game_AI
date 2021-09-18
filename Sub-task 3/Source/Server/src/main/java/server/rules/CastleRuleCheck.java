package server.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;
import MessagesBase.PlayerRegistration;
import server.exceptions.MapRuleException;
import server.player.Player;

/**
 * 
 * @author Nemanja Srdanovic
 *
 */
public class CastleRuleCheck implements IRule {

	private static Logger logger = LoggerFactory.getLogger(CastleRuleCheck.class);

	public CastleRuleCheck() {
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

		castleRule(player, halfMap);

	}

	@Override
	public void returnGameState(String gameID, String playerID) {
		// TODO Auto-generated method stub

	}

	/**
	 * The generated map half must contain a grass field with the castle on it.
	 * 
	 * @param map
	 * @return boolean
	 */
	private void castleRule(Player player, HalfMap halfMap) {

		int castleNum = 0;
		boolean castleOnGrass = false;

		for (HalfMapNode node : halfMap.getNodes()) {

			if (node.isFortPresent() && node.getTerrain().equals(ETerrain.Grass)) {

				++castleNum;
				castleOnGrass = true;
			} else if (node.isFortPresent() && !node.getTerrain().equals(ETerrain.Grass)) {

				++castleNum;
				castleOnGrass = false;
			}

		}

		if (castleNum != GameConstants.NUM_OF_CASTLE_PER_HALF_MAP || !castleOnGrass) {

			logger.info("The castle rule was broken by player with ID: {}", player.getPlayerID());

			throw new MapRuleException(player, "Castle rule not passed.",
					"The provided half map either does not have the accurate castle number or the castle is not on a grass field");
		}
	}

}
