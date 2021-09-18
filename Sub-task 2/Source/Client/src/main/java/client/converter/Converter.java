package client.converter;

import java.util.Iterator;
import java.util.Set;

import MessagesBase.HalfMap;
import MessagesBase.PlayerMove;
import MessagesBase.PlayerRegistration;
import MessagesGameState.GameState;
import MessagesGameState.PlayerState;
import client.enumerations.EMovementType;
import client.exceptions.ConverterException;
import client.game.GameController;
import client.game.Player;
import client.map.Map;

/**
 * This converter object is used by the client to switch client based data
 * format into network based and vice versa.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class Converter {

	private GameController gameController;
	private PlayerState myPlayerState;
	private MapConverter mapConverter;
	private EnumConverter enumConverter;

	/**
	 * Instantiates a new converter object. The parameters must not be null.
	 * 
	 * @param gameController
	 */
	public Converter(GameController gameController) {

		// TODO EXCEPTION FOR PARAM NULL

		super();
		this.gameController = gameController;
		this.mapConverter = new MapConverter(this);
		this.enumConverter = new EnumConverter();

	}

	/**
	 * This method is used by the game controller to convert the player object into
	 * a network protocols convenient object which can be send to the server.
	 * 
	 * @param player (Player object for the client.game package)
	 * @return (PlayerRegistration object from the network protocol)
	 */
	public PlayerRegistration convertePlayerData(Player player) {

		if (player == null)
			throw new ConverterException("Converter exception: The player can´t be null.");

		return new PlayerRegistration(player.getName(), player.getSurname(), player.getStudentID());
	}

	/**
	 * This method is used by the game controller to convert the map object into a
	 * network protocols convenient object which can be send to the server.
	 * 
	 * @param map (Map object for the client.map package)
	 * @return (HalfMap object from the network protocol)
	 */
	public HalfMap converteMapToHalfMap(Map map) {

		if (map == null)
			throw new ConverterException("Converter exception: The map can´t be null.");

		return mapConverter.converteMapToHalfMap(gameController.getPlayer().getPlayerGameID(), map);

	}

	/**
	 * This method is used by the game controller to convert the move (enum)
	 * instruction into a network protocols convenient object which can be send to
	 * the server.
	 * 
	 * @param move
	 * @return
	 */
	public PlayerMove converteEMovementTypeToPlayerMove(EMovementType move) {

		if (move == null)
			throw new ConverterException("Converter exception: The move can´t be null.");

		PlayerMove playerMove = new PlayerMove();

		playerMove = PlayerMove.of(gameController.getPlayer().getPlayerGameID(),
				enumConverter.EMovementType_To_NetworkMove(move));

		return playerMove;
	}

	/**
	 * This method is used by the GameStateWorker to extract the PlayerState object
	 * provided by the server for a specific Player(PlayerID).
	 * 
	 * @param gameState
	 */
	public synchronized PlayerState getMyPlayerState(GameState gameState) {

		if (gameState == null || gameState.getPlayers().isEmpty())
			throw new ConverterException("Converter exception: GameState not convertable.");

		Set<PlayerState> gameStatePlayerSet = gameState.getPlayers();

		for (Iterator<PlayerState> it = gameStatePlayerSet.iterator(); it.hasNext();) {

			PlayerState playerState = it.next();
			if (playerState.getUniquePlayerID().equals(gameController.getPlayer().getPlayerGameID())) {
				return playerState;
			}

		}

		return null;

	}

	/**
	 * Gets the EnumConverter object which has a number of methods for converting
	 * local enumerations into network protocols convenient ones that can be send to
	 * the server and vice versa.
	 * 
	 * @return
	 */
	public EnumConverter getEnumConverter() {
		return enumConverter;
	}

	/**
	 * Gets the MapConverter object which has a number of methods for converting the
	 * Map objects into a network protocols convenient one that can be send to the
	 * server and vice versa.
	 * 
	 * @return
	 */
	public synchronized MapConverter getMapConverter() {
		return mapConverter;
	}

}
