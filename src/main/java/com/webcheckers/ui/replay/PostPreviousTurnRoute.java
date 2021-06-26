package com.webcheckers.ui.replay;

import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Message;
import com.webcheckers.model.Player;
import com.webcheckers.model.ReplayBoard;
import com.webcheckers.ui.GetHomeRoute;
import spark.*;

import java.util.Objects;

public class PostPreviousTurnRoute implements Route{
    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter; //final?
    private PlayerLobby playerLobby;
    //parameters

    /**
     * constructor for PostPreviousTurnRoute
     * @param gameCenter
     * @param templateEngine
     */
    public PostPreviousTurnRoute(GameCenter gameCenter, TemplateEngine templateEngine) {
        Objects.requireNonNull(gameCenter, "gameCenter not null");

        this.gameCenter = gameCenter;
        this.templateEngine = templateEngine;
    }
    /**
     * handle method for PostPreviousTurnRoute, allows users to move to previous turn based on interactions. 
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
//
//        Game game = gameCenter.getGame(gameID);

        replayBoard.prevTurn();

        Message returnMessage;

        // Returns true if the move was successful
        returnMessage = new Message(Message.TYPE.info, "true");

        return gson.toJson(returnMessage);
    }
}
