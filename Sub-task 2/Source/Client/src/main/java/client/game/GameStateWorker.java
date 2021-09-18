package client.game;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesGameState.GameState;
import MessagesGameState.PlayerState;
import client.constants.GameConstants;
import client.converter.Converter;
import client.enumerations.EAvatarPositionValue;
import client.enumerations.ECastleValue;
import client.enumerations.ETreasureValue;
import client.exceptions.MapException;
import client.map.Coordinate;
import client.map.Field;
import client.map.Map;

/**
 * This GameStateWorker object is used by the gameController to be able to work
 * off the game logic and have the updated game state in parallel, without
 * having to call a gameState function every time an wait for the response. The
 * GameStateWorker thread is sending a gameState request automatically every 0.4
 * seconds and updates the game state, game map and player state.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class GameStateWorker implements Runnable {

	private GameController gameController;
	private Converter converter;
	private GameState gameState;
	private PlayerState playerState;
	private String gameStateID;
	private Map fullMap;
	private Avatar avatar;
	private boolean enemyPostionSet;
	private static Logger logger = LoggerFactory.getLogger(GameStateWorker.class);

	/**
	 * Instantiates a new player object. The parameters must not be null.
	 * 
	 * @param gameController (GameController object from the client.game package)
	 */
	public GameStateWorker(GameController gameController) {
		super();
		this.gameController = gameController;
		this.converter = gameController.getConverter();
		this.avatar = gameController.getGame().getAvatar();
		this.gameStateID = "StartID";
		this.enemyPostionSet = false;
	}

	/**
	 * Is called after the player has registered to the game and will send a
	 * gameState request and update all data every 0.4 seconds until the game has
	 * finished. The data is only updated if the received gameState is new (has a
	 * different gameStateId than the last one)
	 */
	@Override
	public void run() {

		while (gameController.isGameActive()) {

			this.gameState = gameController.getNetwork().getGameStatus();

			if (!gameStateID.equals(gameState.getGameStateId())) {

				this.gameStateID = gameState.getGameStateId();
				this.playerState = converter.getMyPlayerState(gameState);

				if (gameState.getMap().isPresent()
						&& gameState.getMap().get().getMapNodes().size() != GameConstants.NUM_OF_HALF_MAP_FIELDS) {

					fullMap = converter.getMapConverter().converteFullMapToMap(gameState.getMap().get());

					avatar.setTreasureCollected(playerState.hasCollectedTreasure());
					setAvatarState(fullMap);
					gameController.setFullMap(fullMap);
					gameController.setGameStateStatus(true);
				}

				setGameStates();
			}

			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {

				logger.error("Game state worker exception: ", e);
			}
		}

	}

	/**
	 * Is used by the thread to update the game state, so that the gameController
	 * can send additional instructions, move to a next game phase or finish his
	 * execution.
	 */
	private synchronized void setGameStates() {

		switch (converter.getEnumConverter().NetworkPlayerGameState_To_EPlayerStateValue(playerState.getState())) {

		case Lost:
			gameController.setIsGameActive(false);
			gameController.setGameResault("The Player has lost the game.");
			break;
		case Won:
			gameController.setIsGameActive(false);
			gameController.setGameResault("The Player has won the game.");
			break;
		case ShouldActNext:
			gameController.setPlayersTurn(true);
			break;
		case ShouldWait:
			gameController.setPlayersTurn(false);
			break;
		}

	}

	/**
	 * Is used by the thread to update the position state, so that the AI calculate
	 * his instructions based on updated data.
	 * 
	 * @param fullMap (Map object from the client.map package)
	 */
	private void setAvatarState(Map fullMap) {

		if (fullMap == null || fullMap.getHashMap().isEmpty())
			throw new MapException("Map exception: The game map can´t be null.");

		for (Entry<Coordinate, Field> entry : fullMap.getHashMap().entrySet()) {

			Field field = entry.getValue();

			if (field.getFieldAvatarContent().equals(EAvatarPositionValue.BothAvatarPosition)
					|| field.getFieldAvatarContent().equals(EAvatarPositionValue.MyAvatarPosition)) {

				avatar.setCurrentPosition(field);
			}

			if (field.getFieldAvatarContent().equals(EAvatarPositionValue.BothAvatarPosition)
					|| field.getFieldAvatarContent().equals(EAvatarPositionValue.EnemyAvatarPosition)) {

				avatar.setEnemyCurrentPosition(field);
			}

			if ((!enemyPostionSet && gameController.getGameRound() > 10)
					&& (field.getFieldAvatarContent().equals(EAvatarPositionValue.BothAvatarPosition)
							|| field.getFieldAvatarContent().equals(EAvatarPositionValue.EnemyAvatarPosition))) {

				avatar.setEnemyPosition(field);
				enemyPostionSet = true;
			}

			if (field.getFieldTreasureContent().equals(ETreasureValue.MyTreasurePresent)) {
				avatar.setTreasureSaw(field);
			}

			if (field.getFieldCastleContent().equals(ECastleValue.EnemyCastlePresent)) {
				avatar.setEnemyCastleSaw(field);
			}

		}
	}

}
