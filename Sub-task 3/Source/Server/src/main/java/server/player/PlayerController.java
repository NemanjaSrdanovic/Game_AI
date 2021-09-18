package server.player;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesGameState.PlayerState;
import server.converter.PlayerConverter;
import server.enumerations.EPlayerStateValue;

/**
 * 
 * @author Nemanja Srdanovic
 *
 */
public class PlayerController {

	private static Logger logger = LoggerFactory.getLogger(PlayerController.class);
	private HashMap<String, Player> players;
	private PlayerConverter playerConverter;
	private boolean firstPlayerChoosen;

	public PlayerController() {

		this.players = new HashMap<String, Player>();
		this.playerConverter = new PlayerConverter();
		this.firstPlayerChoosen = false;
	}

	public HashMap<String, Player> getPlayers() {
		return players;
	}

	public Player getSecondPlayer(String firstPlayerID) {

		for (Entry<String, Player> entry : players.entrySet()) {

			if (!entry.getKey().equals(firstPlayerID)) {

				return entry.getValue();
			}
		}

		logger.error("server.player.PlayerController returned null. The second player was not found.");
		return null;
	}

	public Collection<PlayerState> getPlayerStates(Player player) {

		Collection<PlayerState> playerStates = new HashSet<>();

		if (this.players.size() == 2) {

			Random random = new Random();
			boolean canSendNext = random.nextBoolean();

			if (canSendNext || this.firstPlayerChoosen) {

				switch (getSecondPlayer(player.getPlayerID()).getCurrentState()) {

				case ShouldActNext:
					player.setCurrentState(EPlayerStateValue.ShouldWait);
					break;
				case ShouldWait:
					player.setCurrentState(EPlayerStateValue.ShouldActNext);
					break;
				case Lost:
					player.setCurrentState(EPlayerStateValue.Won);
					break;
				case Won:
					player.setCurrentState(EPlayerStateValue.Lost);
					break;
				}
			}

			this.firstPlayerChoosen = true;
			playerStates.add(playerConverter
					.convertePlayerToPlayerState(fakePlayerIdObject(getSecondPlayer(player.getPlayerID()))));
		}

		playerStates.add(playerConverter.convertePlayerToPlayerState(player));

		return playerStates;

	}

	private Player fakePlayerIdObject(Player player) {

		return new Player(player.getStudentFirstName(), player.getStudentLastName(), player.getStudentID(),
				UUID.randomUUID().toString());
	}

}
