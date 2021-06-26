package com.webcheckers.ui.replay;

import com.webcheckers.model.*;
import com.webcheckers.ui.GetHomeRoute;
import spark.*;
import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;

import static spark.Spark.halt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Class for replay mode game route
 * @author anthony, priya, merry, dante
 *
 */
public class GetGameRoute implements Route{

    private static final Logger LOG = Logger.getLogger(com.webcheckers.ui.replay.GetGameRoute.class.getName());


    private final GameCenter gameCenter;
    private final TemplateEngine templateEngine;
    public static final String TITLE_ATTR = "title";
    public static final String TITLE = "Your Checkers Arena";
    public static final String VIEW_NAME = "game.ftl";

    public static final String CURRENT_PLAYER = "currentPlayer";
    public static final String VIEWMODE = "viewMode";
    public static final String MODEOPTIONS = "modeOptions";

    public static final String REDPLAYER = "redPlayer";
    public static final String WHITEPLAYER = "whitePlayer";
    public static final String ACTIVECOLOR = "activeColor";
    public static final String GAMESTATE = "board";
    public static final String MODEOPTIONS_AS_JSON = "modeOptionsAsJSON";

    /**
     * Constructor for replay's GameRoute.
     * @param gameCenter
     * @param templateEngine
     */
    public GetGameRoute(final GameCenter gameCenter, final TemplateEngine templateEngine) {
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(gameCenter, "gameCenter must not be null");

        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;

        LOG.config("Replay Mode: GetGameRoute is initialized.");
    }

    /**
     * handle method for replay, renders replay's game and connects to replaymenu.ftl, checks if user is logged in
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Session session = request.session();
        final String username = session.attribute(CURRENT_PLAYER);
        final String gameID = request.queryParams("gameID");
        Game game = gameCenter.getGame(gameID);
        Player player = gameCenter.getPlayer(username);
        ReplayBoard replayBoard;

        //error scenario
        if (gameID == null || username == null) {
            response.redirect("/");
            halt();
            return null;
        }

        if (player.getReplayBoard() == null) {
            replayBoard = game.createReplay(player);
            player.setReplayBoard(replayBoard);
        }else{
            replayBoard = player.getReplayBoard();
        }

        Map<String, Object> vm = new HashMap<>();
        vm.put(GetGameRoute.TITLE_ATTR, GetGameRoute.TITLE);

        Map<String, Object> modeOptions = new HashMap<>();
        //TODO: Check turns list to see if their is a next turn and previous turn
        boolean hasNext = replayBoard.hasNext();
        modeOptions.put("hasNext", hasNext); //true if there is a next turn
        boolean hasPrev = replayBoard.hasPrev();
        modeOptions.put("hasPrevious", hasPrev);
        Gson gson = new Gson();
        System.out.println("checkpoint 3");

        vm.put(com.webcheckers.ui.GetGameRoute.TITLE_ATTR, com.webcheckers.ui.GetGameRoute.TITLE);
        vm.put(VIEWMODE, "REPLAY");
        vm.put(CURRENT_PLAYER, username); //could also use attribute
        vm.put(REDPLAYER, game.getRedPlayer().getUsername()); //player is red
        vm.put(WHITEPLAYER, game.getWhitePlayer().getUsername()); //temp opponent
        vm.put(ACTIVECOLOR, replayBoard.getActiveColor()); //FIXME: Will be from replayBoard
        vm.put(GAMESTATE, replayBoard.renderBoard()); //FIXME: Get from replayBoard
        vm.put(MODEOPTIONS, gson.toJson(modeOptions));

        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
