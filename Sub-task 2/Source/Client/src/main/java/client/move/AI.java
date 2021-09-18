package client.move;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.constants.GameConstants;
import client.enumerations.EMovementType;
import client.enumerations.ETerrainType;
import client.exceptions.MoveException;
import client.game.Avatar;
import client.map.Coordinate;
import client.map.Field;
import client.map.Map;
import client.map.MapValueController;

/**
 * This AI object contains all the logic that determines which move has to be
 * made, to succeed the current game phase.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class AI {

	private RouteCalculator routeCalculator;
	private MapSidesController mapSidesContoller;
	private MapValueController mapValueController;

	private HashMap<Coordinate, Field> visitedFields;
	private Set<Field> destinationFields;
	private Route currentRoute;

	private boolean nearestBorderFieldSet;
	private boolean allCastlePositionSearched;
	private boolean currentRouteCorrupted;

	private static Logger logger = LoggerFactory.getLogger(AI.class);

	/**
	 * Instantiates a new player object. The parameters must not be null.
	 * 
	 * @param gameMap
	 * @param halfMap
	 */
	public AI(Map gameMap, Map halfMap) {
		super();
		this.mapSidesContoller = new MapSidesController(gameMap, halfMap);
		this.mapValueController = new MapValueController();
		this.visitedFields = new HashMap<Coordinate, Field>();
		this.destinationFields = new LinkedHashSet<Field>();
		this.routeCalculator = new RouteCalculator(mapSidesContoller);
		this.currentRoute = new Route();

		this.nearestBorderFieldSet = false;
		this.allCastlePositionSearched = false;
		this.currentRouteCorrupted = false;
	}

	/**
	 * The logic how the AI determines which move to make next is set in this
	 * method. The decision is made based on different parameters like: Is the
	 * treasure already found (if not, is it saw and is there a route that is active
	 * to find the treasure). The treasure is found and we are now looking for the
	 * enemy castle (is the castle saw, is the enemy position saved, is there a
	 * route active leading to the opponents map closest border field, is the avatar
	 * on the opponents map side and is looking for the enemy castle).
	 * 
	 * Based on those parameters the AI will: continue the travel on the same route,
	 * calculate the route to the saw treasure/castle field or determine a set of
	 * fields where the enemy castle could be.
	 * 
	 * @param avatar
	 * @return
	 */
	public EMovementType getNextMove(Avatar avatar) {

		EMovementType move = null;

		if (!mapValueController.containsCoordinateValue(visitedFields, avatar.getCurrentPosition().getCoordinate())) {

			visitedFields.put(avatar.getCurrentPosition().getCoordinate(), avatar.getCurrentPosition());
		}

		if (currentRouteCorrupted) {
			currentRoute = new Route();
			currentRouteCorrupted = false;
		}

		if (!avatar.isTreasureCollected()) {

			if (avatar.getTreasureSaw() != null) {

				destinationFields.add(mapValueController.getCoordinateValue(mapSidesContoller.getGameMap().getHashMap(),
						avatar.getTreasureSaw().getCoordinate()));

				currentRoute = routeCalculator.getBestRoute(avatar.getCurrentPosition(), destinationFields,
						mapSidesContoller.getMyMapSide());

				destinationFields.clear();

			} else if (currentRoute.getRoute().isEmpty() || mapValueController
					.isSamePosition(avatar.getCurrentPosition(), currentRoute.getRouteDestination())) {

				setNearestUnvisitedGrassFields(avatar.getCurrentPosition(), mapSidesContoller.getMyMapSide());

				currentRoute = routeCalculator.getBestRoute(avatar.getCurrentPosition(), destinationFields,
						mapSidesContoller.getMyMapSide());

				destinationFields.clear();
			}
		} else {

			if (avatar.getEnemyCastleSaw() != null) {

				destinationFields.add(mapValueController.getCoordinateValue(mapSidesContoller.getOpponentsMapSide(),
						avatar.getEnemyCastleSaw().getCoordinate()));

				currentRoute = routeCalculator.getBestRoute(avatar.getCurrentPosition(), destinationFields,
						mapSidesContoller.getGameMap().getHashMap());

				destinationFields.clear();

			} else if (avatar.getEnemyPosition() != null && !allCastlePositionSearched) {

				try {

					setPossibleEnemyCastleArea(avatar.getEnemyPosition());

				} catch (MoveException e) {

					logger.warn("An exception has occurred but the player recovered and continued.");

					avatar.setEnemyPosition(null);

				}

				currentRoute = routeCalculator.getBestRoute(avatar.getCurrentPosition(), destinationFields,
						mapSidesContoller.getGameMap().getHashMap());

				if (destinationFields.size() <= 1) {
					allCastlePositionSearched = true;
				}

				destinationFields.clear();

			}

			if (avatar.getEnemyPosition() == null && !nearestBorderFieldSet) {

				setEnemyBorderGrassFieldsList();

				currentRoute = routeCalculator.getBestRoute(avatar.getCurrentPosition(), destinationFields,
						mapSidesContoller.getGameMap().getHashMap());

				destinationFields.clear();
				nearestBorderFieldSet = true;

			} else if (currentRoute.getRoute().isEmpty() || mapValueController
					.isSamePosition(currentRoute.getRouteDestination(), avatar.getCurrentPosition())) {

				setNearestUnvisitedGrassFields(avatar.getCurrentPosition(), mapSidesContoller.getOpponentsMapSide());

				currentRoute = routeCalculator.getBestRoute(avatar.getCurrentPosition(), destinationFields,
						mapSidesContoller.getOpponentsMapSide());

				destinationFields.clear();
			}

		}

		try {

			move = currentRouteNextMove(avatar.getCurrentPosition());

		} catch (MoveException e) {

			logger.error("Exception: " + e.getMessage());

		} catch (NullPointerException e) {

			logger.error("Exception: Route not initialized.");
		}

		return move;

	}

	/**
	 * Sets a set of unvisited grass fields that are the nearest to the current
	 * field the avatar is standing on. Those fields are used to calculate all
	 * routes to those fields and take the shortest one.
	 * 
	 * The method first looks if there are unvisited neighbour grass fields, if not
	 * looks for the neighbours neighbour grass fields and so on till an unvisited
	 * grass field is found.
	 * 
	 * @param currentPosition
	 * @param mapSide
	 */
	private void setNearestUnvisitedGrassFields(Field currentPosition, HashMap<Coordinate, Field> mapSide) {

		ArrayList<Field> fieldPool = new ArrayList<Field>();
		fieldPool.add(currentPosition);

		destinationFields.clear();

		for (int currentElement = 0; currentElement < fieldPool.size(); ++currentElement) {

			Field currentField = fieldPool.get(currentElement);

			for (Field neighbourField : mapSidesContoller.getNeighbors(currentField, mapSide)) {

				if (!mapValueController.containsCoordinateValue(visitedFields, neighbourField.getCoordinate())
						&& neighbourField.getFieldTerrain().equals(ETerrainType.Grass)) {

					destinationFields.add(neighbourField);
				} else {

					if (!fieldPool.contains(neighbourField)) {
						fieldPool.add(neighbourField);

					}
				}
			}
		}
	}

	/**
	 * Sets a set of grass fields where the enemy castle could be hidden. This is
	 * done by saving the enemy field position after the 10th round and saving all
	 * grass fields in a 2 field radius around that enemy position. (Two fields
	 * because the avatar couldn't made it further from the castle in 10 rounds even
	 * if he had only took grass fields in one direction for 10 rounds)
	 * 
	 * @param enemyPosition
	 * @throws MoveException
	 */
	private void setPossibleEnemyCastleArea(Field enemyPosition) throws MoveException {

		if (enemyPosition == null || !mapValueController
				.containsCoordinateValue(mapSidesContoller.getGameMap().getHashMap(), enemyPosition.getCoordinate()))
			throw new MoveException("Move exception: The enemy position is not existing.");

		destinationFields.clear();
		Coordinate enemyPositionCoordinate = enemyPosition.getCoordinate();
		int fieldRadius = 2;

		for (Entry<Coordinate, Field> entry : mapSidesContoller.getOpponentsMapSide().entrySet()) {

			Field field = entry.getValue();
			Coordinate coordinate = entry.getKey();

			if ((!mapValueController.containsCoordinateValue(visitedFields, coordinate))
					&& (coordinate.getX() >= enemyPositionCoordinate.getX() - fieldRadius
							&& coordinate.getX() <= enemyPositionCoordinate.getX() + fieldRadius)
					&& (coordinate.getY() >= enemyPositionCoordinate.getY() - fieldRadius
							&& coordinate.getY() <= enemyPositionCoordinate.getY() + fieldRadius)
					&& field.getFieldTerrain().equals(ETerrainType.Grass)) {

				destinationFields.add(field);
			}

		}

	}

	/**
	 * Sets a set of all grass fields on the enemy border which are used to
	 * calculate the fastest route to the opponents map side, in the case that we
	 * have fond the treasure before the 10th move and still don't know where the
	 * enemy castle approximately could be.
	 */
	private void setEnemyBorderGrassFieldsList() {

		Coordinate rectangleMap = new Coordinate(8, 0);

		if (mapValueController.containsCoordinateValue(mapSidesContoller.getGameMap().getHashMap(), rectangleMap)) {

			if (mapSidesContoller.getGameMap().getCastleField().getCoordinate().getX() < 8) {

				for (int coordinateY = 0; coordinateY < 4; ++coordinateY) {

					destinationFields.add(mapValueController.getCoordinateValue(
							mapSidesContoller.getGameMap().getHashMap(), new Coordinate(8, coordinateY)));
				}

			} else {

				for (int coordinateY = 0; coordinateY < 4; ++coordinateY) {

					destinationFields.add(mapValueController.getCoordinateValue(
							mapSidesContoller.getGameMap().getHashMap(), new Coordinate(7, coordinateY)));
				}

			}

		} else {

			if (mapSidesContoller.getGameMap().getCastleField().getCoordinate().getY() < 4) {

				for (int coordinateX = 0; coordinateX < 8; ++coordinateX) {

					destinationFields.add(mapValueController.getCoordinateValue(
							mapSidesContoller.getGameMap().getHashMap(), new Coordinate(coordinateX, 4)));
				}

			} else {

				for (int coordinateX = 0; coordinateX < 8; ++coordinateX) {

					destinationFields.add(mapValueController.getCoordinateValue(
							mapSidesContoller.getGameMap().getHashMap(), new Coordinate(coordinateX, 3)));
				}

			}

		}

	}

	/**
	 * Is taking the next unvisited list field and returning the direction in which
	 * the next move has to be done to get to that field.
	 * 
	 * @param currentPosition
	 * @return
	 * @throws MoveException
	 */
	private EMovementType currentRouteNextMove(Field currentPosition) throws MoveException {

		if (currentRoute.getRoute().isEmpty()) {

			currentRouteCorrupted = true;
			throw new MoveException("Move exception: The current route has no more unvisited fields.");
		}

		if (mapValueController.isSamePosition(currentPosition, currentRoute.getRoute().get(0))) {

			currentRoute.getRoute().remove(0);
		}

		Coordinate nextFieldCoordinate = currentRoute.getRoute().get(0).getCoordinate();

		if (nextFieldCoordinate.getX() > GameConstants.MAX_MAP_WIDTH_FIELD
				|| nextFieldCoordinate.getX() < GameConstants.MIN_MAP_WIDTH_FIELD
				|| nextFieldCoordinate.getY() > GameConstants.MAX_MAP_HEIGHT_FIELD
				|| nextFieldCoordinate.getY() < GameConstants.MIN_MAP_HEIGHT_FIELD) {

			currentRouteCorrupted = true;
			throw new MoveException("Move exception: The next route field doesn't exist.");
		}

		Field nextField = currentRoute.getRoute().get(0);

		if (!mapSidesContoller.getNeighbors(currentPosition, mapSidesContoller.getGameMap().getHashMap())
				.contains(nextField)) {

			currentRouteCorrupted = true;
			throw new MoveException(
					"Move exception: The next route field is't a neighbour to the current position field.");
		}

		if ((nextField.getCoordinate().getX() == currentPosition.getCoordinate().getX() + 1)
				&& (nextField.getCoordinate().getY() == currentPosition.getCoordinate().getY()))
			return EMovementType.Right;
		if ((nextField.getCoordinate().getX() == currentPosition.getCoordinate().getX() - 1)
				&& (nextField.getCoordinate().getY() == currentPosition.getCoordinate().getY()))
			return EMovementType.Left;
		if ((nextField.getCoordinate().getY() == currentPosition.getCoordinate().getY() + 1)
				&& (nextField.getCoordinate().getX() == currentPosition.getCoordinate().getX()))
			return EMovementType.Down;
		if ((nextField.getCoordinate().getY() == currentPosition.getCoordinate().getY() - 1)
				&& (nextField.getCoordinate().getX() == currentPosition.getCoordinate().getX()))
			return EMovementType.Up;

		return null;
	}

}
