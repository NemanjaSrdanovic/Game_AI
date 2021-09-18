package client.move;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import client.enumerations.ETerrainType;
import client.map.Coordinate;
import client.map.Field;
import client.map.Map;
import client.map.MapValueController;

/**
 * This MSC object is used by the AI to to separate the converted game map into
 * the player side and opponent side, so that the route/treasure/castle finding
 * operations are easier to execute. Besides the object is returning the
 * neighbour fields, for all fields on the game map, which is used by a lot of
 * other objects in the client.move package.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class MapSidesController {

	private int mySideFarestLeftCoordinate;
	private int mySideFarestRightCoordinate;
	private int mySideFarestUpCoordinate;
	private int mySideFarestDownCoordinate;

	private HashMap<Coordinate, Field> myMapSide;
	private HashMap<Coordinate, Field> opponentsMapSide;
	private Map gameMap;

	private MapValueController mapValueController;

	/**
	 * Instantiates a new map object. The parameters must not be null.
	 */
	public MapSidesController() {
		super();
		this.mapValueController = new MapValueController();
	}

	/**
	 * Instantiates a new map object. The parameters must not be null.
	 * 
	 * @param gameMap
	 * @param sentHalfMap
	 */
	public MapSidesController(Map gameMap, Map sentHalfMap) {
		super();
		this.myMapSide = new HashMap<>();
		this.opponentsMapSide = new HashMap<>();
		this.mapValueController = new MapValueController();
		this.gameMap = gameMap;

		determineMyMapSide(gameMap, sentHalfMap);

	}

	/**
	 * Determines the player and opponent map side by using the halfmap which the
	 * player has send, the full map which the server has returned, the player
	 * castle position on the map and saves those fields into separate HashMaps.
	 * 
	 * @param fullMap
	 * @param sentHalfMap
	 */
	private void determineMyMapSide(Map fullMap, Map sentHalfMap) {

		setCastleBordersDistances(fullMap.getCastleField().getCoordinate().getX(),
				fullMap.getCastleField().getCoordinate().getY(), sentHalfMap.getCastleField().getCoordinate().getX(),
				sentHalfMap.getCastleField().getCoordinate().getY());

		for (Entry<Coordinate, Field> entry : fullMap.getHashMap().entrySet()) {

			Field field = entry.getValue();
			Coordinate coordinate = entry.getKey();

			if (isMyField(coordinate)) {
				myMapSide.put(coordinate, field);
			} else {
				opponentsMapSide.put(coordinate, field);
			}

		}

	}

	/**
	 * Used by the method which determines the map sides, to set the distances from
	 * the castle fields to the borders, so that those parameters can be used when
	 * determining the map sides.
	 * 
	 * @param newXCoordinate
	 * @param newYCoordinate
	 * @param oldXCoordinate
	 * @param oldYCoordinate
	 */
	private void setCastleBordersDistances(int newXCoordinate, int newYCoordinate, int oldXCoordinate,
			int oldYCoordinate) {

		mySideFarestLeftCoordinate = newXCoordinate + (0 - oldXCoordinate);
		mySideFarestRightCoordinate = newXCoordinate + (7 - oldXCoordinate);

		mySideFarestUpCoordinate = newYCoordinate + (0 - oldYCoordinate);
		mySideFarestDownCoordinate = newYCoordinate + (3 - oldYCoordinate);

	}

	/**
	 * Used by the method which determines the map sides to determine if the field
	 * from the game map is a field from the players map side.
	 * 
	 * @param coordinate
	 * @return
	 */
	private boolean isMyField(Coordinate coordinate) {

		if ((coordinate.getX() >= mySideFarestLeftCoordinate && coordinate.getX() <= mySideFarestRightCoordinate)
				&& (coordinate.getY() >= mySideFarestUpCoordinate && coordinate.getY() <= mySideFarestDownCoordinate))
			return true;

		return false;

	}

	/**
	 * Returns all neighbour fields (Fields that are to the right/left/up/down from
	 * the current field) for the hand over field and from the hand over map.
	 * 
	 * @param currentPosition
	 * @param mapSide
	 * @return
	 */
	public List<Field> getNeighbors(Field currentPosition, HashMap<Coordinate, Field> mapSide) {

		List<Field> neighbors = new ArrayList<Field>();

		int coordinateX = currentPosition.getCoordinate().getX();
		int coordinateY = currentPosition.getCoordinate().getY();

		Field fieldRight = mapValueController.getCoordinateValue(mapSide, new Coordinate(coordinateX + 1, coordinateY));
		Field fieldLeft = mapValueController.getCoordinateValue(mapSide, new Coordinate(coordinateX - 1, coordinateY));

		Field fieldDown = mapValueController.getCoordinateValue(mapSide, new Coordinate(coordinateX, coordinateY + 1));

		Field fieldUp = mapValueController.getCoordinateValue(mapSide, new Coordinate(coordinateX, coordinateY - 1));

		if (fieldRight != null && !fieldRight.getFieldTerrain().equals(ETerrainType.Water)) {
			neighbors.add(fieldRight);
		}

		if (fieldLeft != null && !fieldLeft.getFieldTerrain().equals(ETerrainType.Water)) {
			neighbors.add(fieldLeft);
		}

		if (fieldDown != null && !fieldDown.getFieldTerrain().equals(ETerrainType.Water)) {
			neighbors.add(fieldDown);
		}

		if (fieldUp != null && !fieldUp.getFieldTerrain().equals(ETerrainType.Water)) {
			neighbors.add(fieldUp);
		}

		return neighbors;

	}

	/**
	 * Returns a HashMap containing all fields and coordinates that belong to the
	 * players map side.
	 * 
	 * @return
	 */
	public HashMap<Coordinate, Field> getMyMapSide() {
		return myMapSide;
	}

	/**
	 * Returns a HashMap containing all fields and coordinates that belong to the
	 * oponnents map side.
	 * 
	 * @return
	 */
	public HashMap<Coordinate, Field> getOpponentsMapSide() {
		return opponentsMapSide;
	}

	/**
	 * Returns the converted game map returned from the server.
	 * 
	 * @return
	 */
	public Map getGameMap() {
		return gameMap;
	}

}
