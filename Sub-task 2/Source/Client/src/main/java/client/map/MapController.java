package client.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import client.constants.GameConstants;
import client.enumerations.ECastleValue;
import client.enumerations.ETerrainType;

/**
 * This map controller object is used by the client to generate a map object and
 * its components with all of the associated component-characteristics.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class MapController {

	private MapValidator validator;
	private Random generator;
	private Field castleField;

	/**
	 * Instantiates a new map controller object.
	 */
	public MapController() {
		super();
		this.validator = new MapValidator();
		this.generator = new Random();
	}

	/**
	 * Method used to generate and return a valid map object.
	 * 
	 * @return (map object containing HashMap<Coordinate,Field>)
	 */
	public Map generateMap() {

		Map halfMap;
		do {

			halfMap = new Map(generateHashMap());

		} while (!validator.validateHalfMap(halfMap));

		halfMap.setCastleField(castleField);

		return halfMap;

	}

	/**
	 * Method used to create a collection containing game specified coordinate and
	 * field objects.
	 * 
	 * @return (HashMap<Coordinate,Field>)
	 */
	private HashMap<Coordinate, Field> generateHashMap() {

		HashMap<Coordinate, Field> hasMap = new HashMap<Coordinate, Field>();

		List<Field> fields = generateFields();
		int fieldNumber = 0;

		for (int width = 0; width < GameConstants.HALF_MAP_WIDTH; ++width) {
			for (int height = 0; height < GameConstants.HALF_MAP_HEIGHT; ++height) {
				Coordinate fieldCoordinate = new Coordinate(width, height);
				Field field = fields.get(fieldNumber);
				field.setCoordinate(fieldCoordinate);
				hasMap.put(fieldCoordinate, field);
				++fieldNumber;
			}

		}

		return hasMap;
	}

	/**
	 * Method used to call a number of further methods with the aim of creating a
	 * list of game specified field objects needed to complete the map collection.
	 * 
	 * @return (List<Field>)
	 */
	private List<Field> generateFields() {

		return setCastle(setWaterFields(setMountainFields(makeDafaultFields())));
	}

	/**
	 * Method used to create a list of a game specified number of field with the
	 * default terrain (grass).
	 * 
	 * @return (List<Field> with ETerrain.Grass)
	 */
	private List<Field> makeDafaultFields() {

		List<Field> defaultFieldList = new ArrayList<Field>();

		for (int fieldNumber = 0; fieldNumber < GameConstants.NUM_OF_HALF_MAP_FIELDS; ++fieldNumber) {

			Field field = new Field(ETerrainType.Grass);
			defaultFieldList.add(field);
		}

		return defaultFieldList;
	}

	/**
	 * Method which receives a list of default fields and turns a game specified
	 * number of those fields into mountain fields.
	 * 
	 * @param defaultFields (List<Field> with ETerrain.Grass)
	 * @return (List<Field> with ETerrain.Grass/Mountain)
	 */
	private List<Field> setMountainFields(List<Field> defaultFields) {

		List<Field> mountainFields = defaultFields;

		for (int mountainNumber = 0; mountainNumber < GameConstants.NUM_OF_GENERATED_MOUNTAIN_FIELDS;) {

			int randomField = generator.nextInt(mountainFields.size());

			if (mountainFields.get(randomField).getFieldTerrain().equals(ETerrainType.Grass)) {

				mountainFields.get(randomField).setFieldTerrain(ETerrainType.Mountain);

				++mountainNumber;
			}

		}

		return mountainFields;
	}

	/**
	 * Method which receives a list of grass and mountain fields and turns a game
	 * specified number of the grass fields into water fields.
	 * 
	 * @param defaultFields (List<Field> with ETerrain.Grass/Mountain)
	 * @return (List<Field> with ETerrain.Grass/Mountain/Water)
	 */
	private List<Field> setWaterFields(List<Field> mountainFields) {

		List<Field> waterFields = mountainFields;

		for (int waterNumber = 0; waterNumber < GameConstants.NUM_OF_GENERATED_WATER_FIELDS;) {

			int randomField = generator.nextInt(waterFields.size());

			if (waterFields.get(randomField).getFieldTerrain().equals(ETerrainType.Grass)) {

				waterFields.get(randomField).setFieldTerrain(ETerrainType.Water);

				++waterNumber;
			}

		}

		return waterFields;
	}

	/**
	 * Method which receives a list of fields with all game specific number of
	 * terrain fields and sets a castle characteristic on a random grass field.
	 * 
	 * @param waterFields (List<Field> with ETerrain.Grass/Mountain/Water)
	 * @return (List<Field> with ETerrain.Grass/Mountain/Water & ECastleValue)
	 */
	private List<Field> setCastle(List<Field> waterFields) {

		List<Field> completeFields = waterFields;

		boolean castleSet = false;

		while (!castleSet) {

			int randomField = generator.nextInt(completeFields.size());

			if (completeFields.get(randomField).getFieldTerrain().equals(ETerrainType.Grass)) {

				Field field = completeFields.get(randomField);
				field.setFieldCastleContent(ECastleValue.MyCastlePresent);
				castleSet = true;
				castleField = field;
			}
		}

		return completeFields;
	}

}
