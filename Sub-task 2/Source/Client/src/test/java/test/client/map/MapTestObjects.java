package test.client.map;

import java.util.HashMap;

import client.constants.GameConstants;
import client.enumerations.ECastleValue;
import client.enumerations.ETerrainType;
import client.map.Coordinate;
import client.map.Field;
import client.map.Map;

/**
 * This MapTestObjects object creates data which should trigger a certain
 * behaviour for a certain method for the client.map package objects.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class MapTestObjects {

	private Map fakeWaterOnBorderFieldsMap;
	private Map fakeIslandCreatedMap;
	private Map fakeCastleNotFoundMap;
	private Map fakeNotEnoughTerrainTypeMap;

	public MapTestObjects() {

		generateFakeTerrainMap();
		generateFakeBorderMap();
		generateFakeCastleMap();
		generateFakeIslandMap();
	}

	/**
	 * Generates a map that consists from only grass fields, which will trigger the
	 * mapValidator object to return that the map is not valid.
	 */
	private void generateFakeTerrainMap() {

		HashMap<Coordinate, Field> fields = new HashMap<Coordinate, Field>();

		for (int width = 0; width < GameConstants.HALF_MAP_WIDTH; ++width) {
			for (int height = 0; height < GameConstants.HALF_MAP_HEIGHT; ++height) {

				Coordinate coordinate = new Coordinate(width, height);
				Field field = new Field(ETerrainType.Grass);
				field.setCoordinate(coordinate);
				fields.put(coordinate, field);
			}
		}

		// Only Grass fields
		fakeNotEnoughTerrainTypeMap = new Map(fields);
	}

	/**
	 * Generates a map which has to much water fields on the borders which will
	 * trigger the mapValidator object to return that the map is not valid.
	 */
	private void generateFakeBorderMap() {

		HashMap<Coordinate, Field> borderFields = new HashMap<Coordinate, Field>();
		for (int width = 0; width < GameConstants.HALF_MAP_WIDTH; ++width) {
			for (int height = 0; height < GameConstants.HALF_MAP_HEIGHT; ++height) {

				Coordinate coordinate = new Coordinate(width, height);
				Field field = new Field(ETerrainType.Grass);
				field.setCoordinate(coordinate);

				if (width == 0 && height == 1)
					field.setFieldTerrain(ETerrainType.Water);

				if (width == 0 && height == 2) {
					field.setFieldTerrain(ETerrainType.Water);
				}

				if (width == 7 && height == 3)
					field.setFieldTerrain(ETerrainType.Water);

				if (width == 1 && height == 0)
					field.setFieldTerrain(ETerrainType.Water);

				if (borderFields.size() % 8 == 0)
					field.setFieldTerrain(ETerrainType.Mountain);

				borderFields.put(coordinate, field);
			}
		}

		// To much water on left border
		fakeWaterOnBorderFieldsMap = new Map(borderFields);

	}

	/**
	 * Generates a map without a castle on it which will trigger the mapValidator
	 * object to return null as a castle position and the floodfill alg will return
	 * false;
	 */
	private void generateFakeCastleMap() {

		HashMap<Coordinate, Field> castleFields = new HashMap<Coordinate, Field>();
		for (int width = 0; width < GameConstants.HALF_MAP_WIDTH; ++width) {
			for (int height = 0; height < GameConstants.HALF_MAP_HEIGHT; ++height) {

				Coordinate coordinate = new Coordinate(width, height);
				Field field = new Field(ETerrainType.Grass);
				field.setCoordinate(coordinate);

				if (width == 0 && height == 1)
					field.setFieldTerrain(ETerrainType.Water);

				if (width == 7 && height == 3)
					field.setFieldTerrain(ETerrainType.Water);

				if (width == 6 && height == 3)
					field.setFieldTerrain(ETerrainType.Water);

				if (width == 1 && height == 0)
					field.setFieldTerrain(ETerrainType.Water);

				if (castleFields.size() % 8 == 0)
					field.setFieldTerrain(ETerrainType.Mountain);

				castleFields.put(coordinate, field);
			}
		}

		fakeCastleNotFoundMap = new Map(castleFields);
	}

	/**
	 * Generates a map which contains an island, which will trigger the mapValidator
	 * object to return that the map is not valid.
	 */
	private void generateFakeIslandMap() {

		HashMap<Coordinate, Field> islandFields = new HashMap<Coordinate, Field>();
		for (int width = 0; width < GameConstants.HALF_MAP_WIDTH; ++width) {
			for (int height = 0; height < GameConstants.HALF_MAP_HEIGHT; ++height) {

				Coordinate coordinate = new Coordinate(width, height);
				Field field = new Field(ETerrainType.Grass);
				field.setCoordinate(coordinate);

				if (width == 0 && height == 1)
					field.setFieldTerrain(ETerrainType.Water);

				if (width == 1 && height == 0)
					field.setFieldTerrain(ETerrainType.Water);

				if (width == 7 && height == 3)
					field.setFieldTerrain(ETerrainType.Water);

				if (width == 6 && height == 3)
					field.setFieldTerrain(ETerrainType.Water);

				if (width == 4 && height == 2)
					field.setFieldCastleContent(ECastleValue.MyCastlePresent);

				if (islandFields.size() % 8 == 0)
					field.setFieldTerrain(ETerrainType.Mountain);

				islandFields.put(coordinate, field);
			}
		}

		fakeIslandCreatedMap = new Map(islandFields);
	}

	/**
	 * Returns a map object that is not valid.
	 * 
	 * @return
	 */
	public Map getFakeWaterOnBorderFieldsMap() {
		return fakeWaterOnBorderFieldsMap;
	}

	/**
	 * Returns a map object that is not valid.
	 * 
	 * @return
	 */
	public Map getFakeIslandCreatedMap() {
		return fakeIslandCreatedMap;
	}

	/**
	 * Returns a map object that is not valid.
	 * 
	 * @return
	 */
	public Map getFakeCastleNotFoundMap() {
		return fakeCastleNotFoundMap;
	}

	/**
	 * Returns a map object that is not valid.
	 * 
	 * @return
	 */
	public Map getFakeNotEnoughTypeMap() {
		return fakeNotEnoughTerrainTypeMap;
	}

}
