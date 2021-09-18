package test.client.converter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import MessagesBase.ETerrain;
import MessagesGameState.EFortState;
import MessagesGameState.EPlayerPositionState;
import MessagesGameState.ETreasureState;
import MessagesGameState.FullMap;
import MessagesGameState.FullMapNode;
import client.map.Coordinate;
import client.map.Map;
import client.map.MapController;

/**
 * This ConverterTestObjects object creates data which should trigger a certain
 * behaviour for a certain method.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class ConverterTestObjects {

	private static MapController mapController;
	private static Map exceptionHalfMap;
	private static FullMap fullMap;

	/**
	 * Instantiates a new ConverterTestObjects object
	 */
	public ConverterTestObjects() {
		mapController = new MapController();
		generateTestMapObjects();
	}

	/**
	 * Generates a map object which contains a field that is null, which should
	 * trigger a certain exception.
	 */
	private static void generateTestMapObjects() {

		Map map = mapController.generateMap();

		List<Coordinate> mapKeys = new ArrayList<>(map.getHashMap().keySet());
		Random selector = new Random();

		Coordinate coordinate = mapKeys.get(selector.nextInt(mapKeys.size()));
		map.getHashMap().put(coordinate, null);

		exceptionHalfMap = map;

	}

	/**
	 * Generates a network map object which consists only from fields which have a
	 * Mountain terrain.
	 * 
	 * @param map
	 * @return
	 */
	public static FullMap generateTestFullMapObjects(Map map) {

		Set<FullMapNode> nodes = new LinkedHashSet<FullMapNode>();
		int counter = 0;
		int x = 0;
		int y = 0;

		while (map.getHashMap().size() > counter) {

			nodes.add(new FullMapNode(ETerrain.Mountain, EPlayerPositionState.NoPlayerPresent,
					ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState, x, y));
			++counter;
			++x;

			if (x == 8) {
				x = 0;
				++y;
			}

		}

		FullMap gameMap = new FullMap(nodes);

		return gameMap;
	}

	/**
	 * Returns a map which triggers an exception.
	 * 
	 * @return
	 */
	public static Map getExceptionHalfMap() {
		return exceptionHalfMap;
	}
}
