package client.view;

import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map.Entry;

import client.constants.GameConstants;
import client.game.Game;
import client.map.Coordinate;
import client.map.Field;
import client.map.Map;
import client.map.MapValueController;

/**
 * This cli object is used by the game controller to visualise the map and its
 * known properties on the computers (clients) of the two human players ( e.g.
 * opposing Game pieces , castles, fields (and their respective terrain) etc.)
 * 
 * @author Nemanja Srdanovic
 *
 */
public class Cli {

	private String grasField = "[---G---]";
	private String mountainField = "[---M---]";
	private String waterField = "[---W---]";
	private String avatarField = "[---MA--]";
	private String myCasteField = "[---C---]";
	private String myTreasureField = "[---$---]";
	private String bothAvatarField = "[-MA-EA-]";
	private String enemyAvatarField = "[---EA--]";
	private String enemyCasteField = "[---EC--]";
	private String treasureStatus = "Not found";

	private MapValueController mapValueController;
	private Game game;

	/**
	 * Instantiates a new network object.
	 */
	public Cli(Game game) {
		super();
		this.mapValueController = new MapValueController();
		this.game = game;
		this.game.addPropertyChangeListener(listener);
	}

	/**
	 * Receives the updated object when a change has occur and triggers the display
	 * method.
	 */
	final PropertyChangeListener listener = event -> {
		Object obj = event.getNewValue();

		if (obj instanceof Map) {
			Map map = (Map) obj;
			printMap(map);
		} else if (obj instanceof String) {
			String gameState = (String) obj;
			printFinalState(gameState);
		}

	};

	/**
	 * This method is used to display the final game result to the user.
	 * 
	 * @param state
	 */
	private void printFinalState(String state) {

		System.out.println("[--" + state + "--]");
	}

	/**
	 * Displays the map legend including the objects positions on the map
	 */
	private void printMapLegend() {

		System.out.println("*************************************************************************************");

		System.out.println("Map Legend");

		String playerAvatar = "Your avatar:  " + avatarField;
		String enemyAvatar = "Enemy avatar: " + enemyAvatarField;
		String playerCastle = "Your castle:  " + myCasteField;
		String enemyCastle = "Enemy castle: " + enemyCasteField;
		String playerTreasure = "Your treasure:" + myTreasureField;

		String playerPosition = "unknown";
		String enemyPlayerPosition = "random";
		String playerCastlePosition = game.getFullMap().getCastleField().getCoordinate().toString();
		String enemyCastlePosition = "unknown";

		if (game.getAvatar().getEnemyCastleSaw() != null) {

			enemyCastlePosition = game.getAvatar().getEnemyCastleSaw().getCoordinate().toString();

		}

		if (game.getAvatar().getEnemyPosition() != null) {

			enemyPlayerPosition = game.getAvatar().getEnemyCurrentPosition().getCoordinate().toString();

		}

		if (game.getAvatar().isTreasureCollected()) {
			treasureStatus = "found";

		}

		if (game.getAvatar().getCurrentPosition() != null) {

			playerPosition = game.getAvatar().getCurrentPosition().getCoordinate().toString();
		}

		String firstRow = playerAvatar + "[Position: " + playerPosition + "]   |*| " + enemyAvatar + "[Position: "
				+ enemyPlayerPosition + "]";

		String secondRow = playerCastle + "[Position: " + playerCastlePosition + "]   |*| " + enemyCastle
				+ "[Position: " + enemyCastlePosition + "]";

		String thirdRow = playerTreasure + "[Status: " + treasureStatus + "]";

		System.out.println(firstRow);
		System.out.println(secondRow);
		System.out.println(thirdRow);
		System.out.println("\n");
	}

