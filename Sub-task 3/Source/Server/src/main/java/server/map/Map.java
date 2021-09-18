package server.map;

import java.util.HashMap;

import server.exceptions.MapException;

/**
 * 
 * @author Nemanja Srdanovic
 *
 */
public class Map {

	private HashMap<Coordinate, Field> hashMap;

	public Map() {

	}

	/**
	 * Instantiates a new map object. The parameters must not be null.
	 * 
	 * @param hashMap (contains the field objects for the map)
	 */
	public Map(HashMap<Coordinate, Field> hashMap) {

		if (hashMap == null || hashMap.isEmpty())
			throw new MapException("Map exception: The field map can´t be empty or null.");

		this.hashMap = hashMap;

	}

	/**
	 * Get the collection with the field objects.
	 * 
	 * @return (collection)
	 */
	public HashMap<Coordinate, Field> getHashMap() {
		return hashMap;
	}

}
