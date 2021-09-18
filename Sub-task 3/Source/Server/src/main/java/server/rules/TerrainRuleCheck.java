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
public class TerrainRuleCheck implements IRule {

	private static Logger logger = LoggerFactory.getLogger(TerrainRuleCheck.class);

	public TerrainRuleCheck() {
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

		minimalTerrainTypeRule(player, halfMap);

	}

	@Override
	public void returnGameState(String gameID, String playerID) {
		// TODO Auto-generated method stub

	}

	/**
	 * Every terrain type must appear, i.e. every half of the map must contain at
	 * least 3 mountain fields,15 grass fields and 4 water fields.
	 * 
	 * @param map
	 * @return boolean
	 */
	private void minimalTerrainTypeRule(Player player, HalfMap halfMap) {

		int grassFieldNum = 0;
		int mountainFieldNum = 0;
		int waterFieldNum = 0;

		for (HalfMapNode entry : halfMap.getNodes()) {

			if (entry.getTerrain().equals(ETerrain.Grass))
				++grassFieldNum;

			if (entry.getTerrain().equals(ETerrain.Mountain))
				++mountainFieldNum;

			if (entry.getTerrain().equals(ETerrain.Water))
				++waterFieldNum;

		}

		if (grassFieldNum < GameConstants.MIN_MAP_GRASS_FIELDS
				|| mountainFieldNum < GameConstants.MIN_MAP_MOUNTAIN_FIELDS
				|| waterFieldNum < GameConstants.MIN_MAP_WATER_FIELDS) {

			logger.info("The terrain rule was broken by player with ID: {}", player.getPlayerID());

			throw new MapRuleException(player, "Terrain type rule not passed. ",
					"The minimal terrain type number is not satisfied");
		}

	}

}
