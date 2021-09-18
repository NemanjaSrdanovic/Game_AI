package server.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import server.enumerations.ECastleValue;
import server.enumerations.ETerrainType;
import server.rules.GameConstants;

/**
 * 
 * @author Nemanja Srdanovic
 *
 */
public class MapCombiner {

	public MapCombiner() {
	}

	public Map combineHalfMaps(HashMap<String, PlayerMap> halfMaps) {

		Map tmpMap = null;
		int optionNum = new Random().nextInt(4);

		PlayerMap firstPlayerMap = (PlayerMap) halfMaps.values().toArray()[0];
		PlayerMap secondPlayerMap = (PlayerMap) halfMaps.values().toArray()[1];

		if (optionNum == 0) {

			tmpMap = combineToOneMap(firstPlayerMap, secondPlayerMap, GameConstants.SQUARE_MAP_WIDTH_EXTEND,
					GameConstants.SQUARE_MAP_HEIGHT_EXTEND);

		} else if (optionNum == 1) {

			tmpMap = combineToOneMap(secondPlayerMap, firstPlayerMap, GameConstants.SQUARE_MAP_WIDTH_EXTEND,
					GameConstants.SQUARE_MAP_HEIGHT_EXTEND);

		} else if (optionNum == 2) {

			tmpMap = combineToOneMap(firstPlayerMap, secondPlayerMap, GameConstants.RECTANGLE_MAP_WIDTH_EXTEND,
					GameConstants.RECTANGLE_MAP_HEIGHT_EXTEND);

		} else if (optionNum == 3) {

			tmpMap = combineToOneMap(secondPlayerMap, firstPlayerMap, GameConstants.RECTANGLE_MAP_WIDTH_EXTEND,
					GameConstants.RECTANGLE_MAP_HEIGHT_EXTEND);

		}

		return tmpMap;
	}

	private void setTreasureField(PlayerMap player) {

		List<Field> mapFields = new ArrayList<Field>(player.getMap().getHashMap().values());

		boolean treasureSet = false;

		while (!treasureSet) {

			int randomField = new Random().nextInt(mapFields.size());

			if (mapFields.get(randomField).getFieldTerrain().equals(ETerrainType.Grass)
					&& !mapFields.get(randomField).getFieldCastleContent().equals(ECastleValue.MyCastlePresent)) {

				player.getPlayer().setTreasurePosition(mapFields.get(randomField));
				treasureSet = true;

			}
		}
	}

	private Map combineToOneMap(PlayerMap firstPlayerMap, PlayerMap secondPlayerMap, int extendWidth,
			int extendHeight) {

		setTreasureField(firstPlayerMap);
		setTreasureField(secondPlayerMap);

		HashMap<Coordinate, Field> fullMapFields = firstPlayerMap.getMap().getHashMap();

		for (Entry<Coordinate, Field> entry : secondPlayerMap.getMap().getHashMap().entrySet()) {

			Field field = entry.getValue();

			field.getCoordinate().setX(field.getCoordinate().getX() + extendWidth);
			field.getCoordinate().setY(field.getCoordinate().getY() + extendHeight);

			if (field.getFieldCastleContent().equals(ECastleValue.MyCastlePresent)) {

				secondPlayerMap.getPlayer().setCastePosition(field);
				secondPlayerMap.getPlayer().setCurrentPosition(field);
			}

			fullMapFields.put(field.getCoordinate(), field);

		}

		return new Map(fullMapFields);
	}

}
