package server.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;
import MessagesGameState.FullMap;
import MessagesGameState.FullMapNode;
import server.enumerations.EAvatarPositionValue;
import server.enumerations.ECastleValue;
import server.enumerations.ETerrainType;
import server.exceptions.ConverterException;
import server.map.Coordinate;
import server.map.Field;
import server.map.Map;
import server.player.Player;
import server.rules.GameConstants;

public class MapConverter {

	private EnumConverter enumConverter;

	/**
	 * Instantiates a new map converter object.
	 * 
	 * @param converter
	 */
	public MapConverter() {

		this.enumConverter = new EnumConverter();
	}

	/**
	 * This method is used to convert the network protocol convenient map object
	 * into a server object which can be used by all classes.
	 * 
	 * @param fullMap (FullMap object from the network protocol)
	 * @return (Map object for the server.map package)
	 */
	public Map converteHalfMapToMap(HalfMap halfMap, Player player) {

		if (halfMap == null)
			throw new ConverterException("Converter exception: The game map can´t be null.");

		HashMap<Coordinate, Field> mapHashMap = new HashMap<Coordinate, Field>();

		Collection<HalfMapNode> halfMapNodesSet = halfMap.getNodes();

		if (halfMapNodesSet.isEmpty())
			throw new ConverterException("Converter exception: The game map node list can´t be empty.");

		for (Iterator<HalfMapNode> it = halfMapNodesSet.iterator(); it.hasNext();) {

			HalfMapNode halfMapNode = it.next();

			Field field = converteHalfMapNodeToField(halfMapNode);
			Coordinate coordinate = new Coordinate(halfMapNode.getX(), halfMapNode.getY());
			field.setCoordinate(coordinate);
			mapHashMap.put(coordinate, field);

			if (field.getFieldCastleContent().equals(ECastleValue.MyCastlePresent)) {

				player.setCastePosition(field);
				player.setCurrentPosition(field);
				field.setFieldAvatarContent(EAvatarPositionValue.MyAvatarPosition);
			}

		}

		Map map = new Map(mapHashMap);
		return map;
	}

	/**
	 * This method is used to convert the HalfMap Node object into a server object
	 * which can be used by all classes.
	 * 
	 * @param halfMapNode (FullMap node object from the network protocol)
	 * @return (Field object for the server.map package)
	 */
	private Field converteHalfMapNodeToField(HalfMapNode halfMapNode) {

		if (halfMapNode == null)
			throw new ConverterException("Converter exception: The game map node can´t be null.");

		if (halfMapNode.getX() > GameConstants.MAX_HALF_MAP_WIDTH_FIELD
				|| halfMapNode.getY() > GameConstants.MAX_HALF_MAP_HEIGHT_FIELD)
			throw new ConverterException("Converter exception: The game map node is out of map.");

		Field field = new Field(enumConverter.NetworkTerrain_To_ETerrainType(halfMapNode.getTerrain()));

		if (halfMapNode.isFortPresent()) {

			field.setFieldCastleContent(ECastleValue.MyCastlePresent);
		}

		return field;
	}

	/**
	 * This method is used to convert the server convenient map object into a
	 * network protocol object.
	 * 
	 * 
	 * @param map
	 * @param player
	 * @param gameRound
	 * @return
	 */
	public FullMap converteMapToFullMap(Map map, Player player, int gameRound) {

		Collection<FullMapNode> mapNodes = new HashSet<FullMapNode>();

		if (gameRound <= GameConstants.ROUND_AFTER_WHICH_REAL_POSITION_VISIBLE
				&& map.getHashMap().size() == GameConstants.NUM_OF_HALF_MAP_FIELDS * 2) {

			setRandomEnemyPosition(map, player);
		}

		for (Entry<Coordinate, Field> entry : map.getHashMap().entrySet()) {

			FullMapNode fullMapNode = converteFieldToFullMapNode(entry.getValue(), player, gameRound);

			mapNodes.add(fullMapNode);
		}

		FullMap fullMap = new FullMap(mapNodes);

		return fullMap;
	}

	/**
	 * This method is used to convert the Field object into a network protocol
	 * object.
	 * 
	 * @param field
	 * @param player
	 * @param gameRound
	 * @return
	 */
	private FullMapNode converteFieldToFullMapNode(Field field, Player player, int gameRound) {

		if (field.getFieldCastleContent().equals(ECastleValue.MyCastlePresent)
				&& !(field.getCoordinate().getX() == player.getCastePosition().getCoordinate().getX()
						&& field.getCoordinate().getY() == player.getCastePosition().getCoordinate().getY())) {

			field.setFieldCastleContent(ECastleValue.NoOrUnknownCastleState);

		}

		if (field.getFieldAvatarContent().equals(EAvatarPositionValue.MyAvatarPosition)
				&& !(field.getCoordinate().getX() == player.getCurrentPosition().getCoordinate().getX()
						&& field.getCoordinate().getY() == player.getCurrentPosition().getCoordinate().getY())
				&& gameRound > GameConstants.ROUND_AFTER_WHICH_REAL_POSITION_VISIBLE) {

			field.setFieldAvatarContent(EAvatarPositionValue.EnemyAvatarPosition);
		}

		FullMapNode fullMapNode = new FullMapNode(enumConverter.ETerrainType_To_NetworkTerrain(field.getFieldTerrain()),
				enumConverter.EAvatarPositionValue_To_NetworkPlayerPositionState(field.getFieldAvatarContent()),
				enumConverter.ETreasureValue_To_NetworkTreasureState(field.getFieldTreasureContent()),
				enumConverter.ECastleValue_To_NetworkFortState(field.getFieldCastleContent()),
				field.getCoordinate().getX(), field.getCoordinate().getY());

		return fullMapNode;
	}

	/**
	 * 
	 * This method removes the correct enemy avatar position and set the avatar on a
	 * random position.
	 * 
	 * @param map
	 * @param player
	 */
	private void setRandomEnemyPosition(Map map, Player player) {

		boolean positionSet = false;
		Coordinate currentEnemyAvatarPosition = null;

		for (Entry<Coordinate, Field> entry : map.getHashMap().entrySet()) {

			Field field = entry.getValue();

			if (field.getFieldAvatarContent().equals(EAvatarPositionValue.MyAvatarPosition)
					&& !(field.getCoordinate().getX() == player.getCurrentPosition().getCoordinate().getX()
							&& field.getCoordinate().getY() == player.getCurrentPosition().getCoordinate().getY())) {

				field.setFieldAvatarContent(EAvatarPositionValue.NoAvatarPresent);
				currentEnemyAvatarPosition = field.getCoordinate();

			}
		}

		List<Field> mapFields = new ArrayList<Field>(map.getHashMap().values());

		while (!positionSet) {

			int randomNode = new Random().nextInt(mapFields.size());

			if (mapFields.get(randomNode).getFieldTerrain().equals(ETerrainType.Water)
					&& (mapFields.get(randomNode).getCoordinate().getX() != currentEnemyAvatarPosition.getX()
							&& mapFields.get(randomNode).getCoordinate().getY() != currentEnemyAvatarPosition.getY())) {

				mapFields.get(randomNode).setFieldAvatarContent(EAvatarPositionValue.EnemyAvatarPosition);

				positionSet = true;
			}

		}

	}

}
