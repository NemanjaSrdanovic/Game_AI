package server.converter;

import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.PlayerState;
import server.exceptions.ConverterException;
import server.player.Player;

public class PlayerConverter {

	EnumConverter enumConverter;

	/**
	 * Instantiates a new player converter object.
	 */
	public PlayerConverter() {
		this.enumConverter = new EnumConverter();
	}

	/**
	 * This method is used to convert the server player object into a network
	 * protocol convenient object.
	 * 
	 * @param player
	 * @return
	 */
	public PlayerState convertePlayerToPlayerState(Player player) {

		if (player == null)
			throw new ConverterException("Converter exception: The player can´t be null.");

		PlayerState playerState = new PlayerState(player.getStudentFirstName(), player.getStudentLastName(),
				player.getStudentID(),
				enumConverter.EPlayerStateValue_To_NetworkPlayerGameState(player.getCurrentState()),
				new UniquePlayerIdentifier(player.getPlayerID()), player.isHasCollectedTreasure());

		return playerState;
	}

}
