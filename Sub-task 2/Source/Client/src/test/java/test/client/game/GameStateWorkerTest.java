package test.client.game;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.EPlayerGameState;
import MessagesGameState.GameState;
import MessagesGameState.PlayerState;
import client.converter.Converter;
import client.converter.EnumConverter;
import client.converter.MapConverter;
import client.exceptions.StartParameterException;
import client.game.Avatar;
import client.game.Game;
import client.game.GameController;
import client.game.GameStateWorker;
import client.network.Network;

class GameStateWorkerTest {

	private GameStateWorker worker;
	private static UniquePlayerIdentifier myPlayerId;
	private static PlayerState myPlayerState;
	private static GameState gameState;

	private static GameController controller;
	private static Network network;
	private static Converter converter;
	private static Game game;
	private static Avatar avatar;
	private static EnumConverter enumConverter;
	private static MapConverter mapConverter;
	private static GameTestObjects testObjects;

	/**
	 * Executed once, before all tests in this class to prepare dependencies. Most
	 * of the dependencies are mocked because this class is only used to test the
	 * gameStateWorker and all other classes would complicate that process by giving
	 * different data. In this way we can control which data is returned by which
	 * connected class and assure that the test is correctly completed.
	 * 
	 * @throws StartParameterException
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws StartParameterException {

		controller = Mockito.mock(GameController.class);
		network = Mockito.mock(Network.class);
		game = Mockito.mock(Game.class);
		avatar = Mockito.mock(Avatar.class);
		converter = Mockito.mock(Converter.class);
		enumConverter = Mockito.mock(EnumConverter.class);
		mapConverter = Mockito.mock(MapConverter.class);
		testObjects = new GameTestObjects();

		Mockito.when(controller.getGame()).thenReturn(game);
		Mockito.when(game.getAvatar()).thenReturn(avatar);
		Mockito.when(controller.getNetwork()).thenReturn(network);
		Mockito.when(controller.getConverter()).thenReturn(converter);
		Mockito.when(converter.getEnumConverter()).thenReturn(enumConverter);
		Mockito.when(converter.getMapConverter()).thenReturn(mapConverter);

		Mockito.when(enumConverter.NetworkPlayerGameState_To_EPlayerStateValue(any())).thenCallRealMethod();

	}

	/**
	 * Executed once, after all tests in this class use to clean up dependencies.
	 */
	@AfterAll
	public static void tearDownAfterClass() {

		controller = null;
		network = null;
		game = null;
		avatar = null;
		converter = null;
		enumConverter = null;

	}

	/**
	 * Executed before each tests in this class to prepare dependencies
	 */
	@BeforeEach
	public void setUp() {
		worker = new GameStateWorker(controller);
		myPlayerId = null;
		myPlayerState = null;
		gameState = null;
	}

	/**
	 * Executed after each tests in this class to prepare dependencies
	 */
	@AfterEach
	public void tearDown() {
		worker = null;
	}

	/**
	 * This test fakes a gameState request but uses the real methods to save the
	 * output (like in a real execution). By mocking the data which would be
	 * returned by the server, the data which would have to be saved is known and
	 * can be tested.
	 */
	@Test
	public void receivedGameState_setGameStates_isGameActiveFalse() {

		UniquePlayerIdentifier myPlayerId = new UniquePlayerIdentifier("abe1-fad1");
		PlayerState myPlayerState = new PlayerState("Enemy", "TheEnemyest", "0134999", EPlayerGameState.Lost,
				myPlayerId, false);
		GameState gameState = new GameState("12345");

		Mockito.when(network.getGameStatus()).thenReturn(gameState);
		Mockito.when(converter.getMyPlayerState(any())).thenReturn(myPlayerState);

		Mockito.when(controller.isGameActive()).thenCallRealMethod();
		Mockito.doCallRealMethod().when(controller).setIsGameActive(anyBoolean());

		controller.setIsGameActive(true);
		worker.run();

		Assertions.assertEquals(false, controller.isGameActive());

	}

	/**
	 * The test is mocking a received GameState which contains a FullMap object that
	 * has to be converted and the data saved in the avatar object. The data is
	 * already known and the process can be tested by comparing the fake data and
	 * the data that the process saves in the avatar object. For that we mock the
	 * avatar object but enable the methods which save and extract the data so that
	 * we can use them like i a real time execution.
	 */
	@Test
	public void receivedFullMap_setAvatarState_AllFieldsSet() {

		UniquePlayerIdentifier myPlayerId = new UniquePlayerIdentifier("abe1-fad1");
		PlayerState myPlayerState = new PlayerState("Enemy", "TheEnemyest", "0134999", EPlayerGameState.Won, myPlayerId,
				true);
		Set<PlayerState> players = new LinkedHashSet<PlayerState>();
		players.add(myPlayerState);

		GameState gameState = new GameState(testObjects.getDummyFullMap(), players, "12345");

		Mockito.when(network.getGameStatus()).thenReturn(gameState);
		Mockito.when(converter.getMyPlayerState(any())).thenReturn(myPlayerState);
		Mockito.when(mapConverter.converteFullMapToMap(any())).thenReturn(testObjects.getFullMap());

		Mockito.doNothing().when(controller).setFullMap(any());
		Mockito.doCallRealMethod().when(controller).setGameStateStatus(anyBoolean());

		Mockito.doCallRealMethod().when(avatar).setTreasureCollected(anyBoolean());
		Mockito.doCallRealMethod().when(avatar).setCurrentPosition(any());
		Mockito.doCallRealMethod().when(avatar).setEnemyCurrentPosition(any());
		Mockito.doCallRealMethod().when(avatar).setTreasureSaw(any());
		Mockito.doCallRealMethod().when(avatar).setEnemyCastleSaw(any());

		Mockito.when(avatar.getCurrentPosition()).thenCallRealMethod();
		Mockito.when(avatar.getEnemyCurrentPosition()).thenCallRealMethod();
		Mockito.when(avatar.getTreasureSaw()).thenCallRealMethod();
		Mockito.when(avatar.getEnemyCastleSaw()).thenCallRealMethod();
		Mockito.when(avatar.isTreasureCollected()).thenCallRealMethod();

		controller.setIsGameActive(true);
		worker.run();

		Assertions.assertEquals(true, avatar.isTreasureCollected());
		Assertions.assertEquals(0, avatar.getCurrentPosition().getCoordinate().getY());
		Assertions.assertEquals(1, avatar.getTreasureSaw().getCoordinate().getY());
		Assertions.assertEquals(2, avatar.getEnemyCurrentPosition().getCoordinate().getY());
		Assertions.assertEquals(3, avatar.getEnemyCastleSaw().getCoordinate().getY());

	}

}