	/**
	 * This method is used by the cli to determine which shape the map has and
	 * accordingly advise the print method how to print the map. Also the method
	 * uses another method to convert the fields from the map object to a
	 * appropriate print format.
	 * 
	 * 
	 * @param map (Map object from client.map package)
	 */
	public void printMap(Map map) {

		HashMap<Coordinate, String> printHashMap = converteHashMap(map.getHashMap());

		Coordinate rectangleMap = new Coordinate(8, 0);
		Coordinate squareMap = new Coordinate(0, 4);

		if (mapValueController.containsCoordinateValue(printHashMap, rectangleMap)) {
			printMapShape(printHashMap, GameConstants.HALF_MAP_HEIGHT, GameConstants.HALF_MAP_WIDTH * 2);
		} else if (mapValueController.containsCoordinateValue(printHashMap, squareMap)) {
			printMapShape(printHashMap, GameConstants.HALF_MAP_HEIGHT * 2, GameConstants.HALF_MAP_WIDTH);
		} else {
			printMapShape(printHashMap, GameConstants.HALF_MAP_HEIGHT, GameConstants.HALF_MAP_WIDTH);
		}

	}

	/**
	 * This method is used by the cli to display the map and its respective terrain
	 * to the human player based on the maps shape.
	 * 
	 * @param hashMap        (collection of the field objects in a print appropriate
	 *                       format)
	 * @param mapHeightInput (the size of the maps vertical borders)
	 * @param mapWidtInput   (the size of the maps horizontal borders)
	 */
	private void printMapShape(HashMap<Coordinate, String> hashMap, int mapHeightInput, int mapWidtInput) {

		printMapLegend();

		String mapRow = "";
		String numerationRow = "    ";
		Coordinate key = new Coordinate(0, 0);

		int numerationNum = 0;
		while (numerationNum != mapWidtInput) {

			if (numerationNum < 10) {
				numerationRow += "  |_" + numerationNum + "_|  ";
			} else {
				numerationRow += "  |_" + numerationNum + "_| ";
			}

			++numerationNum;
		}

		System.out.println(numerationRow);

		for (int mapHeight = 0; mapHeight < mapHeightInput; ++mapHeight) {
			for (int mapWidth = 0; mapWidth < mapWidtInput; ++mapWidth) {

				key.setY(mapHeight);
				key.setX(mapWidth);
				String field = mapValueController.getCoordinateValue(hashMap, key);
				mapRow += field;

			}
			System.out.println("|" + mapHeight + "| " + mapRow);

			mapRow = "";
		}

		System.out.println("\n");
		System.out.println("*************************************************************************************");
	}

	/**
	 * The method iterates through the field collection of the map object and uses
	 * another method to convert the field objects to a appropriate print format.
	 * 
	 * @param hashMap (collection of field objects from the map object)
	 * @return (collection of the field objects in a print appropriate format)
	 */
	private HashMap<Coordinate, String> converteHashMap(HashMap<Coordinate, Field> hashMap) {

		HashMap<Coordinate, String> convertedHashMap = new HashMap<Coordinate, String>();

		for (Entry<Coordinate, Field> entry : hashMap.entrySet()) {

			Field field = entry.getValue();

			convertedHashMap.put(entry.getKey(), fieldToStringConverter(field));
		}

		return convertedHashMap;
	}

	/**
	 * This method is used to convert the fields from the map object to a
	 * appropriate print format.
	 * 
	 * @param field (Field object from the client.map package)
	 * @return
	 */
	private String fieldToStringConverter(Field field) {

		String fieldString = null;

		switch (field.getFieldTerrain()) {

		case Water:
			return fieldString = waterField;
		case Mountain:
			fieldString = mountainField;
			break;
		default:
			fieldString = grasField;

		}

		switch (field.getFieldAvatarContent()) {

		case BothAvatarPosition:
			fieldString = bothAvatarField;
			break;
		case EnemyAvatarPosition:
			fieldString = enemyAvatarField;
			break;
		case MyAvatarPosition:
			fieldString = avatarField;
			break;
		}

		switch (field.getFieldCastleContent()) {

		case MyCastlePresent:
			fieldString = myCasteField;
			break;
		case EnemyCastlePresent:
			fieldString = enemyCasteField;
			break;
		}

		switch (field.getFieldTreasureContent()) {

		case MyTreasurePresent:
			fieldString = myTreasureField;
			break;
		}

		return fieldString;
	}

}
