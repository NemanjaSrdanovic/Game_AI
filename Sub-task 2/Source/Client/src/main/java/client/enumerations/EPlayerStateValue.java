package client.enumerations;

/**
 * Enum used by the client to indicate if a client has won/lost or should send
 * new commands (or not).
 * 
 * @author Nemanja Srdanovic
 *
 */
public enum EPlayerStateValue {
	Lost, ShouldActNext, ShouldWait, Won;

}
