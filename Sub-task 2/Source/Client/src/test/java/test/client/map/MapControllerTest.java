package test.client.map;

import java.util.Map.Entry;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;

import client.map.Coordinate;
import client.map.Field;
import client.map.Map;
import client.map.MapController;

public class MapControllerTest {

	private static Map previousMap;
	private static MapController mapController;

	/**
	 * Executed once, before all tests in this class to prepare dependencies.
	 */
	@BeforeAll
	public static void setUpBeforeClass() {

		previousMap = null;
		mapController = new MapController();
		previousMap = mapController.generateMap();
	}

	/**
	 * Executed once, after all tests in this class use to clean up dependencies.
	 */
	@AfterAll
	public static void tearDownAfterClass() {

		previousMap = null;
	}

	/**
	 * The test is used to check if the generated maps differ in at least 5 fields
	 * (like the server would have). To ensure correct result this test is conducted
	 * 5 times in a row.
	 * 
	 */
	@RepeatedTest(value = 5)
	public void mapGenerated_CheckIfTheNextMapDifferentiates_shouldReturnTrueEachTime() {

		Assertions.assertTrue(isTheNewMapDifferentEnough(previousMap, mapController.generateMap()));
	}

	/**
	 * Compares two hand over maps and confirms (or not) that the maps differ in at
	 * least 5 fields.
	 * 
	 * @param previousMap
	 * @param newMap
	 * @return
	 */
	private boolean isTheNewMapDifferentEnough(Map previousMap, Map newMap) {

		int difference = 0;

		for (Entry<Coordinate, Field> newEntry : newMap.getHashMap().entrySet()) {

			if (difference >= 5)
				return true;

			Field newField = newEntry.getValue();
			Coordinate newCoordinate = newEntry.getKey();

			for (Entry<Coordinate, Field> previousEntry : previousMap.getHashMap().entrySet()) {

				Field previousField = previousEntry.getValue();
				Coordinate previousCoordinate = previousEntry.getKey();

				if (previousCoordinate.getX() == newCoordinate.getX()
						&& previousCoordinate.getY() == newCoordinate.getY()) {

					if (!previousField.getFieldTerrain().equals(newField.getFieldTerrain())) {
						++difference;
					}

					break;
				}
			}

		}

		return false;
	}

}
