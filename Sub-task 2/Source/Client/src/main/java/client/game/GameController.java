package client.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.converter.Converter;
import client.enumerations.EMovementType;
import client.exceptions.StartParameterException;
import client.map.Map;
import client.map.MapController;
import client.move.AI;
import client.network.Network;
import client.view.Cli;

/**
 * This game controller object is used by the client to control and initialise
 * all game logic (data-exchange, converting data, game phases).
 * 
 * @author Nemanja Srdanovic
 *
 */
public class GameController {

	private Player player;
	private String gameID;
	private Network network;
	private Game game;
	private Converter converter;
	private boolean isGameActive;
	private boolean isPlayersTurn;
	private Map map;
	private Cli cli;
	private GameStateWorker gameStateWorker;
	private Thread thread;
	private boolean gameStateUpToDate;
	private int gameRound;
	private AI artificialIntelligence;
	private MapController mapController;
	private static Logger logger = LoggerFactory.getLogger(GameController.class);

	/**
	 * Instantiates a new player object. The parameters must not be null.
	 * 
	 * @param serverBaseUrl
	 * @param player
	 * @param gameID
	 * @throws StartParameterException
	 */
	public GameController(String serverBaseUrl, Player player, String gameID) throws StartParameterException {

		if (gameID.length() != 5)
			throw new StartParameterException("The gameID must have 5 charachters!");

		this.player = player;
		this.gameID = gameID;
		this.isGameActive = true;
		this.isPlayersTurn = false;

		this.network = new Network(gameID, serverBaseUrl);
		this.converter = new Converter(this);

		this.game = new Game();
		this.cli = new Cli(game);

		this.gameStateWorker = new GameStateWorker(this);
		this.thread = new Thread(gameStateWorker);

		this.gameStateUpToDate = false;
		this.gameRound = 0;

	}

	/**
	 * Starts the game and runs it in a strict order.
	 */
	public void startGame() {

		registerPlayer();
		thread.start();
		sendHalfMap();
		playGame();

	}

	/**
	 * Takes over the player data provided by user, uses the converter object to
	 * convert the data and sends them to the server by using the network object. If
	 * there are no errors, the response is the unique player id, which is than
	 * saved.
	 */
	private void registerPlayer() {

		logger.info("Registering {}", player);

		setPlayerGameID(network.registerPlayer(converter.convertePlayerData(player)));
	}

	/**
	 * Initialises a MapController object which is used to generate a new map
	 * object. At his turn the converter object is used to convert that map object
	 * and send it to the server by using the network object. There is no response
	 * to this action, so that if there are no error the game can go into the next
	 * phase.
	 */
	private void sendHalfMap() {

		mapController = new MapController();
		map = mapController.generateMap();

		boolean halfMapSent = false;

		do {

			if (isPlayersTurn()) {

				logger.info("Sending players map side.");
				network.sendHalfMap(converter.converteMapToHalfMap(map));
				halfMapSent = true;
				setGameRound(2);
				setPlayersTurn(false);

			}

		} while (!halfMapSent && isGameActive());

	}

	/**
	 * In this game phase the actual game is been played. An AI object is
	 * initialised which determines every game round, that is the players turn,
	 * which move to play next. That instruction is then converted by the converter
	 * object and the network object is used to send that move instruction to the
	 * server. This is done until the game is won or lost.
	 */
	private void playGame() {

		logger.info("The game has started.");

		boolean aiInstantiated = false;

		while (!aiInstantiated && isGameActive()) {

			if (game.getFullMap() != null) {

				artificialIntelligence = new AI(game.getFullMap(), map);
				aiInstantiated = true;
			}
		}

		do {

			if (isPlayersTurn() && isGameStateUpToDate() && isGameActive()) {

				EMovementType nextMove = artificialIntelligence.getNextMove(game.getAvatar());

				if (nextMove != null) {
					network.sendPlayerMove(converter.converteEMovementTypeToPlayerMove(nextMove));
					setGameRound(2);
					setGameStateStatus(false);
					setPlayersTurn(false);
				}

			}

		} while (isGameActive());

	}

	/**
	 * Returns the unique GameID provided by the user at the game start.
	 * 
	 * @return (String)
	 */
	private String getGameID() {
		return gameID;
	}

	/**
	 * Saves the unique player id provided by the server into the player object.
	 * 
	 * @param playerID (String)
	 */
	private void setPlayerGameID(String playerID) {
		this.player.setPlayerGameID(playerID);
	}

	/**
	 * Returns the player object initialised at the very beginning of the game.
	 * 
	 * @return (Player object from the client.game package)
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Sets the value by which is determined if the client should send more
	 * instructions to the server or not.
	 * 
	 * @param continueGame (true or false)
	 */
	public synchronized void setIsGameActive(boolean continueGame) {
		this.isGameActive = continueGame;
	}

	/**
	 * Returns if the client should continue sending instructions to the server.
	 * 
	 * @return (true for send additional, and false for stop)
	 */
	public synchronized boolean isGameActive() {
		return isGameActive;
	}

	/**
	 * Returns if the client can send the calculated instruction to the server or if
	 * he should continue waiting.
	 * 
	 * @return (true for send, and false for wait)
	 */
	private synchronized boolean isPlayersTurn() {
		return isPlayersTurn;
	}

	/**
	 * Sets the value by which is determined if the client should send the
	 * instructions to the server or wait for his turn.
	 * 
	 * @param isPlayersTurn (true for send, and false for wait)
	 */
	protected synchronized void setPlayersTurn(boolean isPlayersTurn) {
		this.isPlayersTurn = isPlayersTurn;
	}

	/**
	 * Return the network object which is responsible for sending the converted
	 * objects to the server.
	 * 
	 * @return (Network object from the client.network package)
	 */
	public synchronized Network getNetwork() {
		return network;
	}

	/**
	 * Returns the converter object which is responsible for converting the client
	 * objects into network protocol convenient objects.
	 * 
	 * @return (Converter object form the client.converter package)
	 */
	public synchronized Converter getConverter() {
		return converter;
	}

	/**
	 * Saves the converted map object returned by the server into the game object.
	 * 
	 * @param fullMap (Map object from the client.map package)
	 */
	public synchronized void setFullMap(Map fullMap) {

		getGame().setFullMap(fullMap);
	}

	/**
	 * Returns the game objects which saves the game map and player data.
	 * 
	 * @return
	 */
	public synchronized Game getGame() {
		return game;
	}

	/**
	 * Sets if the current game state has already been used or if its a new one.
	 * 
	 * @param update (true for new game state or false for already used old)
	 */
	public synchronized void setGameStateStatus(boolean update) {
		this.gameStateUpToDate = update;
	}

	/**
	 * Return if the currently saved value for the game state is the value for the
	 * old already used game state or a new one.
	 * 
	 * @return (true for new game state or false for already used old)
	 */
	private synchronized boolean isGameStateUpToDate() {
		return gameStateUpToDate;
	}

	/**
	 * Returns the current game round. Especially important for the enemyPosition
	 * setup in the GameStateWorker.
	 * 
	 * @return (Number)
	 */
	protected synchronized int getGameRound() {
		return gameRound;
	}

	/**
	 * Sets the current game round by adding 2 to the current number of game round.
	 * Two is added because the sending of a the both players instructions is
	 * considered.
	 * 
	 * @param gameRound (Number)
	 */
	private void setGameRound(int gameRound) {
		this.gameRound += gameRound;
	}

	/**
	 * Sets the final game result.
	 * 
	 * @param gameResault
	 */
	protected synchronized void setGameResault(String gameResault) {

		this.game.setGameResault(gameResault);
	}

}
