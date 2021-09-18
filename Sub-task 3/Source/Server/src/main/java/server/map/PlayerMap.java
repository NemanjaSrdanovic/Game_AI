package server.map;

import server.player.Player;

/**
 * 
 * @author Nemanja Srdanovic
 *
 */
public class PlayerMap {

	private Player player;
	private Map map;

	public PlayerMap(Player player, Map map) {

		this.player = player;
		this.map = map;

	}

	public Player getPlayer() {
		return player;
	}

	public Map getMap() {
		return map;
	}

}
