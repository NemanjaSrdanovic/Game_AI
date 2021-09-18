package client.game;

import client.map.Field;

/**
 * This avatar object is used by the client to save data regarding the player
 * position and provide this data to the AI for route calculation and movement
 * decisions.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class Avatar {

	private Field currentPosition;
	private Field treasureSaw;
	private Field enemyCastleSaw;
	private Field enemyPosition;
	private Field enemyCurrentPosition;
	private boolean treasureCollected;

	/**
	 * Instantiates a new player object.
	 */
	public Avatar() {
		super();
		this.currentPosition = null;
		this.treasureSaw = null;
		this.enemyCastleSaw = null;
		this.enemyPosition = null;
		this.enemyCurrentPosition = null;
		this.treasureCollected = false;
	}

	/**
	 * Returns avatars current position on the game map.
	 * 
	 * @return (Field from the client.map package)
	 */
	public Field getCurrentPosition() {
		return currentPosition;
	}

	/**
	 * Saves the current position that the avatar has on the game map.
	 * 
	 * @param currentPosition (Field from the client.map package)
	 */
	public void setCurrentPosition(Field currentPosition) {
		this.currentPosition = currentPosition;
	}

	/**
	 * Returns a field on the game map where the treasure is hidden, or null if such
	 * field was not visible during the game.
	 * 
	 * @return (Field from the client.map package)
	 */
	public Field getTreasureSaw() {
		return treasureSaw;
	}

	/**
	 * Saves the field object from the game map where the treasure is hidden.
	 * 
	 * @param treasureSaw (Field from the client.map package)
	 */
	public void setTreasureSaw(Field treasureSaw) {
		this.treasureSaw = treasureSaw;
	}

	/**
	 * Returns a field on the game map where the enemy castle is hidden, or null if
	 * such field was not visible during the game.
	 * 
	 * @return (Field from the client.map package)
	 */
	public Field getEnemyCastleSaw() {
		return enemyCastleSaw;
	}

	/**
	 * Saves the field object from the game map where the enemy castle is hidden.
	 * 
	 * @param enemyCastleSaw (Field from the client.map package)
	 */
	public void setEnemyCastleSaw(Field enemyCastleSaw) {
		this.enemyCastleSaw = enemyCastleSaw;
	}

	/**
	 * Returns if the treasure is found and collected.
	 * 
	 * @return (true if collected or false if not)
	 */
	public boolean isTreasureCollected() {
		return treasureCollected;
	}

	/**
	 * Saves the value which indicates if the treasure is collected. The default
	 * value is false.
	 * 
	 * @param treasureCollected (true or false)
	 */
	public void setTreasureCollected(boolean treasureCollected) {
		this.treasureCollected = treasureCollected;
	}

	/**
	 * Returns a field on the game map where the enemy avatar was first seen after
	 * the 10th game round, or null if such field is still not visible.
	 * 
	 * @return (Field from the client.map package)
	 */
	public Field getEnemyPosition() {
		return enemyPosition;
	}

	/**
	 * Saves the field object where the enemy avatar was first seen after the 10th
	 * game round.
	 * 
	 * @param enemyPosition (Field from the client.map package)
	 */
	public void setEnemyPosition(Field enemyPosition) {
		this.enemyPosition = enemyPosition;
	}

	/**
	 * Returns the enemy player position (random as correct)
	 * 
	 * @return
	 */
	public Field getEnemyCurrentPosition() {
		return enemyCurrentPosition;
	}

	/**
	 * Sets the enemy player position based on the converted game map.
	 * 
	 * @param enemyCurrentPosition
	 */
	public void setEnemyCurrentPosition(Field enemyCurrentPosition) {
		this.enemyCurrentPosition = enemyCurrentPosition;
	}

}
