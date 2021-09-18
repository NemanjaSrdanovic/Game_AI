package client.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import MessagesBase.ERequestState;
import MessagesBase.PlayerMove;
import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.GameState;
import reactor.core.publisher.Mono;

/**
 * This network object is used by the game controller to send and receive data
 * from/to the server.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class Network {

	private UniquePlayerIdentifier uniquePlayerID;
	private String gameID;
	private String playerID;
	private String serverBaseUrl;
	private GameState gameState;
	private WebClient baseWebClient;
	private Mono<ResponseEnvelope> webAccess;
	private static Logger logger = LoggerFactory.getLogger(Network.class);

	/**
	 * Instantiates a new network object. The parameters must not be null.
	 * 
	 * @param gameID
	 * @param serverBaseUrl
	 */
	public Network(String gameID, String serverBaseUrl) {

		super();
		this.gameID = gameID;
		this.serverBaseUrl = serverBaseUrl;

		baseWebClient = WebClient.builder().baseUrl(serverBaseUrl + "/games")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();
	}

	/**
	 * 
	 * The client uses this method to send an HTTP post request with an XML message
	 * in the body to the following endpoint:
	 * http(s)://<domain>:<port>/games/<SpielID>/players
	 * 
	 * Hereby is the part ‹SpielID› with the unique gameID obtained during game
	 * creation to replace.
	 * 
	 * If the request is carried out correctly, the player is registered to the game
	 * and gets a registration number.
	 * 
	 * @param player (PlayerRegistration object from the network protocol)
	 * @return (String (UUID) e.g. a2906a21-6cc9-43a2-844f-c3f79a7d54f4)
	 */
	public String registerPlayer(PlayerRegistration player) {

		webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameID + "/players")
				.body(BodyInserters.fromValue(player)).retrieve().bodyToMono(ResponseEnvelope.class);

		ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();

		if (resultReg.getState() == ERequestState.Error) {

			logger.error("Registration error, error message: {}", resultReg.getExceptionMessage());

		} else {
			uniquePlayerID = resultReg.getData().get();
			logger.info("Player successfully registred! Player ID: {}", uniquePlayerID.getUniquePlayerID());
		}

		playerID = uniquePlayerID.getUniquePlayerID();
		return playerID;
	}

	/**
	 * The client uses this method to send an HTTP post request with an XML message
	 * in the body to the following endpoint:
	 * http(s)://<domain>:<port>/games/<SpielID>/halfmaps
	 * 
	 * Hereby is the part ‹SpielID› with the unique gameID obtained during game
	 * creation to replace.
	 * 
	 * If the request is carried out correctly, the player has successfully send his
	 * part of the map to the server.
	 * 
	 * @param halfMap (HalfMap object from the network protocol)
	 */
	public void sendHalfMap(MessagesBase.HalfMap halfMap) {

		webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameID + "/halfmaps")
				.body(BodyInserters.fromValue(halfMap)).retrieve().bodyToMono(ResponseEnvelope.class);

		ResponseEnvelope resultReg = webAccess.block();

		if (resultReg.getState() == ERequestState.Error) {

			logger.error("Map transmission error, error message: {}", resultReg.getExceptionMessage());

		} else {

			logger.info("Map successfully send!");

		}

	}

	/**
	 * The client uses this method to send an GET request with an XML message in the
	 * body to the following endpoint:
	 * http(s)://<domain>:<port>/games/<SpielID>/states/<SpielerID>
	 * 
	 * Here is the part ‹SpielID› with the unique gameID obtained during game
	 * creation to replace and <SpielerID> with the unique gamerID communicated by
	 * the server during player registration.
	 * 
	 * If the request is carried out correctly, the player will get all the game
	 * relevant information in the response.
	 * 
	 * @return (GameState object from the network protocol)
	 */
	public GameState getGameStatus() {

		webAccess = baseWebClient.method(HttpMethod.GET).uri("/" + gameID + "/states/" + playerID).retrieve()
				.bodyToMono(ResponseEnvelope.class);

		ResponseEnvelope<GameState> resultReg = webAccess.block();

		if (resultReg.getState() == ERequestState.Error) {

			logger.error("Game state request error, error message: {}", resultReg.getExceptionMessage());

		} else {
			gameState = resultReg.getData().get();
		}

		return gameState;

	}

	/**
	 * The client uses this method to send an HTTP post request with an XML message
	 * in the body to the following endpoint:
	 * http(s)://<domain>:<port>/games/<SpielID>/moves
	 * 
	 * If the request is carried out correctly, the player has moved his avatar to a
	 * next desired field.
	 * 
	 * @param move
	 */
	public void sendPlayerMove(PlayerMove move) {

		webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameID + "/moves")
				.body(BodyInserters.fromValue(move)).retrieve().bodyToMono(ResponseEnvelope.class);

		ResponseEnvelope resultReg = webAccess.block();

		if (resultReg.getState() == ERequestState.Error) {

			logger.error("Player move error, error message: {}", resultReg.getExceptionMessage());

		}
	}
}