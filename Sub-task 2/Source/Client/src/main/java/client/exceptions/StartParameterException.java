package client.exceptions;

/**
 * This StartParameterException object is used to indicate that an error has
 * occurred when the objects form the client.game package were used.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class StartParameterException extends Exception {

	/**
	 * Instantiates a new StartParameterException object. The parameters must not be
	 * null.
	 * 
	 * @param message
	 * @param cause
	 */
	public StartParameterException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new StartParameterException object. The parameters must not be
	 * null.
	 * 
	 * @param message
	 */
	public StartParameterException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
