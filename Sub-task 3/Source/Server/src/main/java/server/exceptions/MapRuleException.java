package server.exceptions;

import server.player.Player;

public class MapRuleException extends RuleException {

	private static final long serialVersionUID = 1L;

	public MapRuleException(Player player, String errorName, String errorMessage) {
		super(player, errorName, errorMessage);

	}

}
