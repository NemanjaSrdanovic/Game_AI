package client.exceptions;

/**
 * This MoveException object is used to indicate that an error has occurred when
 * the objects form the client.move package were used.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class MoveException extends Exception {

	/**
	 * Instantiates a MoveException player object. The parameters must not be null.
	 * 
	 * @param message
	 * @param cause
	 */
	public MoveException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new MoveException object. The parameters must not be null.
	 * 
	 * @param message
	 */
	public MoveException(String message) {
		super(message);

		// TODO Auto-generated constructor stub
	}

}
