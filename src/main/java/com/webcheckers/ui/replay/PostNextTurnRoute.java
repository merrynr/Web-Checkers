package com.webcheckers.ui.replay;

import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.*;
import com.webcheckers.ui.GetHomeRoute;
import spark.*;

import java.util.Objects;

public class PostNextTurnRoute implements Route{
    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter; //final?
    private PlayerLobby playerLobby;
    //parameters
    
    /**
     * constructor for PostNextTurnRoute
     * @param gameCenter
     * @param templateEngine
     */

    public PostNextTurnRoute(GameCenter gameCenter, TemplateEngine templateEngine) {
        Objects.requireNonNull(gameCenter, "gameCenter not null");

        this.gameCenter = gameCenter;
        this.templateEngine = templateEngine;
    }
    /**
     * handle method for PostNextTurnRoute, returns messages if user wants to move to next turn based on interaction
     */
    @Override
    public Object handle(Request request, Response response) {
        final Session session = request.session();
        String username = session.attribute(GetHomeRoute.CURRENT_PLAYER);
        Player player = gameCenter.getPlayer(username);

        ReplayBoard replayBoard = player.getReplayBoard();

        Gson gson = new Gson();

//        final String json = request.body(); // json of the move object in the request body
//        String gameID = gson.fromJson(json, String.class); //converts json to object

        Message returnMessage;

        replayBoard.nextTurn();

        // Returns true if the move was successful
        returnMessage = new Message(Message.TYPE.info, "true");

        return gson.toJson(returnMessage);
    }
}
