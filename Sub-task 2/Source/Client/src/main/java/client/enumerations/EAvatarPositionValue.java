package client.enumerations;

/**
 * Enum used by the client to indicate if an avatar is positioned on a related
 * map field or not. Note, initially (during the first couple of move command
 * exchanges) the position of the enemy avatar will be faked (otherwise finding
 * the enemy castle is too easy).
 * 
 * @author Nemanja Srdanovic
 *
 */
public enum EAvatarPositionValue {
	BothAvatarPosition, EnemyAvatarPosition, MyAvatarPosition, NoAvatarPresent;
}
