package server.exceptions;

import server.player.Player;

public class RuleException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String errorName;
	private final Player player;

	public RuleException(Player player, String errorName, String errorMessage) {
		super(errorMessage);
		this.errorName = errorName;
		this.player = player;

	}

	public String getErrorName() {
		return errorName;
	}

	public Player getPlayer() {
		return player;
	}

}
