package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;
import com.webcheckers.model.Game;
import com.webcheckers.model.Message;
import com.webcheckers.model.Move;
import com.webcheckers.model.Player;

import spark.*;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the PostSubmitTurnRoute. It handles when a player submits a turn.
 * @author Anthony Cianfrocco, Dante, Merry, Priya
 */
public class PostSubmitTurnRoute implements Route{

    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;

    /**
     * Constructor for PostSubmitTurn
     * @param gameCenter
     * @param templateEngine
     */
    public PostSubmitTurnRoute(GameCenter gameCenter, TemplateEngine templateEngine){
        this.gameCenter = gameCenter;
        this.templateEngine = templateEngine;
    }

    /**
     * handle method for PostSubmitTurn, calls turn validity and returns messages about validity
     * @param request
     * @param response
     * @return a message object of type info if the turn is valid
     *          other wise, error with an error message
     * @throws Exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

        final Map<String, Object> vm = new HashMap<>();
        final Session session = request.session();
        String username = session.attribute(GetHomeRoute.CURRENT_PLAYER);
        String gameID = gameCenter.getGameID(username);
        Game game = gameCenter.getGame(gameID);

        Message returnMessage;
        Gson gson = new Gson();

        Move.MOVETYPE movetype = game.submitTurn();

        switch (movetype) {
            case WRONGDIRECTION:
                returnMessage = new Message(Message.TYPE.ERROR, "Can not move a non-king piece backwards.");
                break;
            case INVALIDCHAIN:
                returnMessage = new Message(Message.TYPE.ERROR, "Cannot perform a take after jumping a single square.");
                break;
            case INVALIDEND:
                returnMessage = new Message(Message.TYPE.ERROR, "Invalid drop location.");
                break;
            case NOPIECE:
                returnMessage = new Message(Message.TYPE.ERROR, "There must be a piece for you to take.");
                break;
            case SELFTAKE:
                returnMessage = new Message(Message.TYPE.ERROR, "Cannot take your own piece.");
                break;
            case SINGLEFIRST:
                returnMessage = new Message(Message.TYPE.ERROR, "Cannot move a single square after taking a piece.");
                break;
            case BETTERMOVE:
                returnMessage = new Message(Message.TYPE.ERROR, "Must take jump move");
                break;
            case ENDOCCUPIED:
                returnMessage = new Message(Message.TYPE.ERROR, "Drop point is occupied");
                break;
            case PARTIALMULTI:
                System.out.println("PARTIAL");
                returnMessage = new Message(Message.TYPE.ERROR, "Must take next jump");
                break;
            case VALID:
                returnMessage = new Message(Message.TYPE.info, "");
                break;
            default:
                returnMessage = new Message(Message.TYPE.ERROR, "LOGIC ERROR: Not initialized");
        }

        return gson.toJson(returnMessage);
    }
}
