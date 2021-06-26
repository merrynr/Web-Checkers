package com.webcheckers.ui;

import com.webcheckers.application.GameCenter;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import spark.*;
import static spark.Spark.halt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class GetReplayMenuRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetReplayMenuRoute.class.getName());
    private final GameCenter gameCenter;
    private final TemplateEngine templateEngine;

    public static final String TITLE_ATTR = "title";
    public static final String TITLE = "replay list";
    public static final String VIEW_NAME = "replaymenu.ftl";

    public static final String CURRENT_PLAYER = "currentPlayer";
    public static final String GAME_LIST = "gamelist";
    public static final String NUMBER_OF_PLAYERS_ATTR = "numberPlayers";

    public GetReplayMenuRoute(GameCenter gameCenter, TemplateEngine templateEngine) {
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(gameCenter, "gameCenter must not be null");

        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;

        LOG.config("GetReplayMenuRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Session session = request.session();
        final String username = session.attribute(GetHomeRoute.CURRENT_PLAYER);
        Map<String, Object> vm = new HashMap<>();

        if(username == null) {
            response.redirect("/");
            halt();
            return null;
        }

        //TODO: Call game.replayMode()
        //TODO: Assign replayBoard to this player
        Player player = gameCenter.getPlayer(username);
        ArrayList<Game> gamesList = gameCenter.getFinishedGamesList();
        for (Game game:gamesList){
            game.createReplay(player);
        }

        vm.put(GetReplayMenuRoute.TITLE_ATTR, GetReplayMenuRoute.TITLE);
        vm.put("player", player);
        vm.put(CURRENT_PLAYER, username);
        vm.put(GAME_LIST, gamesList);
        vm.put(NUMBER_OF_PLAYERS_ATTR, gameCenter.getNumPlayers());

        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
