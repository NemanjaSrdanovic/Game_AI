package client.map;

import client.enumerations.EAvatarPositionValue;
import client.enumerations.ECastleValue;
import client.enumerations.ETerrainType;
import client.enumerations.ETreasureValue;
import client.exceptions.MapException;

/**
 * This field object is used by the client to add nodes to the map object by
 * which every map node can be distinguished
 * 
 * @author Nemanja Srdanovic
 *
 */
public class Field {

	private Coordinate coordinate;
	private ETerrainType fieldTerrain;
	private ECastleValue fieldCastleContent;
	private ETreasureValue fieldTreasureContent;
	private EAvatarPositionValue fieldAvatarContent;

	/**
	 * Instantiates a new field object which still hasn't a fixed position on the
	 * map but has all default field-characteristics. The parameters must not be
	 * null.
	 * 
	 * @param fieldTerrain
	 */
	public Field(ETerrainType fieldTerrain) {

		if (fieldTerrain == null)
			throw new MapException("Map exception: The field terrain can´t be null.");

		this.coordinate = null;
		this.fieldTerrain = fieldTerrain;
		this.fieldCastleContent = ECastleValue.NoOrUnknownCastleState;
		this.fieldTreasureContent = ETreasureValue.NoOrUnknownTreasureValue;
		this.fieldAvatarContent = EAvatarPositionValue.NoAvatarPresent;

	}

	/**
	 * Instantiates a new field object with a fixed position on the map but not all
	 * field-characteristics.
	 * 
	 * @param coordinate
	 * @param fieldTerrain
	 */
	public Field(Coordinate coordinate, ETerrainType fieldTerrain) {

		super();
		this.coordinate = coordinate;
		this.fieldTerrain = fieldTerrain;
	}

	/**
	 * 
	 * Instantiates a new field object with a given position on the map and all
	 * field-characteristics. The parameters must not be null.
	 * 
	 * @param coordinate
	 * @param fieldTerrain
	 * @param fieldCastleContent
	 * @param fieldAvatarContent
	 */
	public Field(Coordinate coordinate, ETerrainType fieldTerrain, ECastleValue fieldCastleContent,
			EAvatarPositionValue fieldAvatarContent, ETreasureValue fieldTreasureContent) {

		if (coordinate == null || fieldTerrain == null || fieldCastleContent == null || fieldAvatarContent == null
				|| fieldTreasureContent == null) {
			throw new MapException("Map exception: All field parameters must be filled out.");
		}

		this.coordinate = coordinate;
		this.fieldTerrain = fieldTerrain;
		this.fieldCastleContent = fieldCastleContent;
		this.fieldAvatarContent = fieldAvatarContent;
		this.fieldTreasureContent = fieldTreasureContent;
	}

	/**
	 * Get fields horizontal and vertical position (coordinate).
	 * 
	 * @return (coordinate object with x and y position).
	 */
	public Coordinate getCoordinate() {
		return coordinate;
	}

	/**
	 * Set fields horizontal and vertical position (coordinate).
	 * 
	 * @param (coordinate object with x and y position).
	 */
	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	/**
	 * Get the terrain used by this field.
	 * 
	 * @return (the terrain, e.g., Grass or Mountain)
	 */
	public ETerrainType getFieldTerrain() {
		return fieldTerrain;
	}

	/**
	 * Set the terrain used by this field.
	 * 
	 * @param fieldTerrain (the terrain, e.g., Grass or Mountain)
	 */
	public void setFieldTerrain(ETerrainType fieldTerrain) {
		this.fieldTerrain = fieldTerrain;
	}

	/**
	 * Get the castle characteristics used by this field.
	 * 
	 * @return (the characteristics e.g., EnemyCastlePresent, MyCastlePresent)
	 */
	public ECastleValue getFieldCastleContent() {
		return fieldCastleContent;
	}

	/**
	 * Set the castle characteristics used by this field.
	 * 
	 * @param (the characteristics e.g., EnemyCastlePresent, MyCastlePresent)
	 */
	public void setFieldCastleContent(ECastleValue fieldCastleContent) {
		this.fieldCastleContent = fieldCastleContent;
	}

	/**
	 * Get the treasure characteristics used by this field.
	 * 
	 * @return (the characteristics e.g., MyTreasurePresent)
	 */
	public ETreasureValue getFieldTreasureContent() {
		return fieldTreasureContent;
	}

	/**
	 * Set the treasure characteristics used by this field.
	 * 
	 * @param (the characteristics e.g., MyTreasurePresent)
	 */
	public void setFieldTreasureContent(ETreasureValue fieldTreasureContent) {
		this.fieldTreasureContent = fieldTreasureContent;
	}

	/**
	 * Get the avatar characteristics used by this field.
	 * 
	 * @return (the characteristics e.g., EnemyAvatarPosition, MyAvatarPosition)
	 */
	public EAvatarPositionValue getFieldAvatarContent() {
		return fieldAvatarContent;
	}

	/**
	 * Set the avatar characteristics used by this field.
	 * 
	 * @param (the characteristics e.g., EnemyAvatarPosition, MyAvatarPosition)
	 */
	public void setFieldAvatarContent(EAvatarPositionValue fieldAvatarContent) {
		this.fieldAvatarContent = fieldAvatarContent;
	}

	/**
	 * Implements a working toString method for this object to ease debugging.
	 */
	@Override
	public String toString() {
		return "Field [coordinate=" + coordinate + ", fieldTerrain=" + fieldTerrain + ", fieldCastleContent="
				+ fieldCastleContent + ", fieldTreasureContent=" + fieldTreasureContent + ", fieldAvatarContent="
				+ fieldAvatarContent + "]";
	}

}
