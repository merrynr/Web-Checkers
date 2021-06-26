package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Message;
import com.webcheckers.model.Player;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;

import java.util.Objects;

/**
 * Route for undoing a move
 * @author anthony, merry
 */
public class PostBackupMoveRoute implements Route{
	
	private final TemplateEngine templateEngine;
	private final GameCenter gameCenter; //final?
	private PlayerLobby playerLobby;
    public static final String CURRENT_PLAYER = "currentPlayer";

	//parameters
	public PostBackupMoveRoute(GameCenter gameCenter, TemplateEngine templateEngine) {

		Objects.requireNonNull(gameCenter, "gameCenter not null");
		
		this.gameCenter = gameCenter;
		this.templateEngine = templateEngine;
	}

	@Override
	public Object handle(Request request, Response response) {
        final Session session = request.session();
        final String username = session.attribute(CURRENT_PLAYER);
        String newGameID = gameCenter.getGameID(username);

		Message returnMessage;
        Gson gson = new Gson();

		Game currentGame = gameCenter.getGame(newGameID); //Changed from: gameCenter.getGames().get(newGameID); should be the same right?
		Player player = currentGame.getPlayer();
		Boolean isMyTurn = currentGame.isTurn(player);

		if(isMyTurn) {
			if(currentGame.backupMove()) { //TODO: message needs to state which moves were undone
				returnMessage = new Message(Message.TYPE.info, "Reverted piece back to last move");
			} else {
				returnMessage = new Message(Message.TYPE.info, "Cannot undo a move if there are none made yet");
			}
		} else {
			//return message with type error
			returnMessage = new Message(Message.TYPE.ERROR, "Cannot undo a move if it is not your turn");
		}
		return gson.toJson(returnMessage);
	}
	
}



