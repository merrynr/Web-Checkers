package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;

import com.webcheckers.application.GameCenter;
import spark.*;


import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


/**
 * This is the post SIGN OUT route
 * @author Priya, Anthony, Dante, Merry
 *
 */
public class PostSignOutRoute implements Route {

    private static final Logger LOG = Logger.getLogger(PostSignOutRoute.class.getName());

    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;

    public PostSignOutRoute(GameCenter gameCenter, TemplateEngine templateEngine) {
        //create instance of GameCenter
        this.gameCenter = gameCenter;
        this.templateEngine = templateEngine;
        //
        LOG.config("GetSignInRoute is initialized.");
    }

    /**
     * Responds to user wanting to sign out from home page and updates the home page after signing out.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

        final Map<String, Object> vm = new HashMap<>();
        final Session session = request.session();
        String username = session.attribute(GetHomeRoute.CURRENT_PLAYER);
        gameCenter.setUserLoggedOut(username);
        session.attribute(GetHomeRoute.CURRENT_PLAYER, null);
        vm.put(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE);
        vm.put(GetHomeRoute.NUMBER_OF_PLAYERS_ATTR, gameCenter.getNumPlayers());
        response.redirect("/");
        return templateEngine.render(new ModelAndView(vm, GetHomeRoute.VIEW_NAME));
    }
}
