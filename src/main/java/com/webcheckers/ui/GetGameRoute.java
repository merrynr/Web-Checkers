package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.application.GameCenter;
import com.webcheckers.model.Board;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;

import spark.*;


public class GetGameRoute implements Route{

    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private final GameCenter gameCenter;
    private final TemplateEngine templateEngine;
    public static final String TITLE_ATTR = "title";
    public static final String TITLE = "Your Checkers Arena";
    public static final String VIEW_NAME = "game.ftl";

    public static final String GAMEID = "gameID";
    public static final String CURRENT_PLAYER = "currentPlayer";
    public static final String VIEWMODE = "viewMode";
    public static final String MODEOPTIONS = "modeOptions";

    public static final String REDPLAYER = "redPlayer";
    public static final String WHITEPLAYER = "whitePlayer";
    public static final String ACTIVECOLOR = "activeColor";
    public static final String GAMESTATE = "board";


    /**
     * constructor for GetGameRoute
     * @param gameCenter
     * @param templateEngine
     */
    public GetGameRoute(final GameCenter gameCenter, final TemplateEngine templateEngine) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        //
        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;
        LOG.config("GetGameRoute is initialized.");

    }

    /**
     * Handles the GetGameRoute
     * @param request
     * @param response
     * @return the rendered HTML for the Game page
     * @throws Exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("GetGameRoute is invoked.");
        Map<String, Object> vm = new HashMap<>();
        vm.put(GetGameRoute.TITLE_ATTR, GetGameRoute.TITLE);

        final Session session = request.session();
        final String username = session.attribute(CURRENT_PLAYER);
        Player currentPlayer = gameCenter.getPlayer(username);
        // new game is starting
        if (currentPlayer.isInGame() == false) {
            Player opponent = this.gameCenter.getPlayer(request.queryParams("opponent"));
            gameCenter.setGameJustStarted(true);
            if (opponent.isInGame()) { //TODO did not test yet
                vm.put(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE);
                vm.put(GetHomeRoute.ERROR_MESSAGE_ATTR, "Error: Player in game.");
                vm.put(GetHomeRoute.CURRENT_PLAYER, username);
                vm.put(GetHomeRoute.PLAYER_LIST, gameCenter.getUserDatabase().keySet());
                response.redirect(WebServer.HOME_URL); //go home if opponent is in game
                return templateEngine.render(new ModelAndView(vm, GetHomeRoute.VIEW_NAME));
            }
            String newGameID = this.gameCenter.startGame(username, opponent.getUsername());
            Game game = this.gameCenter.getGame(newGameID);
            Board board = game.getBoard();

            vm.put(GetGameRoute.TITLE_ATTR, GetGameRoute.TITLE);
            vm.put(GAMEID, newGameID);
            vm.put(CURRENT_PLAYER, currentPlayer.getUsername());
            vm.put(VIEWMODE, "PLAY");
            //vm.put(MODEOPTIONS, "modeOptions!'{}'"); //yeah, need JSON.. (not yet - changed to match js)
            vm.put(REDPLAYER, game.getRedPlayer().getUsername()); //player is red
            vm.put(WHITEPLAYER, game.getWhitePlayer().getUsername());
            vm.put(ACTIVECOLOR, game.getActiveColor());
            vm.put(GAMESTATE, board.getBoard(currentPlayer));
            return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
        }
        // player is in a game
        else{
            gameCenter.setGameJustStarted(false);
            String gameID = gameCenter.getGameID(username);
            Game game = gameCenter.getGame(gameID);

            if (game.isOver()){
                vm.put(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE);
                vm.put(GetHomeRoute.CURRENT_PLAYER, username);
                vm.put(GetHomeRoute.PLAYER_LIST, gameCenter.getUserDatabase().keySet());
                vm.put(GetHomeRoute.IN_GAME_ATTR, "true");
                // checks how the game ended
                if (game.opponentResigned()){
                    vm.put(GetHomeRoute.END_GAME_MESSAGE_ATTR, "Opponent resigned.");
                }
                else{
                    vm.put(GetHomeRoute.END_GAME_MESSAGE_ATTR, "You lost.");
                }
                response.redirect(WebServer.HOME_URL);
                return templateEngine.render(new ModelAndView(vm, GetHomeRoute.VIEW_NAME));
            }
            else{
                // player is in game and board needs to be updated
                Board board = game.getBoard();

                vm.put(GetGameRoute.TITLE_ATTR, GetGameRoute.TITLE);
                vm.put(GAMEID, gameID);
                vm.put(CURRENT_PLAYER, currentPlayer.getUsername());
                vm.put(VIEWMODE, "PLAY");
                //vm.put(MODEOPTIONS, "modeOptions!'{}'");
                vm.put(REDPLAYER, game.getRedPlayer().getUsername()); //player is red
                vm.put(WHITEPLAYER, game.getWhitePlayer().getUsername());
                vm.put(ACTIVECOLOR, game.getActiveColor());
                vm.put(GAMESTATE, board.getBoard(currentPlayer));

                return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
            }
        }
    }
}
