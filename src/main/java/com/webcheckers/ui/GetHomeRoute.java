package com.webcheckers.ui;

import java.util.*;
import java.util.logging.Logger;

import com.webcheckers.application.GameCenter;

import com.webcheckers.model.Board;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import spark.*;

/**
 * The UI Controller to GET the Home page.
 *
 * @author Bryan Basham, Priya, Anthony, Dante, Merry
 */
public class GetHomeRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    private final GameCenter gameCenter;
    private final TemplateEngine templateEngine;

    public static final String TITLE_ATTR = "title";
    public static final String TITLE = "Game home: Welcome!";
    public static final String VIEW_NAME = "home.ftl";

    public static final String CURRENT_PLAYER = "currentPlayer";
    public static final String PLAYER_LIST = "userDatabaseString";
    public static final String NUMBER_OF_PLAYERS_ATTR = "numberPlayers";
    public static final String ERROR_MESSAGE_ATTR = "error messege";
    public static final String END_GAME_MESSAGE_ATTR = "endGameMessage";
    public static final String GAME_ATTR = "game";
    public static final String IN_GAME_ATTR = "inGame";



    /**
    * Create the Spark Route (UI controller) for the
    * {@code GET /} HTTP request.
    *
    * @param templateEngine
    *   the HTML template rendering engine
    */
    public GetHomeRoute(final GameCenter gameCenter, final TemplateEngine templateEngine) {
    // validation
    Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    Objects.requireNonNull(gameCenter, "gameCenter must not be null");
    //
    this.templateEngine = templateEngine;
    this.gameCenter = gameCenter;
    //

    LOG.config("GetHomeRoute is initialized.");
    }

    /**
    * Render the WebCheckers Home page.
    *
    * @param request
    *   the HTTP request
    * @param response
    *   the HTTP response
    *
    * @return
    *   the rendered HTML for the Home page
    */
    @Override
    public Object handle(Request request, Response response) {
    LOG.finer("GetHomeRoute is invoked.");
    //
    final Session session = request.session();
    final String username = session.attribute(CURRENT_PLAYER);
    Map<String, Object> vm = new HashMap<>();
    if (username != null) { 					//if signed in
        if (gameCenter.getPlayer(username).isInGame() == true) { 	//if player is in game
            if (gameCenter.gameJustStarted()){						//if game is new (not ongoing)
                String newGameID = gameCenter.getGameID(username);	//go through steps to create a game
                Game game = this.gameCenter.getGame(newGameID);
                Board board = game.getBoard();

                vm.put(GetGameRoute.TITLE_ATTR, GetGameRoute.TITLE);
                vm.put(GetGameRoute.GAMEID, newGameID);

                vm.put(CURRENT_PLAYER, username);
                vm.put(GetGameRoute.VIEWMODE, "PLAY");
                vm.put(GetGameRoute.MODEOPTIONS, "modeOptionsAsJSON!'{}'");
                vm.put(GetGameRoute.REDPLAYER, game.getRedPlayer().getUsername()); //player is red
                vm.put(GetGameRoute.WHITEPLAYER, game.getWhitePlayer().getUsername());
                vm.put(GetGameRoute.ACTIVECOLOR, game.getActiveColor());
                vm.put(GetGameRoute.GAMESTATE, board.getBoard(gameCenter.getPlayer(username)));

                response.redirect("/game");
                return templateEngine.render(new ModelAndView(vm, GetGameRoute.VIEW_NAME));
            } else if (gameCenter.gameIsOver(username)){
                Player currentPlayer = gameCenter.getPlayer(username);

                vm.put(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE);
                vm.put(CURRENT_PLAYER, username);
                if (gameCenter.isResigned(username)){
                    vm.put(GetHomeRoute.END_GAME_MESSAGE_ATTR, "Opponent resigned.");
                }
                else if (currentPlayer.equals(gameCenter.getWinner(username))){
                    vm.put(GetHomeRoute.END_GAME_MESSAGE_ATTR, "You won!");
                }else{
                    vm.put(GetHomeRoute.END_GAME_MESSAGE_ATTR, "You lost.");
                }
                ArrayList<String> playerList = gameCenter.getActiveUserList();
                vm.put(PLAYER_LIST, playerList);
                vm.put(NUMBER_OF_PLAYERS_ATTR, gameCenter.getNumPlayers());

                currentPlayer.removeGame(); // gameIsOver so update player to reflect that

                return templateEngine.render(new ModelAndView(vm, GetHomeRoute.VIEW_NAME));
            }
            else {
                vm.put(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE);
                vm.put(CURRENT_PLAYER, username);
                vm.put(GAME_ATTR, "true"); // there is a current game
                vm.put(IN_GAME_ATTR, "true"); //the player is in a game
                ArrayList<String> playerList = gameCenter.getActiveUserList();
                vm.put(PLAYER_LIST, playerList);
                vm.put(NUMBER_OF_PLAYERS_ATTR, gameCenter.getNumPlayers());
                return templateEngine.render(new ModelAndView(vm, GetHomeRoute.VIEW_NAME));
            }
        }
    } {
        vm.put(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE);
        vm.put(CURRENT_PLAYER, username);
        ArrayList<String> playerList = gameCenter.getActiveUserList();
        vm.put(PLAYER_LIST, playerList);
        vm.put(NUMBER_OF_PLAYERS_ATTR, gameCenter.getNumPlayers());
        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));

    }
  }
}
