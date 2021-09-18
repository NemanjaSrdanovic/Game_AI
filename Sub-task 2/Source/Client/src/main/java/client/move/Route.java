package client.move;

import java.util.ArrayList;

import client.map.Field;

/**
 * This route object is used to save the fields and the order in which fields
 * have to be accessed, to arrive from a start field to an destination field. It
 * includes a number of function which helps the AI determine which instruction
 * has to be done next.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class Route {

	private ArrayList<Field> route;
	private Field startingPosition;
	private Field routeDestination;

	/**
	 * Instantiates a new map object. The parameters must not be null.
	 * 
	 * @param position
	 * @param destination
	 * @param route
	 */
	public Route(Field position, Field destination, ArrayList<Field> route) {

		this.route = route;
		this.startingPosition = position;
		this.routeDestination = destination;

	}

	/**
	 * Instantiates a new map object.
	 */
	public Route() {
		this.route = new ArrayList<Field>();
	}

	/**
	 * Returns if the hand over field is contained in the route for which this
	 * method has been called.
	 * 
	 * @param inputField
	 * @return
	 */
	public boolean containtsField(Field inputField) {

		int coordinateX = inputField.getCoordinate().getX();
		int coordinateY = inputField.getCoordinate().getY();

		for (Field field : route) {

			if ((field.getCoordinate().getX() == coordinateX) && (field.getCoordinate().getY() == coordinateY)) {
				return true;
			}
		}

		return false;

	}

	/**
	 * Returns the list containing all fields in execution order.
	 * 
	 * @return
	 */
	public ArrayList<Field> getRoute() {
		return route;
	}

	/**
	 * Saves the list containing all fields in execution order.
	 * 
	 * @param route
	 */
	public void setRoute(ArrayList<Field> route) {
		this.route = route;
	}

	/**
	 * Returns the field which was set as the starting position for that route.
	 * 
	 * @return
	 */
	public Field getStartingPosition() {
		return startingPosition;
	}

	/**
	 * Returns the field which was set as the end position for that route.
	 * 
	 * @return
	 */
	public Field getRouteDestination() {
		return routeDestination;
	}

	/**
	 * Implements a working toString method for this object to ease debugging.
	 *
	 */
	@Override
	public String toString() {
		return "Route [route=" + route + "]";
	}

}
