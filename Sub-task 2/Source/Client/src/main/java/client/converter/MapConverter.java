package client.converter;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;
import MessagesGameState.FullMap;
import MessagesGameState.FullMapNode;
import client.constants.GameConstants;
import client.enumerations.ECastleValue;
import client.exceptions.ConverterException;
import client.map.Coordinate;
import client.map.Field;
import client.map.Map;

/**
 * This map converter object is used by the converter to switch client based map
 * objects into network based objects and and vice versa.
 * 
 * @author Nemanja Srdanovic
 * 
 */
public class MapConverter {

	private Converter converter;

	/**
	 * Instantiates a new map converter object. The parameters must not be null.
	 * 
	 * @param converter
	 */
	public MapConverter(Converter converter) {

		if (converter == null)
			throw new ConverterException("Converter exception: The converter can´t be null.");

		this.converter = converter;
	}

	/**
	 * This method is used by the map-converter to convert the map object into a
	 * network protocols convenient object which can be send to the server.
	 * 
	 * @param playerID (unique player number allocated from the server in the
	 *                 registration process)
	 * @param map      (Map object for the client.map package)
	 * @return (HalfMap object from the network protocol)
	 */
	public HalfMap converteMapToHalfMap(String playerID, Map map) {

		if (map == null)
			throw new ConverterException("Converter exception: The map can´t be null.");
		if (playerID.isEmpty())
			throw new ConverterException("Converter exception: The player ID can´t be empty.");

		Collection<HalfMapNode> nodes = new HashSet<HalfMapNode>();

		for (Entry<Coordinate, Field> entry : map.getHashMap().entrySet()) {

			HalfMapNode node = converteFieldToHalfMapNode(entry.getValue());

			nodes.add(node);
		}

		return new HalfMap(playerID, nodes);

	}

	/**
	 * This method is used by the map-converter to convert the network protocol
	 * convenient map object into a client object which can be used by all classes.
	 * 
	 * @param fullMap (FullMap object from the network protocol)
	 * @return (Map object for the client.map package)
	 */
	public Map converteFullMapToMap(FullMap fullMap) {

		if (fullMap == null)
			throw new ConverterException("Converter exception: The game map can´t be null.");

		HashMap<Coordinate, Field> mapHashMap = new HashMap<Coordinate, Field>();

		Collection<FullMapNode> fullMapNodesSet = fullMap.getMapNodes();

		if (fullMapNodesSet.isEmpty())
			throw new ConverterException("Converter exception: The game map node list can´t be empty.");

		Field castleField = null;

		for (Iterator<FullMapNode> it = fullMapNodesSet.iterator(); it.hasNext();) {

			FullMapNode fullMapNode = it.next();

			Field field = converteFullMapNodeToField(fullMapNode);
			Coordinate coordinate = new Coordinate(fullMapNode.getX(), fullMapNode.getY());
			field.setCoordinate(coordinate);
			mapHashMap.put(coordinate, field);

			if (field.getFieldCastleContent().equals(ECastleValue.MyCastlePresent)) {
				castleField = field;

			}

		}

		Map map = new Map(mapHashMap);
		map.setCastleField(castleField);

		return map;
	}

	/**
	 * This method is used by the map-converter to convert the maps field object
	 * into a network convenient map node object which can be send to the server.
	 * 
	 * @param field (Field object for the client.map package)
	 * @return (Map node object from the network protocol)
	 */
	private HalfMapNode converteFieldToHalfMapNode(Field field) {

		if (field == null)
			throw new ConverterException("Converter exception: The map field can´t be null.");

		boolean castlePresent = false;

		if (field.getFieldCastleContent().equals(ECastleValue.MyCastlePresent))
			castlePresent = true;

		HalfMapNode node = new HalfMapNode(field.getCoordinate().getX(), field.getCoordinate().getY(), castlePresent,
				converter.getEnumConverter().ETerrainType_To_NetworkTerrain(field.getFieldTerrain()));

		return node;
	}

	/**
	 * This method is used by the map-converter to convert the FullMap Node object
	 * into a client object which can be used by all classes.
	 * 
	 * @param fullMapNode (FullMap node object from the network protocol)
	 * @return (Field object for the client.map package)
	 */
	private Field converteFullMapNodeToField(FullMapNode fullMapNode) {

		if (fullMapNode == null)
			throw new ConverterException("Converter exception: The game map node can´t be null.");

		if (fullMapNode.getX() > GameConstants.MAX_MAP_WIDTH_FIELD
				|| fullMapNode.getY() > GameConstants.MAX_MAP_HEIGHT_FIELD)
			throw new ConverterException("Converter exception: The game map node is out of map.");

		Field field = new Field(converter.getEnumConverter().NetworkTerrain_To_ETerrainType(fullMapNode.getTerrain()));

		field.setFieldCastleContent(
				converter.getEnumConverter().NetworkFortState_To_ECastleValue(fullMapNode.getFortState()));

		field.setFieldAvatarContent(converter.getEnumConverter()
				.NetworkPlayerPositionState_To_EAvatarPositionValue(fullMapNode.getPlayerPositionState()));

		field.setFieldTreasureContent(
				converter.getEnumConverter().NetworkTreasureState_To_ETreasureValue(fullMapNode.getTreasureState()));

		return field;
	}
}
