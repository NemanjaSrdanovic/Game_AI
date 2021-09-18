package test.client.converter;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import MessagesBase.EMove;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.EPlayerGameState;
import MessagesGameState.GameState;
import MessagesGameState.PlayerState;
import client.converter.Converter;
import client.enumerations.EMovementType;
import client.exceptions.ConverterException;
import client.exceptions.StartParameterException;
import client.game.GameController;
import client.game.Player;

/**
 * This ConverterTest object is used to test the main functionalities of the
 * converter object.
 * 
 * @author Nemanja Srdanovic
 *
 */
class ConverterTest {

	private static Converter converter;
	private static GameController controller;
	private static Player player;

	/**
	 * Executed once, before all tests in this class use to prepare dependencies.
	 * 
	 * @throws StartParameterException
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws StartParameterException {

		controller = Mockito.mock(GameController.class);
		converter = new Converter(controller);
		player = new Player("Nemanja", "Srdanovic", "01576891");
		player.setPlayerGameID("a123b-dae21-d539o-dfr1");

		Mockito.when(controller.getPlayer()).thenReturn(player);
	}

	/**
	 * Executed once, after all tests in this class use to clean up dependencies
	 */
	@AfterAll
	public static void tearDownAfterClass() {
		controller = null;
		converter = null;
		player = null;
	}

	/**
	 * Used to test if the defined exception for a null player object is executed.
	 */
	@Test
	public void playerObjectReceived_converte_convertedToPlayerData() {

		Assertions.assertEquals(player.getStudentID(), converter.convertePlayerData(player).getStudentID());

		Executable execution = () -> converter.convertePlayerData(null);
		Assertions.assertThrows(ConverterException.class, execution);
	}

	/**
	 * 
	 * Tests if the converter correctly converters the local move instruction to the
	 * network adequate instruction.
	 */
	@Test
	public void EMovementTypeReceived_converte_convertedToPlayerMove() {

		EMovementType move = EMovementType.Up;
		EMove expectedMove = EMove.Up;

		Assertions.assertEquals(player.getPlayerGameID(),
				converter.converteEMovementTypeToPlayerMove(move).getUniquePlayerID());

		Assertions.assertEquals(expectedMove, converter.converteEMovementTypeToPlayerMove(move).getMove());
	}

	/**
	 * Checks if the converter correctly extracts the playerState object from the
	 * GameState.
	 */
	@Test
	public void GameStateReceived_extractsPlayerState_returnsMyPlayerState() {

		UniquePlayerIdentifier myPlayerId = new UniquePlayerIdentifier(player.getPlayerGameID());
		UniquePlayerIdentifier enemyPlayerId = new UniquePlayerIdentifier("ddr21-452f-fk38-380f");

		PlayerState myPlayerState = new PlayerState(player.getName(), player.getSurname(), player.getStudentID(),
				EPlayerGameState.Won, myPlayerId, true);

		PlayerState enemyPlayerState = new PlayerState("Enemy", "TheEnemyest", "0134999", EPlayerGameState.Lost,
				enemyPlayerId, false);

		Set<PlayerState> players = new LinkedHashSet<PlayerState>();
		players.add(enemyPlayerState);
		players.add(myPlayerState);

		GameState gameState = new GameState(players, "12345");

		Assertions.assertEquals(myPlayerState, converter.getMyPlayerState(gameState));

	}

}
