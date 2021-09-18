package server.exceptions;

public class GameRuleException extends RuleException {

	private static final long serialVersionUID = 1L;

	public GameRuleException(String errorName, String errorMessage) {
		super(null, errorName, errorMessage);
	}

}
