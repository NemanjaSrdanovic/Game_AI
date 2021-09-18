package test.client.converter;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import MessagesGameState.FullMap;
import MessagesGameState.FullMapNode;
import client.converter.Converter;
import client.exceptions.ConverterException;
import client.game.GameController;
import client.map.Map;
import client.map.MapController;

/**
 * This MapConverterTest object is used to test the main functionalities of the
 * mapConverter object.
 * 
 * @author Nemanja Srdanovic
 *
 */
class MapConverterTest {

	private static Converter converter;
	private static MapController mapController;
	private static ConverterTestObjects testObjects;

	/**
	 * Executed once, before all tests in this class use to prepare dependencies.
	 */
	@BeforeAll
	public static void setUpBeforeClass() {

		GameController controller = Mockito.mock(GameController.class);
		converter = new Converter(controller);
		mapController = new MapController();
		testObjects = new ConverterTestObjects();
	}

	/**
	 * Executed once, after all tests in this class use to clean up dependencies
	 */
	@AfterAll
	public static void tearDownAfterClass() {

		converter = null;
		mapController = null;
		testObjects = null;
	}

	/**
	 * This test is used to check if the mapConverter object throws an specified
	 * exception when one of the map fields is a null object.
	 */
	@Test
	public void mapReceived_ConverteToHalfMap_HalfMapNodeConverterException() {

		Map map = testObjects.getExceptionHalfMap();
		Executable execution = () -> converter.getMapConverter().converteMapToHalfMap("1234", map);

		Assertions.assertThrows(ConverterException.class, execution);

	}

	/**
	 * This test checks if the mapConverter is triggering an specified exception
	 * when the received network map is not complete.
	 */
	@Test
	public void fullMapReceived_ConverteToMap_FullMapNodeConverterException() {

		FullMapNode node = new FullMapNode();
		Set<FullMapNode> nodes = new LinkedHashSet<FullMapNode>();
		nodes.add(node);
		FullMap fullMap = new FullMap(nodes);

		Executable execution = () -> converter.getMapConverter().converteFullMapToMap(fullMap);

		Assertions.assertThrows(ConverterException.class, execution);

	}

	/**
	 * With this test the objects tests if the mapConverter convenes a received
	 * local map object into a network object correctly
	 */
	@Test
	public void mapReceived_ConverteToHalfMap_newHalfMapObjectCreated() {

		Map map = mapController.generateMap();

		Assertions.assertEquals(map.getHashMap().size(),
				converter.getMapConverter().converteMapToHalfMap("123", map).getNodes().size());

	}

	/**
	 * Checks if a received network map object is correctly converted in a client
	 * type object.
	 */
	@Test
	public void fullMapReceived_ConverteToMap_newMapObjectCreated() {

		Map map = mapController.generateMap();

		Assertions.assertEquals(map.getHashMap().size(), converter.getMapConverter()
				.converteFullMapToMap(testObjects.generateTestFullMapObjects(map)).getHashMap().size());
	}

}
