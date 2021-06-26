package com.webcheckers.ui;

import java.util.Objects;

import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;
import com.webcheckers.model.Game;
import com.webcheckers.model.Message;
import com.webcheckers.model.Player;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;

/**
 * Route for resigning a game
 * @author priya
 *
 */
public class PostResignGameRoute implements Route {
	   
	private TemplateEngine templateEngine;
	private GameCenter gameCenter;
	public static final String CURRENT_PLAYER = "currentPlayer";
	public static final String VIEW_NAME = "home.ftl";
	//parameters

	/**
	 * Constructor for PostResignGameRoute
	 * @param gameCenter
	 * @param templateEngine
	 */
	public PostResignGameRoute(GameCenter gameCenter, TemplateEngine templateEngine) {
		Objects.requireNonNull(gameCenter, "gameCenter not null");

		this.gameCenter = gameCenter;
		this.templateEngine = templateEngine;
	}

	/**
	 * Handle method for PostResignGameRoute, removes game when resigned and sends proper message
	 * @param request
	 * @param response
	 * @return a message of type info if successful, error otherwise
	 */
	@Override
	public Object handle(Request request, Response response) {

        final Session session = request.session();
        final String username = session.attribute(CURRENT_PLAYER);
        Player player = gameCenter.getPlayer(username);
        String gameID = gameCenter.getGameID(username);
        Message returnMessage;
        Gson gson = new Gson();
		Game game = this.gameCenter.getGame(gameID);
	    if (game.resignGame(player)) {
			player.removeGame();

			returnMessage = new Message(Message.TYPE.info, "Resign");
		} else {
			returnMessage = new Message(Message.TYPE.ERROR, "error");
		}

        return gson.toJson(returnMessage);
	}
}
