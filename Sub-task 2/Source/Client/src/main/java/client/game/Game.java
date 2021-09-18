package client.game;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import client.exceptions.ConverterException;
import client.exceptions.MapException;
import client.map.Map;

/**
 * This game object is used by the client to save data regarding used and edited
 * by the game controller and also update data in the cli, which will be
 * presented to the user.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class Game {

	private Map fullMap;
	private Avatar avatar;
	private String gameResault;
	private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

	/**
	 * Instantiates a new player object. The parameters must not be null.
	 */
	public Game() {
		super();
		this.avatar = new Avatar();
		this.fullMap = null;
		this.gameResault = "Game ongoing";
	}

	/**
	 * Returns the converted game map send by the server.
	 * 
	 * @return (Map object from the client.map package)
	 */
	public synchronized Map getFullMap() {
		return fullMap;
	}

	/**
	 * Registers a view which will be informed if a change in the model happens.
	 * 
	 * @param cli (Cli object from the client.view package)
	 */
	public void addPropertyChangeListener(PropertyChangeListener cli) {
		changes.addPropertyChangeListener(cli);
	}

	/**
	 * Is used by the GameController to save the converted Full map send by the
	 * server in the game object. When the map has changed all registered views are
	 * informed, so that they can display the changes.
	 * 
	 * @param newFullMap (Map object from the client.map package)
	 */
	public void setFullMap(Map newFullMap) {

		if (newFullMap == null)
			throw new MapException("Map exception: The new game map can´t be null.");

		Map oldFullMap = this.fullMap;
		this.fullMap = newFullMap;

		changes.firePropertyChange("Game map update.", oldFullMap, newFullMap);
	}

	/**
	 * Is used by the GameController to save the game result. When the result has
	 * changed all registered views are informed, so that they can display the
	 * change.
	 * 
	 * @param gameResault
	 */
	public void setGameResault(String gameResault) {

		if (gameResault.length() == 0)
			throw new ConverterException("Converter exception: The game resault can´t be empty.");

		String oldGameResault = this.gameResault;
		this.gameResault = gameResault;

		changes.firePropertyChange("Game Resault update.", oldGameResault, gameResault);
	}

	/**
	 * Returns the avatar object which contains informations about the player.
	 * 
	 * @return (Avatar object from the client.game package)
	 */
	public synchronized Avatar getAvatar() {
		return avatar;
	}

}
