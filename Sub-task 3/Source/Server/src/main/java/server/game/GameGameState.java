package server.game;

import java.util.HashMap;
import java.util.UUID;

import server.map.MapController;
import server.player.Player;

/**
 * 
 * @author Nemanja Srdanovic
 *
 */
public class GameGameState {

	private MapController mapController;
	private String gameStateID;
	private HashMap<String, Player> oldPlayerState;
	private int halfMapsReceived;

	public GameGameState(MapController mapController) {

		this.mapController = mapController;
		this.oldPlayerState = new HashMap<String, Player>();
		this.gameStateID = UUID.randomUUID().toString();
		this.halfMapsReceived = mapController.getHalfMaps().size();

	}

	protected String getGameStateID(Player player) {

		Player oldPlayerData = null;

		if (oldPlayerState.get(player.getPlayerID()) == null) {

			oldPlayerState.put(player.getPlayerID(), new Player());

			oldPlayerData = oldPlayerState.get(player.getPlayerID());
			oldPlayerData.setCurrentPosition(player.getCurrentPosition());
			oldPlayerData.setCurrentState(player.getCurrentState());
			oldPlayerData.setHasCollectedTreasure(player.isHasCollectedTreasure());

		}

		oldPlayerData = oldPlayerState.get(player.getPlayerID());

		if (oldPlayerData.getCurrentPosition() == null) {

			oldPlayerData.setCurrentPosition(player.getCurrentPosition());
		}

		if (player.getCurrentPosition() != null && !oldPlayerData.getCurrentPosition().getCoordinate()
				.equals(player.getCurrentPosition().getCoordinate())) {

			oldPlayerData.setCurrentPosition(player.getCurrentPosition());
			this.gameStateID = UUID.randomUUID().toString();

		}

		if (this.halfMapsReceived != mapController.getHalfMaps().size()) {

			this.halfMapsReceived = mapController.getHalfMaps().size();

			this.gameStateID = UUID.randomUUID().toString();

		}

		if (!oldPlayerData.getCurrentState().equals(player.getCurrentState())) {

			oldPlayerData.setCurrentState(player.getCurrentState());
			this.gameStateID = UUID.randomUUID().toString();

		}

		if (oldPlayerData.isHasCollectedTreasure() != player.isHasCollectedTreasure()) {

			oldPlayerData.setHasCollectedTreasure(player.isHasCollectedTreasure());
			this.gameStateID = UUID.randomUUID().toString();

		}

		return this.gameStateID;
	}

}
