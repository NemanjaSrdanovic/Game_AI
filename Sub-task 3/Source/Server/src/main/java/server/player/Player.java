package server.player;

import server.enumerations.EPlayerStateValue;
import server.map.Field;

/**
 * 
 * @author Nemanja Srdanovic
 *
 */
public class Player {

	private String studentFirstName;
	private String studentLastName;
	private String studentID;
	private String playerID;
	private EPlayerStateValue currentState;
	private boolean hasCollectedTreasure;
	private Field treasurePosition;
	private Field currentPosition;
	private Field castePosition;

	public Player() {

		this.currentState = EPlayerStateValue.ShouldWait;
		this.hasCollectedTreasure = false;
	}

	public Player(String studentFirstName, String studentLastName, String studentID, String playerID) {
		super();
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
		this.studentID = studentID;
		this.playerID = playerID;
		this.hasCollectedTreasure = false;
		this.currentState = EPlayerStateValue.ShouldWait;
	}

	public String getStudentFirstName() {
		return studentFirstName;
	}

	public void setStudentFirstName(String studentFirstName) {
		this.studentFirstName = studentFirstName;
	}

	public String getStudentLastName() {
		return studentLastName;
	}

	public void setStudentLastName(String studentLastName) {
		this.studentLastName = studentLastName;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public String getPlayerID() {
		return playerID;
	}

	public boolean isHasCollectedTreasure() {
		return hasCollectedTreasure;
	}

	public void setHasCollectedTreasure(boolean hasCollectedTreasure) {
		this.hasCollectedTreasure = hasCollectedTreasure;
	}

	public Field getCastePosition() {
		return castePosition;
	}

	public void setCastePosition(Field castePosition) {
		this.castePosition = castePosition;
	}

	public EPlayerStateValue getCurrentState() {
		return currentState;
	}

	public void setCurrentState(EPlayerStateValue currentState) {
		this.currentState = currentState;
	}

	public Field getTreasurePosition() {
		return treasurePosition;
	}

	public void setTreasurePosition(Field treasurePosition) {
		this.treasurePosition = treasurePosition;
	}

	public Field getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(Field currentPosition) {
		this.currentPosition = currentPosition;
	}

}
