package server.rules;

import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import server.player.Player;

/**
 * 
 * @author Nemanja Srdanovic
 *
 */
public interface IRule {

	public void newGame();

	public void registerPlayer(PlayerRegistration playerRegistration, String gameID);

	public void receiveHalfMap(Player player, HalfMap halfMap, String gameID);

	public void returnGameState(String gameID, String playerID);

}
