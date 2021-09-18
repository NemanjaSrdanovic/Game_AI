package server.rules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class IslandRuleCheck implements IRule {

	private static Logger logger = LoggerFactory.getLogger(IslandRuleCheck.class);

	private Set<HalfMapNode> visitedFields;

	public IslandRuleCheck() {

		this.visitedFields = new HashSet<HalfMapNode>();

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

		waterFieldsIslandRule(player, halfMap);

	}

	@Override
	public void returnGameState(String gameID, String playerID) {
		// TODO Auto-generated method stub

	}

	/**
	 * No islands may be generated, so mountains or grass fields are never allowed
	 * to be completely enclosed by water or map boundaries (or a combination of
	 * both).
	 * 
	 * @param map
	 * @return boolean
	 */
	private void waterFieldsIslandRule(Player player, HalfMap halfMap) {

		visitedFields.clear();
		fieldAccessibilityRule(halfMap, getCastleNode(player, halfMap));

		if (visitedFields.size() != (GameConstants.NUM_OF_HALF_MAP_FIELDS - countHalfMapWaterFields(halfMap))) {

			logger.info("The island rule was broken by player with ID: {}", player.getPlayerID());

			throw new MapRuleException(player, "Island found.",
					"There is an island on the provided half map, so that not all fields are accessible.");

		}

	}

	/**
	 * 
	 * It must be possible to generate a path of a series of valid movement commands
	 * to every field of the half map (that is not made of water) from every other
	 * field of the half map (which does not consist of water).
	 * 
	 * @param halfMap
	 * @return boolean
	 */
	private void fieldAccessibilityRule(HalfMap halfMap, HalfMapNode node) {

		if (node != null) {

			if (node.getTerrain().equals(ETerrain.Water)) {
				return;
			} else if (visitedFields.contains(node)) {
				return;
			} else {
				visitedFields.add(node);
			}

			for (HalfMapNode currentNode : getNeighbors(node, halfMap)) {

				fieldAccessibilityRule(halfMap, currentNode);
			}

		}

		return;
	}

	/**
	 * The generated map half must contain a grass field with the castle on it.
	 * 
	 * @param map
	 * @return boolean
	 */
	private HalfMapNode getCastleNode(Player player, HalfMap halfMap) {

		for (HalfMapNode node : halfMap.getNodes()) {

			if (node.isFortPresent()) {
				return node;
			}

		}

		logger.info("The castle for a map provided by with following ID could not be found: {}", player.getPlayerID());
		throw new MapRuleException(player, "No castle found.", "No castle could be found on the provided half map.");
	}

	/**
	 * Returns all neighbour fields (Fields that are to the right/left/up/down from
	 * the current field) for the hand over field and from the hand over map.
	 * 
	 * @param currentPosition
	 * @param mapSide
	 * @return
	 */
	private List<HalfMapNode> getNeighbors(HalfMapNode currentPosition, HalfMap halfMap) {

		List<HalfMapNode> neighbors = new ArrayList<HalfMapNode>();

		int coordinateX = currentPosition.getX();
		int coordinateY = currentPosition.getY();

		for (HalfMapNode node : halfMap.getNodes()) {

			if (((node.getX() == coordinateX + 1 || node.getX() == coordinateX - 1)
					|| (node.getY() == coordinateY + 1 || node.getY() == coordinateY - 1))
					&& !node.getTerrain().equals(ETerrain.Water)) {

				neighbors.add(node);
			}

		}

		return neighbors;

	}

	private int countHalfMapWaterFields(HalfMap halfMap) {

		int numOfCountedWaterFields = 0;

		for (HalfMapNode node : halfMap.getNodes()) {

			if (node.getTerrain().equals(ETerrain.Water)) {

				++numOfCountedWaterFields;
			}
		}

		return numOfCountedWaterFields;
	}

}
