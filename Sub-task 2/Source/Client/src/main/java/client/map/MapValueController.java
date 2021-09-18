package client.map;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * 
 * This map value controller object is used by the client to validate if a map
 * contains a specific field and a field with a specific characteristics.
 * 
 * It was made cause the methods .contains() .get() etc. for java HashMap´s and
 * HashSet´s are not working if the object is just a bit adjusted, so that those
 * methods were useless because the objects aren't the same every time the
 * converter converts them from fullMap to map every turn.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class MapValueController {

	/**
	 * Instantiates a new map value controller object.
	 */
	public MapValueController() {
		super();
	}

	/**
	 * This method checks if the given map contains a specific field object based on
	 * the field x and y coordinate.
	 * 
	 * @param map             (HashMap<Coordinate, T> from the map object)
	 * @param coordinateInput (Coordinate object which a field object could contain)
	 * @return ( true or false )
	 */
	public <T> boolean containsCoordinateValue(HashMap<Coordinate, T> map, Coordinate coordinateInput) {

		for (Entry<Coordinate, T> entry : map.entrySet()) {

			Coordinate coordinate = entry.getKey();

			if (coordinate.getX() == coordinateInput.getX() && coordinate.getY() == coordinateInput.getY()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * This method returns a field object from the given map based on the given x
	 * and y coordinates, or return null if such field doesn't exist.
	 * 
	 * @param map             (HashMap<Coordinate, T> from the map object)
	 * @param coordinateInput (Coordinate object which a field object could contain)
	 * @return ( Field object or null if the field doesn't exist)
	 */
	public <T> T getCoordinateValue(HashMap<Coordinate, T> map, Coordinate coordinateInput) {

		for (Entry<Coordinate, T> entry : map.entrySet()) {

			Coordinate coordinate = entry.getKey();

			if (coordinate.getX() == coordinateInput.getX() && coordinate.getY() == coordinateInput.getY()) {
				return entry.getValue();
			}
		}

		return null;
	}

	/**
	 * Comparing if the two hand over field have the same coordinate on the map (are
	 * the same).
	 * 
	 * @param currentPosition
	 * @param nextField
	 * @return
	 */
	public boolean isSamePosition(Field currentPosition, Field nextField) {

		return (nextField.getCoordinate().getX() == currentPosition.getCoordinate().getX())
				&& (nextField.getCoordinate().getY() == currentPosition.getCoordinate().getY());

	}

}
