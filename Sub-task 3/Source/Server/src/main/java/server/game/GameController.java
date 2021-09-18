package server.game;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.FullMap;
import MessagesGameState.GameState;
import MessagesGameState.PlayerState;
import server.enumerations.EPlayerStateValue;
import server.map.MapController;
import server.map.PlayerMap;
import server.player.Player;
import server.player.PlayerController;
import server.rules.Rules;

/**
 * 
 * @author Nemanja Srdanovic
 *
 */
public class GameController {

	private MapController mapController;
	private PlayerController playerController;
	private GameGameState gameState;
	private Rules gameRules;

	public GameController() {

		this.mapController = new MapController();
		this.playerController = new PlayerController();
		this.gameState = new GameGameState(mapController);
		this.gameRules = new Rules();
	}

	private MapController getMapController() {
		return mapController;
	}

	private PlayerController getPlayerController() {
		return playerController;
	}

	private GameGameState getGameState() {

		return this.gameState;
	}

	public Rules getGameRules() {
		return gameRules;
	}

	public void addNewPlayer(PlayerRegistration playerRegistration, UniquePlayerIdentifier newPlayerID) {

		getPlayerController().getPlayers().put(newPlayerID.getUniquePlayerID(),
				new Player(playerRegistration.getStudentFirstName(), playerRegistration.getStudentLastName(),
						playerRegistration.getStudentID(), newPlayerID.getUniquePlayerID()));
	}

	public void setHalfMap(String gameID, HalfMap halfMap) {

		getMapController().setHalfMap(RunningGames.getGames().get(gameID).getGameController().getPlayerController()
				.getPlayers().get(halfMap.getUniquePlayerID()), halfMap);

		getPlayerController().getPlayers().get(halfMap.getUniquePlayerID())
				.setCurrentState(EPlayerStateValue.ShouldWait);

		getPlayerController().getSecondPlayer(halfMap.getUniquePlayerID())
				.setCurrentState(EPlayerStateValue.ShouldActNext);

		RunningGames.getGames().get(gameID).setGameRound(RunningGames.getGames().get(gameID).getGameRound() + 1);

	}

	public GameState getGameStateObject(String gameID, String playerID) {

		Player player = getPlayerController().getPlayers().get(playerID);
		Optional<FullMap> map = getMapController().getOptionalFullMap(player,
				RunningGames.getGames().get(gameID).getGameRound());
		Collection<PlayerState> players = getPlayerController().getPlayerStates(player);
		String gameStateID = getGameState().getGameStateID(player);

		return new GameState(map, players, gameStateID);
	}

	public HashMap<String, Player> getPlayers() {

		return getPlayerController().getPlayers();
	}

	public HashMap<String, PlayerMap> getHalfMaps() {

		return getMapController().getHalfMaps();
	}

}
