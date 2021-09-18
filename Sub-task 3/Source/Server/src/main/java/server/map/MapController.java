package server.map;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;

import MessagesBase.HalfMap;
import MessagesGameState.FullMap;
import server.converter.MapConverter;
import server.player.Player;
import server.rules.GameConstants;

/**
 * 
 * @author Nemanja Srdanovic
 *
 */
public class MapController {

	private Map map;
	private HashMap<String, PlayerMap> halfMaps;
	private MapCombiner mapCombiner;
	private MapConverter mapConverter;

	public MapController() {

		this.map = null;
		this.halfMaps = new HashMap<String, PlayerMap>();
		this.mapCombiner = new MapCombiner();
		this.mapConverter = new MapConverter();

	}

	public Optional<FullMap> getOptionalFullMap(Player player, int gameRound) {

		FullMap fullMap = new FullMap();

		if (halfMaps.get(player.getPlayerID()) != null) {
			fullMap = mapConverter.converteMapToFullMap(getMapDuplicate(getMap()), player, gameRound);
		}

		return Optional.of(fullMap);

	}

	private Map getMap() {
		return map;
	}

	private void setMap(Map map) {
		this.map = map;
	}

	public HashMap<String, PlayerMap> getHalfMaps() {
		return halfMaps;
	}

	private Map getMapDuplicate(Map map) {

		HashMap<Coordinate, Field> hashMap = new HashMap<>();

		for (Entry<Coordinate, Field> entry : map.getHashMap().entrySet()) {

			Coordinate coordinate = new Coordinate(entry.getKey().getX(), entry.getKey().getY());
			Field field = new Field(coordinate, entry.getValue().getFieldTerrain(),
					entry.getValue().getFieldCastleContent(), entry.getValue().getFieldAvatarContent(),
					entry.getValue().getFieldTreasureContent());

			hashMap.put(coordinate, field);
		}

		return new Map(hashMap);
	}

	public void setHalfMap(Player player, HalfMap halfMap) {

		Map convertedMap = mapConverter.converteHalfMapToMap(halfMap, player);

		halfMaps.put(player.getPlayerID(), new PlayerMap(player, convertedMap));

		if (halfMaps.size() < GameConstants.HALF_MAPS_NUM_TO_COMPLETE_MAP) {

			setMap(convertedMap);

		} else if (halfMaps.size() == GameConstants.HALF_MAPS_NUM_TO_COMPLETE_MAP) {

			setMap(mapCombiner.combineHalfMaps(this.halfMaps));

		}

	}

}
