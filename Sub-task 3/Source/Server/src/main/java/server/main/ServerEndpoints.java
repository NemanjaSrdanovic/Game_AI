package server.main;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.GameState;
import server.enumerations.EPlayerStateValue;
import server.exceptions.GameRuleException;
import server.exceptions.RuleException;
import server.game.Game;
import server.game.RunningGames;
import server.rules.GameConstants;
import server.rules.IRule;

@Controller
@RequestMapping(value = "/games")
public class ServerEndpoints {

	private static Logger logger = LoggerFactory.getLogger(ServerEndpoints.class);

	/**
	 * Before two clients can compete against each other, one of them must have
	 * created a new game on the server (this is not carried out by the AI, but by a
	 * human). To do this, it is necessary to access the endpoint below by sending
	 * an HTTP GET request to it.
	 * 
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody UniqueGameIdentifier newGame() {

		String gameId = UUID.randomUUID().toString().substring(0, 5);
		UniqueGameIdentifier gameIdentifier = new UniqueGameIdentifier(gameId);
		Game newGame = new Game(gameIdentifier);

		for (IRule rule : newGame.getGameController().getGameRules().getRules()) {

			rule.newGame();
		}

		RunningGames.getGames().put(gameId, newGame);

		logger.info("New game created. ID :  {}", gameId);

		return gameIdentifier;

	}

	/**
	 * The client sends an HTTP post request with an XML message to the endpoint
	 * below.Information about the client's developer is transmitted as the body of
	 * the message. This would be the first name ( studentFirstName ), last name (
	 * studentLastName ) and the matriculation number ( studentID ).
	 * 
	 * The server replies to the registration message with a confirmation that the
	 * registration has been carried out and a corresponding unambiguous PlayerID .
	 * These PlayerID must be transmitted with the following requests so that the
	 * server knows from which client (or player) this request was made.
	 * 
	 * @param gameID
	 * @param playerRegistration
	 * @return
	 */
	@RequestMapping(value = "/{gameID}/players", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<UniquePlayerIdentifier> registerPlayer(@PathVariable String gameID,
			@Validated @RequestBody PlayerRegistration playerRegistration) {

		if (RunningGames.getGames().get(gameID) == null) {

			throw new GameRuleException("Game id not found.", "The provided gameId is invalid.");
		}

		for (IRule rule : RunningGames.getGames().get(gameID).getGameController().getGameRules().getRules()) {

			rule.registerPlayer(playerRegistration, gameID);

		}

		UniquePlayerIdentifier newPlayerID = new UniquePlayerIdentifier(UUID.randomUUID().toString());

		RunningGames.getGames().get(gameID).getGameController().addNewPlayer(playerRegistration, newPlayerID);

		ResponseEnvelope<UniquePlayerIdentifier> playerIDMessage = new ResponseEnvelope<>(newPlayerID);

		logger.info(
				"New player for game registered. GameID: " + gameID + " PlayerID: " + newPlayerID.getUniquePlayerID());

		return playerIDMessage;

	}

	/**
	 * 
	 * The client sends an HTTP POST request with an XML message in the body to the
	 * endpoint below.Here is the part ‹GameID› with the unique one obtained during
	 * game creation GameID to replace. Information about the half of the card is
	 * used as the body of the message. These consist of the unique PlayerID to let
	 * the server know which client sent the data and also from a list of fields
	 * that have halfMapNode Elements are mapped.
	 * 
	 * The server replies generically with a responseEnvelope . But since there is
	 * no data here, that is data Element not included in this case.
	 * 
	 * @param gameID
	 * @param halfMap
	 * @return
	 */
	@RequestMapping(value = "/{gameID}/halfmaps", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<?> receiveHalfMap(@PathVariable String gameID,
			@Validated @RequestBody HalfMap halfMap) {

		if (RunningGames.getGames().get(gameID) == null) {

			throw new GameRuleException("Game id not found.", "The provided gameId is invalid.");

		}

		for (IRule rule : RunningGames.getGames().get(gameID).getGameController().getGameRules().getRules()) {

			rule.receiveHalfMap(RunningGames.getGames().get(gameID).getGameController().getPlayers()
					.get(halfMap.getUniquePlayerID()), halfMap, gameID);
		}

		RunningGames.getGames().get(gameID).getGameController().setHalfMap(gameID, halfMap);

		logger.info("Half map received. Game: " + gameID + " Player: " + halfMap.getUniquePlayerID());

		return new ResponseEnvelope<>();

	}

	/**
	 * The client sends an HTTP GET request to the endpoint below. Here is the part
	 * GameID with the unique one obtained during game creation GameID to replace
	 * and PlayerID with the unique one communicated by the server during
	 * registration PlayerID . If both IDs are known to the server, the server
	 * replies with the game status, otherwise a corresponding error message is
	 * returned.
	 * 
	 * @param gameID
	 * @param playerID
	 * @return
	 */
	@RequestMapping(value = "/{gameID}/states/{playerID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<GameState> returnGameState(@PathVariable String gameID,
			@PathVariable String playerID) {

		if (RunningGames.getGames().get(gameID) == null) {

			throw new GameRuleException("Game id not found.", "The provided gameId is invalid.");

		}

		for (IRule rule : RunningGames.getGames().get(gameID).getGameController().getGameRules().getRules()) {

			rule.returnGameState(gameID, playerID);
		}

		ResponseEnvelope<GameState> gameState = new ResponseEnvelope<>(
				RunningGames.getGames().get(gameID).getGameController().getGameStateObject(gameID, playerID));

		logger.info("Game state returned. {}", gameState.getData().get().getPlayers());

		return gameState;

	}

	/**
	 * Each game is automatically removed / ended 10 minutes after the game was
	 * created, so no further messages can be sent to it. This scheduler is calling
	 * a method to do that every 10 minutes.
	 */
	@Scheduled(fixedRate = GameConstants.TIME_MILLIS_TO_CHECK_FOR_EXPIRED_GAMES)
	private void checkForExpiredGames() {

		RunningGames.removeExpiredGames();
	}

	/**
	 * The most basic way of handling exceptions in spring. The superclass catches
	 * subclasses exceptions which is than sent to the client. If a business rule is
	 * broke the exception is also handing over a player object so that that player
	 * state can be set to lost.
	 * 
	 * @param ex
	 * @param response
	 * @return
	 */
	@ExceptionHandler({ RuleException.class })
	public @ResponseBody ResponseEnvelope<?> handleException(RuleException ex, HttpServletResponse response) {
		ResponseEnvelope<?> result = new ResponseEnvelope<>(ex.getErrorName(), ex.getMessage());
		response.setStatus(HttpServletResponse.SC_OK);

		if (ex.getPlayer() != null)
			ex.getPlayer().setCurrentState(EPlayerStateValue.Lost);

		return result;
	}
}
