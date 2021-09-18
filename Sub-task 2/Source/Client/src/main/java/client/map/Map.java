package client.map;

import java.util.HashMap;

import client.exceptions.MapException;

/**
 * This map object is used by the client to represent an area made of field
 * objects distinguished by a x and y coordinate.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class Map {

	private HashMap<Coordinate, Field> hashMap;
	private Field castleField;

	/**
	 * Instantiates a new map object. The parameters must not be null.
	 * 
	 * @param hashMap (contains the field objects for the map)
	 */
	public Map(HashMap<Coordinate, Field> hashMap) {

		if (hashMap == null || hashMap.isEmpty())
			throw new MapException("Map exception: The field map can´t be empty or null.");

		this.hashMap = hashMap;
		this.castleField = null;
	}

	/**
	 * Get the collection with the field objects.
	 * 
	 * @return (collection)
	 */
	public HashMap<Coordinate, Field> getHashMap() {
		return hashMap;
	}

	/**
	 * Set the collection with the field objects.
	 * 
	 * @param hashMap (collection)
	 */
	public void setHashMap(HashMap<Coordinate, Field> hashMap) {

		if (hashMap == null || hashMap.isEmpty())
			throw new MapException("Map exception: The field map can´t be empty or null.");

		this.hashMap = hashMap;
	}

	/**
	 * Returns the field containing the castle.
	 * 
	 * @return (Field object from the client.map package)
	 */
	public Field getCastleField() {
		return castleField;
	}

	/**
	 * Saves the field object containing the castle.
	 * 
	 * @param castleField
	 */
	public void setCastleField(Field castleField) {
		this.castleField = castleField;
	}

}
