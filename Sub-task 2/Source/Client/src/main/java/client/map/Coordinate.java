package client.map;

import client.constants.GameConstants;
import client.exceptions.MapException;

/**
 * This coordinate object is used by the client to add a parameter to the field
 * object by which every object can be distinguished.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class Coordinate {

	private int x;
	private int y;

	/**
	 * Instantiates a new coordinate object.
	 * 
	 * @param x (is the field position on the maps horizontal side)
	 * @param y (is the field position on the maps vertical side)
	 */
	public Coordinate(int x, int y) {

		this.x = x;
		this.y = y;
	}

	/**
	 * Get the horizontal coordinate.
	 * 
	 * @return ( a number between 0 and 15)
	 */
	public int getX() {
		return x;
	}

	/**
	 * Set the horizontal coordinate.
	 * 
	 * @param x (a number between 0 and 15)
	 */
	public void setX(int x) {

		if (x < GameConstants.MIN_MAP_WIDTH_FIELD || x > GameConstants.MAX_MAP_WIDTH_FIELD)
			throw new MapException("Map Exception: The horizontal coordinate must be between (inclusive) 0 and 15.");

		this.x = x;
	}

	/**
	 * Get the vertical coordinate.
	 * 
	 * @return (a number between 0 and 7)
	 */
	public int getY() {
		return y;
	}

	/**
	 * Set the vertical coordinate.
	 * 
	 * @param y (a number between 0 and 7)
	 */
	public void setY(int y) {

		if (y < GameConstants.MIN_MAP_HEIGHT_FIELD || y > GameConstants.MAX_MAP_HEIGHT_FIELD)
			throw new MapException("Map Exception: The vertical coordinate must be between (inclusive) 0 and 7.");

		this.y = y;
	}

	/**
	 * Implements a working toString method for this object to ease debugging.
	 */
	@Override
	public String toString() {
		return "[x=" + x + ", y=" + y + "]";
	}

}
