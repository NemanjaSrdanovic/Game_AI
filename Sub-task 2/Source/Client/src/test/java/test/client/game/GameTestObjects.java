package test.client.game;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import MessagesGameState.FullMap;
import MessagesGameState.FullMapNode;
import client.enumerations.EAvatarPositionValue;
import client.enumerations.ECastleValue;
import client.enumerations.ETerrainType;
import client.enumerations.ETreasureValue;
import client.map.Coordinate;
import client.map.Field;
import client.map.Map;
import client.map.MapController;
import client.map.MapValueController;

/**
 * This GameTestObjects object creates data which should trigger a certain
 * behaviour for a certain method in the GameStateWorker object.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class GameTestObjects {

	private Map fullMap;

	/**
	 * Instantiates a new GameTestObjects object
	 */
	public GameTestObjects() {
		super();
		generateMapTestObject();
	}

	/**
	 * Creates a Map object that will be returned when the converter is called by
	 * the GameStateWorker object. This map contains field with certain attributes
	 * on certain positions. So that we can check if the GameStateWorker method has
	 * saved the correct fields in the avatar object.
	 */
	private void generateMapTestObject() {

		MapController controller = new MapController();
		Map map = controller.generateMap();
		MapValueController valueController = new MapValueController();

		Coordinate coordinate = valueController.getCoordinateValue(map.getHashMap(), new Coordinate(0, 0))
				.getCoordinate();
		Coordinate coordinate2 = valueController.getCoordinateValue(map.getHashMap(), new Coordinate(0, 1))
				.getCoordinate();
		Coordinate coordinate3 = valueController.getCoordinateValue(map.getHashMap(), new Coordinate(0, 2))
				.getCoordinate();
		Coordinate coordinate4 = valueController.getCoordinateValue(map.getHashMap(), new Coordinate(0, 3))
				.getCoordinate();

		Field field = new Field(coordinate, ETerrainType.Grass, ECastleValue.NoOrUnknownCastleState,
				EAvatarPositionValue.MyAvatarPosition, ETreasureValue.NoOrUnknownTreasureValue);
		Field field2 = new Field(coordinate2, ETerrainType.Grass, ECastleValue.NoOrUnknownCastleState,
				EAvatarPositionValue.NoAvatarPresent, ETreasureValue.MyTreasurePresent);
		Field field3 = new Field(coordinate3, ETerrainType.Grass, ECastleValue.NoOrUnknownCastleState,
				EAvatarPositionValue.EnemyAvatarPosition, ETreasureValue.NoOrUnknownTreasureValue);
		Field field4 = new Field(coordinate4, ETerrainType.Grass, ECastleValue.EnemyCastlePresent,
				EAvatarPositionValue.NoAvatarPresent, ETreasureValue.NoOrUnknownTreasureValue);

		map.getHashMap().put(coordinate, field);
		map.getHashMap().put(coordinate2, field2);
		map.getHashMap().put(coordinate3, field3);
		map.getHashMap().put(coordinate4, field4);

		fullMap = map;

	}

	public Map getFullMap() {
		return fullMap;
	}

	/**
	 * Returns a dummy network FullMap object used to activate the if statement in
	 * the GameStateWorkerObject
	 * 
	 * @return
	 */
	public Optional<FullMap> getDummyFullMap() {

		Set<FullMapNode> nodes = new LinkedHashSet<FullMapNode>();
		FullMap map = new FullMap(nodes);

		Optional<FullMap> mapList = Optional.of(map);

		return mapList;
	}

}
