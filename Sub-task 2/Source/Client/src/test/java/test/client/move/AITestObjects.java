package test.client.move;

import java.util.HashMap;

import client.constants.GameConstants;
import client.enumerations.ECastleValue;
import client.enumerations.ETerrainType;
import client.map.Coordinate;
import client.map.Field;
import client.map.Map;
import client.map.MapController;
import client.map.MapValueController;

/**
 * This AITestObjects object creates data which should trigger a certain
 * behaviour for a certain method in the AI object.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class AITestObjects {

	private static Map myMapSide;
	private static Map opponentsMapSide;
	private static Map gameMap;
	private static MapController mapController;
	private static MapValueController mapValueController;
	private static Field avatarNextToTreasureField;
	private static Field treasureField;
	private static Field enemyCastleField;
	private static Field avatarPositionWhenCastleSaw;
	private static Field enemyAvatarPositionOnTenthTurn;

	/**
	 * Instantiates a new AITestObjects object
	 */
	public AITestObjects() {
		mapValueController = new MapValueController();
		mapController = new MapController();
		myMapSide = mapController.generateMap();
		opponentsMapSide = mapController.generateMap();
		gameMap = combineMaps(myMapSide, opponentsMapSide);
	}

	/**
	 * Combines two hand over half maps (4x8) into one fullMap (8x8)
	 * 
	 * @param firstMap
	 * @param secondMap
	 * @return
	 */
	private static Map combineMaps(Map firstMap, Map secondMap) {

		firstMap.getCastleField().setFieldCastleContent(ECastleValue.NoOrUnknownCastleState);

		HashMap<Coordinate, Field> mapFields = new HashMap<Coordinate, Field>();

		for (int height = 0; height < GameConstants.HALF_MAP_HEIGHT; ++height) {
			for (int width = 0; width < GameConstants.HALF_MAP_WIDTH; ++width) {

				Coordinate coordinateUp = new Coordinate(width, height);
				Field fieldUp = mapValueController.getCoordinateValue(firstMap.getHashMap(), coordinateUp);
				fieldUp.setCoordinate(coordinateUp);
				mapFields.put(coordinateUp, fieldUp);

				Field fieldDown = mapValueController.getCoordinateValue(secondMap.getHashMap(), coordinateUp);
				Coordinate coordinateDown = new Coordinate(width, height + 4);
				fieldDown.setCoordinate(coordinateDown);
				mapFields.put(coordinateDown, fieldDown);

			}

		}

		Map fullMap = new Map(mapFields);
		Field castleField = mapValueController.getCoordinateValue(fullMap.getHashMap(), new Coordinate(0, 0));
		castleField.setFieldCastleContent(ECastleValue.MyCastlePresent);
		fullMap.setCastleField(castleField);

		return fullMap;

	}

	/**
	 * Generates a map which has only one legit move from the position which is also
	 * saved throug the making process of this map object.
	 * 
	 * @return
	 */
	public static Map getOnlyOneNeighbourMap() {

		Map newMap = gameMap;

		mapValueController.getCoordinateValue(newMap.getHashMap(), new Coordinate(0, 0))
				.setFieldTerrain(ETerrainType.Grass);
		newMap.setCastleField(mapValueController.getCoordinateValue(newMap.getHashMap(), new Coordinate(0, 0)));
		myMapSide.setCastleField(mapValueController.getCoordinateValue(newMap.getHashMap(), new Coordinate(0, 0)));

		mapValueController.getCoordinateValue(newMap.getHashMap(), new Coordinate(0, 1))
				.setFieldTerrain(ETerrainType.Water);
		mapValueController.getCoordinateValue(newMap.getHashMap(), new Coordinate(1, 0))
				.setFieldTerrain(ETerrainType.Grass);

		return newMap;
	}

	/**
	 * Returns a map and a avatar position from which the avatar has 4 legit move
	 * options. One of the 4 fields is marked as treasure position.
	 * 
	 * @return
	 */
	public static Map getAllNeighboursGrassMap() {

		Map grassMap = myMapSide;

		mapValueController.getCoordinateValue(grassMap.getHashMap(), new Coordinate(4, 1))
				.setFieldTerrain(ETerrainType.Grass);
		mapValueController.getCoordinateValue(grassMap.getHashMap(), new Coordinate(4, 2))
				.setFieldTerrain(ETerrainType.Grass);
		mapValueController.getCoordinateValue(grassMap.getHashMap(), new Coordinate(4, 3))
				.setFieldTerrain(ETerrainType.Grass);
		mapValueController.getCoordinateValue(grassMap.getHashMap(), new Coordinate(3, 2))
				.setFieldTerrain(ETerrainType.Grass);
		mapValueController.getCoordinateValue(grassMap.getHashMap(), new Coordinate(5, 2))
				.setFieldTerrain(ETerrainType.Grass);

		avatarNextToTreasureField = mapValueController.getCoordinateValue(grassMap.getHashMap(), new Coordinate(4, 2));
		treasureField = mapValueController.getCoordinateValue(grassMap.getHashMap(), new Coordinate(3, 2));
		return grassMap;
	}

	/**
	 * Returns a game Map and a mountain field avatar position, from which the
	 * enemyCastle position field is visible.
	 * 
	 * @return
	 */
	public static Map getTwoFieldsFromEnemyCastleMap() {

		Map enemyCastleMap = gameMap;

		mapValueController.getCoordinateValue(enemyCastleMap.getHashMap(), new Coordinate(3, 2))
				.setFieldTerrain(ETerrainType.Mountain);
		mapValueController.getCoordinateValue(enemyCastleMap.getHashMap(), new Coordinate(3, 3))
				.setFieldTerrain(ETerrainType.Grass);
		mapValueController.getCoordinateValue(enemyCastleMap.getHashMap(), new Coordinate(3, 4))
				.setFieldTerrain(ETerrainType.Grass);
		mapValueController.getCoordinateValue(enemyCastleMap.getHashMap(), new Coordinate(3, 4))
				.setFieldCastleContent(ECastleValue.EnemyCastlePresent);

		avatarPositionWhenCastleSaw = mapValueController.getCoordinateValue(enemyCastleMap.getHashMap(),
				new Coordinate(3, 2));
		enemyCastleField = mapValueController.getCoordinateValue(enemyCastleMap.getHashMap(), new Coordinate(3, 4));
		enemyAvatarPositionOnTenthTurn = mapValueController.getCoordinateValue(enemyCastleMap.getHashMap(),
				new Coordinate(3, 6));

		return enemyCastleMap;
	}

	/**
	 * Retruns a map object which simulates that all surrounding positions (posible
	 * enemyCastle fields9 have been checked. And a unvisited grass Field two fields
	 * away from the current avatar position.
	 * 
	 * @return
	 */
	public static Map allPosibleEnemyCastlePositionsVisited() {

		Map castleFieldsMap = gameMap;

		for (int width = 0; width < 4; ++width) {
			for (int height = 5; height < 8; ++height) {

				mapValueController.getCoordinateValue(castleFieldsMap.getHashMap(), new Coordinate(width, height))
						.setFieldTerrain(ETerrainType.Mountain);
			}

		}

		mapValueController.getCoordinateValue(castleFieldsMap.getHashMap(), new Coordinate(0, 4))
				.setFieldTerrain(ETerrainType.Grass);
		enemyAvatarPositionOnTenthTurn = mapValueController.getCoordinateValue(castleFieldsMap.getHashMap(),
				new Coordinate(0, 7));

		return castleFieldsMap;
	}

	/**
	 * Returns the enemyAvatar position which is in association with the
	 * allPosibleEnemyCastlePositionsVisited map
	 * 
	 * @return
	 */
	public static Field getEnemyAvatarPositionOnTenthTurn() {
		return enemyAvatarPositionOnTenthTurn;
	}

	/**
	 * Returns the castleField position which is in association with the
	 * getTwoFieldsFromEnemyCastleMap map
	 * 
	 * @return
	 */
	public static Field getEnemyCastleField() {
		return enemyCastleField;
	}

	/**
	 * Returns the avatar position which is in association with the
	 * getTwoFieldsFromEnemyCastleMap map
	 * 
	 * @return
	 */
	public static Field getAvatarPositionWhenCastleSaw() {
		return avatarPositionWhenCastleSaw;
	}

	/**
	 * Returns the avatar position which is in association with the
	 * getAllNeighboursGrassMap map
	 * 
	 * @return
	 */
	public static Field getAvatarNextToTreasureField() {
		return avatarNextToTreasureField;
	}

	/**
	 * Returns the treasure position which is in association with the
	 * getAllNeighboursGrassMap map
	 * 
	 * @return
	 */
	public static Field getTreasureField() {
		return treasureField;
	}

	/**
	 * Returns the players map side calculated by this object based on the hand over
	 * maps.
	 * 
	 * @return
	 */
	public static Map getMyMapSide() {
		return myMapSide;
	}

	/**
	 * Returns the opponents map side calculated by this object based on the hand
	 * over maps.
	 * 
	 * @return
	 */
	public static Map getOpponentsMapSide() {
		return opponentsMapSide;
	}

	/**
	 * Returns the complete map calculated by this object based on the hand over
	 * maps.
	 * 
	 * @return
	 */
	public static Map getGameMap() {
		return gameMap;
	}

}
