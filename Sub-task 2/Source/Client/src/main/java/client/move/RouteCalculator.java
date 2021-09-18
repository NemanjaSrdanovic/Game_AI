package client.move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Set;

import client.enumerations.ETerrainType;
import client.map.Coordinate;
import client.map.Field;

/**
 * This RuteCalculator object is used by the AI to calculate the best route from
 * a given position to a set of possible destinations, and to return the fastest
 * route.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class RouteCalculator {

	private HashMap<Field, Integer> costToDestinationField;
	private HashMap<Field, Field> fieldToGetToDestination;
	private Set<Field> fieldsWithUnknownMinRoute;
	private Set<Field> fieldsWithKnownMinRoute;
	private MapSidesController mapSidesController;

	/**
	 * Instantiates a new player object. The parameters must not be null.
	 * 
	 * @param mapSidesController (MSC object from the client.move package)
	 */
	public RouteCalculator(MapSidesController mapSidesController) {
		super();
		this.mapSidesController = mapSidesController;
		this.costToDestinationField = new HashMap<Field, Integer>();
		this.fieldToGetToDestination = new HashMap<Field, Field>();
		this.fieldsWithUnknownMinRoute = new HashSet<Field>();
		this.fieldsWithKnownMinRoute = new HashSet<Field>();

	}

	/**
	 * Is called by the AI object and hand over a position on the map and a set of
	 * destination fields. The method than hands the position and every individual
	 * destination from the set, to another method which returns the route for those
	 * two positions. All received routes are compared to determine the fastest one,
	 * and than this route is returned to the AI.
	 * 
	 * @param fieldPosition
	 * @param targetFields
	 * @param map
	 * @return
	 */
	public Route getBestRoute(Field fieldPosition, Set<Field> targetFields, HashMap<Coordinate, Field> map) {

		int minRouteCost = 1000;
		Route bestRoute = new Route();

		for (Field fieldDestination : targetFields) {

			Route route = getRoute(fieldPosition, fieldDestination, map);

			int routeCost = getRouteCost(fieldPosition, route);

			if (minRouteCost > routeCost) {

				bestRoute = route;
				minRouteCost = routeCost;
			}
		}

		return bestRoute;

	}

	/**
	 * Activates a method which calculates all routes from the hand over position
	 * field, to every other position on the map. That results in a HashMap which
	 * contains all fields and for every one a field over which that field can be
	 * accessed the fastest. The hand over destination field is found in that
	 * HashMap, his key field is saved and becomes the new destination for which we
	 * again find the key field and save it. In that way we loop through every
	 * field, till we arrive at the position field, for which every route has been
	 * calculated.
	 * 
	 * @param position
	 * @param destination
	 * @param map
	 * @return
	 */
	private Route getRoute(Field position, Field destination, HashMap<Coordinate, Field> map) {

		calculateAllRoutes(position, map);

		ArrayList<Field> fieldRoute = new ArrayList<Field>();
		Field routeField = destination;

		do {

			fieldRoute.add(routeField);
			routeField = fieldToGetToDestination.get(routeField);

		} while (routeField != null);

		Collections.reverse(fieldRoute);
		return new Route(position, destination, fieldRoute);
	}

	/**
	 * Is activated to find the fastest route from the hand over positionField to
	 * every other field on the hand over map. For this to do, the position field is
	 * added to a set of fields, for which the shortest route to the position is
	 * still unknown and the route cost is set to 0 (cause its already the field for
	 * which the route has to be calculated).
	 * 
	 * For every other field the route is set to a very high number cause we still
	 * haven't found the route to them.
	 * 
	 * Than we loop through that set, always taking the field with the shortest
	 * route. Since that is already the shortest route to the field we save that
	 * field to the fieldsWithKnownMinRoute list and examine that fields neighbours.
	 * 
	 * We compare all route costs with the newly calculated through another
	 * neighbour field and set the route cost of that field to the new one, and the
	 * associated field to that neighbour field, if that route is shorter than the
	 * current one.
	 * 
	 * This is done for all fields an their neighbours (if the shortest route for
	 * the neighbour is not already found) which results in two HashMaps from which
	 * one contain two fields (map field and that one field which is the shortest
	 * route to him) and the second field and cost (map field, and the cost to come
	 * to the field).
	 * 
	 * @param positionField
	 * @param map
	 */
	private void calculateAllRoutes(Field positionField, HashMap<Coordinate, Field> map) {

		fieldsWithUnknownMinRoute.clear();
		costToDestinationField.clear();
		fieldsWithKnownMinRoute.clear();
		fieldToGetToDestination.clear();

		fieldsWithUnknownMinRoute.add(positionField);
		costToDestinationField.put(positionField, 0);
		setRestMoveCost(positionField, map);

		while (!fieldsWithUnknownMinRoute.isEmpty()) {

			Field currentField = getSmalestValue(fieldsWithUnknownMinRoute);
			fieldsWithKnownMinRoute.add(currentField);
			fieldsWithUnknownMinRoute.remove(currentField);

			for (Field neighbour : mapSidesController.getNeighbors(currentField, map)) {
				if (!fieldsWithKnownMinRoute.contains(neighbour)) {

					fieldsWithUnknownMinRoute.add(neighbour);
					int routeCostOverNeighbour = costToDestinationField.get(currentField)
							+ getTravelCost(currentField, neighbour);

					if (routeCostOverNeighbour < costToDestinationField.get(neighbour)) {

						costToDestinationField.put(neighbour, routeCostOverNeighbour);
						fieldToGetToDestination.put(neighbour, currentField);

					}
				}
			}

		}

	}

	/**
	 * Takes all fields from the map (except the startField) and sets the costs to
	 * come to that field to a very high number. This number will eventually be
	 * recalculated in the route finding process to that start field.
	 * 
	 * @param startField
	 * @param map
	 */
	private void setRestMoveCost(Field startField, HashMap<Coordinate, Field> map) {

		for (Entry<Coordinate, Field> entry : map.entrySet()) {

			if (!entry.getValue().equals(startField)) {
				costToDestinationField.put(entry.getValue(), 10000);
			}
		}
	}

	/**
	 * Return the field with the smallest route cost from the set of fields with
	 * unknown minimal route to the start field.
	 * 
	 * @param fieldsWithUnknownMinRoute
	 * @return
	 */
	private Field getSmalestValue(Set<Field> fieldsWithUnknownMinRoute) {

		int smalestValueCost = 10000;
		Field smalestValue = null;

		for (Field field : fieldsWithUnknownMinRoute) {

			if (smalestValueCost > costToDestinationField.get(field)) {

				smalestValue = field;
				smalestValueCost = costToDestinationField.get(field);
			}
		}

		return smalestValue;
	}

	/**
	 * Returns the travel cost from a start field to a destination field based on
	 * the two fields terrain. The specification for the travel cost is taken over
	 * from the "game idea" document.
	 * 
	 * @param fromField
	 * @param toField
	 * @return
	 */
	private int getTravelCost(Field fromField, Field toField) {

		switch (toField.getFieldTerrain()) {

		case Grass:
			if (fromField.getFieldTerrain().equals(ETerrainType.Grass))
				return 2;
			if (fromField.getFieldTerrain().equals(ETerrainType.Mountain))
				return 3;
			if (fromField.getFieldTerrain().equals(ETerrainType.Water))
				return 10000;
		case Mountain:
			if (fromField.getFieldTerrain().equals(ETerrainType.Grass))
				return 3;
			if (fromField.getFieldTerrain().equals(ETerrainType.Mountain))
				return 4;
			if (fromField.getFieldTerrain().equals(ETerrainType.Water))
				return 10000;
		case Water:
			return 10000;
		}

		return 1000;

	}

	/**
	 * Returns the entire cost for the whole route, by calculating the cost for the
	 * travel between all field. This method is used to determine which route has
	 * the smallest costs and should be send to the AI.
	 * 
	 * @param inputPosition
	 * @param route
	 * @return
	 */
	private int getRouteCost(Field inputPosition, Route route) {

		int cost = 0;
		Field currentPosition = inputPosition;

		ListIterator<Field> it = route.getRoute().listIterator();

		while (it.hasNext()) {

			Field nextField = it.next();

			cost += getTravelCost(currentPosition, nextField);
			currentPosition = nextField;
		}

		return cost;
	}

}
