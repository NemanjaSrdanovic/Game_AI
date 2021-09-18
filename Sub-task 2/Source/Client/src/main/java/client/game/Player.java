package client.game;

import client.exceptions.StartParameterException;

/**
 * This player object is used by the client to save data regarding the player
 * and provide this data to the game controller for registering the player to a
 * game.
 *
 * @author Nemanja Srdanovic
 *
 */
public class Player {

	private String name;
	private String surname;
	private String studentID;
	private static String playerGameID;

	/**
	 * Instantiates a new player object. The parameters must not be null.
	 * 
	 * @param name
	 * @param surname
	 * @param matriculationID
	 * @throws StartParameterException
	 */
	public Player(String name, String surname, String matriculationID) throws StartParameterException {

		if (name.length() == 0 || surname.length() == 0 || matriculationID.length() == 0) {
			throw new StartParameterException("The player credentials must be filled out.");
		}

		this.name = name;
		this.surname = surname;
		this.studentID = matriculationID;
	}

	/**
	 * Get the first name.
	 * 
	 * @return (the first name which was used during the player registration phase.)
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the last name.
	 * 
	 * @return (the last name which was used during the player registration phase.)
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * Get the student id.
	 * 
	 * @return (the student id which was used during the player registration phase.)
	 */
	public String getStudentID() {
		return studentID;
	}

	/**
	 * Get unique player id sent by the server, after registration.
	 * 
	 * @return (String)
	 */
	public String getPlayerGameID() {
		return playerGameID;
	}

	/**
	 * Sets the unique player id sent by the server, after registration.
	 * 
	 * @param playerGameID
	 */
	public void setPlayerGameID(String playerGameID) {
		this.playerGameID = playerGameID;
	}

	/**
	 * Implements a working toString method for this object.
	 */
	@Override
	public String toString() {
		return "Player [Name: " + name + ", Surname: " + surname + ", StudentID: " + studentID + "]";
	}

}
