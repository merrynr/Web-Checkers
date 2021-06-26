package com.webcheckers.ui.replay;

import com.webcheckers.application.GameCenter;
import com.webcheckers.model.Player;
import spark.*;

import static spark.Spark.halt;

import java.util.Objects;
import java.util.logging.Logger;

public class GetStopWatchingRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetStopWatchingRoute.class.getName());

    private final GameCenter gameCenter;
    private final TemplateEngine templateEngine;

    public static final String TITLE_ATTR = "title";
    public static final String TITLE = "Game list";
    public static final String VIEW_NAME = "gamelist.ftl";

    public static final String CURRENT_PLAYER = "currentPlayer";
    /**
     * Constructor for StopWatchingRoute
     * @param gameCenter
     * @param templateEngine
     */
    public GetStopWatchingRoute(GameCenter gameCenter, TemplateEngine templateEngine) {
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(templateEngine, "gameCenter must not be null");

        this.gameCenter = gameCenter;
        this.templateEngine = templateEngine;

        LOG.config("Replay Mode: GetStopWatchingRoute is initialized.");
    }
    /**
     * Handle method for GetStopWatchingRoute, allows players to exit replay mode
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

        LOG.finer("GetHomeRoute is invoked.");
        //
        final Session session = request.session();
        final String username = session.attribute(CURRENT_PLAYER); //may not need, just getting things
        final String gameid = request.queryParams("gameID"); //same for this
        Player player = gameCenter.getPlayer(username);
        player.setReplayBoard(null);

        response.redirect("/");
        halt();
        return null;
    }
}
