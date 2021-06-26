package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.*;

import com.webcheckers.model.Message;
import spark.*;

/**
 * Validating moves
 * @author anthony, priya, merry, dante
 *
 */

public class PostValidateMoveRoute implements Route{

	private static final Logger LOG = Logger.getLogger(PostValidateMoveRoute.class.getName());

	private final TemplateEngine templateEngine;
	private final GameCenter gameCenter;
	//parameters
	/**
	 * Constructor for PostValidateMoveRoute
	 * @param gameCenter
	 * @param templateEngine
	 */
	
	public PostValidateMoveRoute(GameCenter gameCenter, TemplateEngine templateEngine) {
		Objects.requireNonNull(gameCenter, "gameCenter not null");
		
		this.gameCenter = gameCenter;
		this.templateEngine = templateEngine;
	}

	/**
	 * Handle method for PostValidateMove, gets game and attributes, checks logic against states of validity 
	 * @param request
	 * @param response
	 * @return a message object with type info if the move is valid
	 * and returns messages considering validity specific to cases.
	 */
	@Override
	public Object handle(Request request, Response response) {

		final Session session = request.session();
		final String username = session.attribute(GetHomeRoute.CURRENT_PLAYER);
		String newGameID = gameCenter.getGameID(username);
		Game game = this.gameCenter.getGame(newGameID);

		Message returnMessage;
		Gson gson = new Gson();
		final String json = request.body(); // json of the move object in the request body
		Move move = gson.fromJson(json, Move.class); //converts json to object

		int startRow, startCell, endRow, endCell;
		startRow = move.getStart().getRow();
		startCell = move.getStart().getCell();
		endRow = move.getEnd().getRow();
		endCell = move.getEnd().getCell();

		Move.MOVETYPE movetype = game.validateMove(startRow, startCell, endRow, endCell);
		boolean valid = false;

		switch(movetype) {
			case WRONGDIRECTION:
				valid = false;
				returnMessage = new Message(Message.TYPE.ERROR, "Can not move a non-king piece backwards.");
				break;
			case INVALIDCHAIN:
				valid = false;
				returnMessage = new Message(Message.TYPE.ERROR, "Cannot perform a take after jumping a single square.");
				break;
			case INVALIDEND:
				valid = false;
				returnMessage = new Message(Message.TYPE.ERROR, "Invalid drop location.");
				break;
			case NOPIECE:
				valid = false;
				returnMessage = new Message(Message.TYPE.ERROR, "There must be a piece for you to take.");
				break;
			case SELFTAKE:
				valid = false;
				returnMessage = new Message(Message.TYPE.ERROR, "Cannot take your own piece.");
				break;
			case SINGLEFIRST:
				valid = false;
				returnMessage = new Message(Message.TYPE.ERROR, "Cannot move a single square after taking a piece.");
				break;
			case BETTERMOVE:
				valid = false; // might need to be false since it is a move that shouldn't be made
				returnMessage = new Message(Message.TYPE.ERROR, "Must take jump move");
				break;
			case ENDOCCUPIED:
				valid = false; // might need to be false since it is a move that shouldn't be made
				returnMessage = new Message(Message.TYPE.ERROR, "Drop point is occupied");
				break;
			case SINGLE:    //__________valid cases__________
				valid = true;
				returnMessage = new Message(Message.TYPE.info, "");
				break;
			case TAKE:
				valid = true;
				returnMessage = new Message(Message.TYPE.info, "");
				break;
			default:
				returnMessage = new Message(Message.TYPE.ERROR, "LOGIC ERROR: Not initialized");
		}

		//-----------------> Update conditional to test for new movetypes<-----------------//
		return gson.toJson(returnMessage);
	}
}
