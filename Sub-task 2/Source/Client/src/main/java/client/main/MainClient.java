package client.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.exceptions.StartParameterException;
import client.game.GameController;
import client.game.Player;

public class MainClient {

	private static Logger logger = LoggerFactory.getLogger(MainClient.class);

	public static void main(String[] args) {

		/*
		 * IMPORTANT: Parsing/Handling of starting parameters. args[0] = Game Mode, you
		 * can use this to know that you code is running on the evaluation server (if
		 * this is the case args[0] = TR). If this is the case only a command line
		 * interface must be displayed. Also, no JavaFX and Swing UI component and
		 * classes must be used/executed by your Client in any way IF args[0]=TR.
		 * 
		 * args[1] = Server url, will hold the server url which your client should use.
		 * Note, only use the server url supplied here as the url used by you during the
		 * development and by the evaluation server (for grading) is NOT the same!
		 * args[1] enables your client to always get the correct one.
		 * 
		 * args[2] = Holds the game ID which your client should use. For testing
		 * purposes you can create a new one by accessing
		 * 
		 * http://swe.wst.univie.ac.at:18235/games with your web browser.
		 * 
		 * IMPORANT: If there is a value stored in args[2] you MUST use it! DO NOT
		 * create new games in your code in such a case!
		 * 
		 */

		String serverBaseUrl;
		String gameID;
		Player player;
		GameController gameController;

		try {

			serverBaseUrl = args[1];
			gameID = args[2];
			player = new Player("Nemanja", "Srdanovic", "01576891");
			gameController = new GameController(serverBaseUrl, player, gameID);
			logger.info("Starting the game for {}:", player);
			gameController.startGame();

		} catch (StartParameterException e) {

			logger.error("Game parameter exception, exception message: ", e);

		} catch (ArrayIndexOutOfBoundsException e) {

			logger.error("Game parameter exception, exception message: ", e);
		}

	}
}
