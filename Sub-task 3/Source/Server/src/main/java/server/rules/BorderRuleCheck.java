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
public class BorderRuleCheck implements IRule {

	private static Logger logger = LoggerFactory.getLogger(BorderRuleCheck.class);

	public BorderRuleCheck() {
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

		waterFieldsOnBorderRule(player, halfMap);

	}

	@Override
	public void returnGameState(String gameID, String playerID) {
		// TODO Auto-generated method stub

	}

	/**
	 * Each card border may only consist of less than half of water fields (maximum
	 * 3 water fields on the long and 1 water field on the short sides) in order to
	 * make the crossing between the two halves of the playing card possible.
	 * 
	 * @param map
	 * @return boolean
	 */
	private void waterFieldsOnBorderRule(Player player, HalfMap halfMap) {

		int longSideWaterFieldsUp = 0;
		int longSideWaterFieldsDown = 0;
		int shortSideWaterFieldsLeft = 0;
		int shortSideWaterFieldsRight = 0;

		for (HalfMapNode entry : halfMap.getNodes()) {

			if (entry.getY() == 0 && entry.getTerrain().equals(ETerrain.Water))
				++longSideWaterFieldsDown;

			if (entry.getY() == 3 && entry.getTerrain().equals(ETerrain.Water))
				++longSideWaterFieldsUp;

			if (entry.getX() == 0 && entry.getTerrain().equals(ETerrain.Water))
				++shortSideWaterFieldsLeft;

			if (entry.getX() == 7 && entry.getTerrain().equals(ETerrain.Water))
				++shortSideWaterFieldsRight;
		}

		if (longSideWaterFieldsUp > GameConstants.MAX_MAP_WATER_FIELDS_ON_HORIZONTAL_EGE
				|| longSideWaterFieldsDown > GameConstants.MAX_MAP_WATER_FIELDS_ON_HORIZONTAL_EGE
				|| shortSideWaterFieldsLeft > GameConstants.MAX_MAP_WATER_FIELDS_ON_VERTICAL_EGE
				|| shortSideWaterFieldsRight > GameConstants.MAX_MAP_WATER_FIELDS_ON_VERTICAL_EGE) {

			logger.info("The border rule was broken by player with ID: {}", player.getPlayerID());

			throw new MapRuleException(player, "Border rule not passed. ",
					"The amount of water fields on borders is too high.");
		}

	}

}
