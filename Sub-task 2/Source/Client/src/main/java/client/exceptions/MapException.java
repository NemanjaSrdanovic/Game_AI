package client.exceptions;

/**
 * This MapException object is used to indicate that an error has occurred when
 * the objects form the client.map package were used.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class MapException extends RuntimeException {

	/**
	 * Instantiates a new MapException object. The parameters must not be null.
	 * 
	 * @param message
	 * @param cause
	 */
	public MapException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new MapException object. The parameters must not be null.
	 * 
	 * @param message
	 */
	public MapException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
