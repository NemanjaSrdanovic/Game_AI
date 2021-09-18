package client.map;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import client.constants.GameConstants;
import client.enumerations.ECastleValue;
import client.enumerations.ETerrainType;
import client.move.MapSidesController;

/**
 * This map validator object is used by the client to validate if a generated
 * map has fulfilled all game specified rules.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class MapValidator {

	private Set<Field> visitedFields;
	private int expectedNumberOfAccessibleFields;

	private MapSidesController mapSidesController = new MapSidesController();

	/**
	 * Instantiates a new map validator object.
	 */
	public MapValidator() {

		this.visitedFields = new HashSet<Field>();
		this.expectedNumberOfAccessibleFields = GameConstants.NUM_OF_HALF_MAP_FIELDS
				- GameConstants.NUM_OF_GENERATED_WATER_FIELDS;
	}

	/**
	 * 
	 * Checks all map generation rules and returns if map is valid.
	 * 
	 * @param map
	 * @return boolean
	 */
	public boolean validateHalfMap(Map map) {

		return minimalTerrainTypeRule(map) && waterFieldsOnBorderRule(map) && waterFieldsIslandRule(map);

	}

	/**
	 * Each card border may only consist of less than half of water fields (maximum
	 * 3 water fields on the long and 1 water field on the short sides) in order to
	 * make the crossing between the two halves of the playing card possible.
	 * 
	 * @param map
	 * @return boolean
	 */
	private boolean waterFieldsOnBorderRule(Map map) {

		int longSideWaterFieldsUp = 0;
		int longSideWaterFieldsDown = 0;
		int shortSideWaterFieldsLeft = 0;
		int shortSideWaterFieldsRight = 0;

		for (Entry<Coordinate, Field> entry : map.getHashMap().entrySet()) {

			Field field = entry.getValue();

			if (field.getCoordinate().getY() == 0 && field.getFieldTerrain().equals(ETerrainType.Water))
				++longSideWaterFieldsDown;

			if (field.getCoordinate().getY() == 3 && field.getFieldTerrain().equals(ETerrainType.Water))
				++longSideWaterFieldsUp;

			if (field.getCoordinate().getX() == 0 && field.getFieldTerrain().equals(ETerrainType.Water))
				++shortSideWaterFieldsLeft;

			if (field.getCoordinate().getX() == 7 && field.getFieldTerrain().equals(ETerrainType.Water))
				++shortSideWaterFieldsRight;
		}

		if (longSideWaterFieldsUp > GameConstants.MAX_MAP_WATER_FIELDS_ON_HORIZONTAL_EGE
				|| longSideWaterFieldsDown > GameConstants.MAX_MAP_WATER_FIELDS_ON_HORIZONTAL_EGE
				|| shortSideWaterFieldsLeft > GameConstants.MAX_MAP_WATER_FIELDS_ON_VERTICAL_EGE
				|| shortSideWaterFieldsRight > GameConstants.MAX_MAP_WATER_FIELDS_ON_VERTICAL_EGE) {

			return false;
		} else
			return true;

	}

	/**
	 * No islands may be generated, so mountains or grass fields are never allowed
	 * to be completely enclosed by water or map boundaries (or a combination of
	 * both).
	 * 
	 * @param map
	 * @return boolean
	 */
	private boolean waterFieldsIslandRule(Map map) {

		visitedFields.clear();
		fieldAccessibilityRule(map, CastleRule(map));

		if (visitedFields.size() != expectedNumberOfAccessibleFields) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 
	 * It must be possible to generate a path of a series of valid movement commands
	 * to every field of the half map (that is not made of water) from every other
	 * field of the half map (which does not consist of water).
	 * 
	 * @param map
	 * @return boolean
	 */
	private void fieldAccessibilityRule(Map map, Field field) {

		if (field != null) {

			if (field.getFieldTerrain().equals(ETerrainType.Water)) {
				return;
			} else if (visitedFields.contains(field)) {
				return;
			} else {
				visitedFields.add(field);
			}

			for (Field currentField : mapSidesController.getNeighbors(field, map.getHashMap())) {

				fieldAccessibilityRule(map, currentField);
			}

		}

		return;
	}

	/**
	 * Every terrain type must appear, i.e. every half of the map must contain at
	 * least 3 mountain fields,15 grass fields and 4 water fields.
	 * 
	 * @param map
	 * @return boolean
	 */
	private boolean minimalTerrainTypeRule(Map map) {

		int grassFieldNum = 0;
		int mountainFieldNum = 0;
		int waterFieldNum = 0;

		for (Entry<Coordinate, Field> entry : map.getHashMap().entrySet()) {

			Field field = entry.getValue();

			if (field.getFieldTerrain().equals(ETerrainType.Grass))
				++grassFieldNum;

			if (field.getFieldTerrain().equals(ETerrainType.Mountain))
				++mountainFieldNum;

			if (field.getFieldTerrain().equals(ETerrainType.Water))
				++waterFieldNum;

		}

		if (grassFieldNum < GameConstants.MIN_MAP_GRASS_FIELDS
				|| mountainFieldNum < GameConstants.MIN_MAP_MOUNTAIN_FIELDS
				|| waterFieldNum < GameConstants.MIN_MAP_WATER_FIELDS) {

			return false;
		} else
			return true;

	}

	/**
	 * The generated map half must contain a grass field with the castle on it.
	 * 
	 * @param map
	 * @return boolean
	 */
	private Field CastleRule(Map map) {

		for (Entry<Coordinate, Field> entry : map.getHashMap().entrySet()) {

			Field field = entry.getValue();

			if (field.getFieldCastleContent().equals(ECastleValue.MyCastlePresent)) {
				return field;
			}

		}

		return null;
	}

}
