package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Message;
import com.webcheckers.model.Player;
import spark.*;

import java.util.Objects;

public class PostCheckTurnRoute implements Route{

    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;
    //parameters

    /**
     * PostCheckTurnRoute constructor 
     * @param gameCenter
     * @param templateEngine
     */
    public PostCheckTurnRoute(GameCenter gameCenter, TemplateEngine templateEngine) {
        Objects.requireNonNull(gameCenter, "gameCenter not null");

        this.gameCenter = gameCenter;
        this.templateEngine = templateEngine;
    }
    /**
     * Handle method for PostCheckTurn, sends messages regarding game state
     */
    @Override
    public Object handle(Request request, Response response) {
        final Session session = request.session();
        String username = session.attribute(GetHomeRoute.CURRENT_PLAYER);
        Player player = gameCenter.getPlayer(username);
        String gameID = gameCenter.getGameID(username);
        Game game = gameCenter.getGame(gameID);
        Message returnMessage;
        if (game.isTurn(player)){
            returnMessage = new Message(Message.TYPE.info, "true");
        }
        else if(game.opponentResigned()){
            returnMessage = new Message(Message.TYPE.info, "true");
        }
        else{
            returnMessage = new Message(Message.TYPE.info, "false");
        }
        Gson gson = new Gson();
        return gson.toJson(returnMessage);
    }
}
