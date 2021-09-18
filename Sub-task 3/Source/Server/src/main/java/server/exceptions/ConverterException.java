package server.exceptions;

/**
 * This ConverterException object is used to indicate that an error has occurred
 * when the objects form the server.converter package were used.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class ConverterException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new ConverterException object. The parameters must not be
	 * null.
	 * 
	 * @param message
	 * @param cause
	 */
	public ConverterException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new ConverterException object. The parameters must not be
	 * null.
	 * 
	 * @param message
	 */
	public ConverterException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
