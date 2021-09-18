package test.client.map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import client.map.MapValidator;

public class MapValidatorTest {

	private static MapTestObjects generator;
	private static MapValidator validator;

	/**
	 * Executed once, before all tests in this class to prepare dependencies.
	 */
	@BeforeAll
	public static void setUpBeforeClass() {
		generator = new MapTestObjects();
		validator = new MapValidator();
	}

	/**
	 * Executed once, after all tests in this class use to clean up dependencies.
	 */
	@AfterAll
	public static void tearDownAfterClass() {

		generator = null;
		validator = null;

	}

	/**
	 * Test that receives a invalid map object (not all terrain types) and checks if
	 * the mapValidator object Identifies that error.
	 */
	@Test
	public void mapReceived_CheckIfEnoughTerrainTypes_ReturnsFalse() {

		Assertions.assertFalse(validator.validateHalfMap(generator.getFakeNotEnoughTypeMap()));
	}

	/**
	 * Test that receives a invalid map object (to much water on border) and checks
	 * if the mapValidator object Identifies that error.
	 */
	@Test
	public void mapReceived_CheckIfNotToMuchWaterOnBorder_ReturnsFalse() {

		Assertions.assertFalse(validator.validateHalfMap(generator.getFakeWaterOnBorderFieldsMap()));
	}

	/**
	 * Test that receives a invalid map object (no castle on map) and checks if the
	 * mapValidator object Identifies that error.
	 */
	@Test
	public void mapReceived_CheckIfCastleOnMap_ReturnsFalse() {

		Assertions.assertFalse(validator.validateHalfMap(generator.getFakeCastleNotFoundMap()));
	}

	/**
	 * Test that receives a invalid map object (island on map) and checks if the
	 * mapValidator object Identifies that error.
	 */
	@Test
	public void mapReceived_CheckIfIslandOnMap_ReturnsFalse() {

		Assertions.assertFalse(validator.validateHalfMap(generator.getFakeIslandCreatedMap()));
	}
}
