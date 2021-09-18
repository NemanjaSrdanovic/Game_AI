package server.map;

import server.exceptions.MapException;
import server.rules.GameConstants;

/**
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

}
