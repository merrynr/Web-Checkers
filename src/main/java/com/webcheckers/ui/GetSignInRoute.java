package com.webcheckers.ui;

import spark.*;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import com.webcheckers.application.GameCenter;

/**
 * This is the SIGN IN get route. goes to signout.ftl
 *
 * @author Priya, Anthony, Dante, Merry
 */

public class GetSignInRoute implements Route{
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;

    public static final String TITLE_ATTR = "title";
    public static final String TITLE = "Login";
    public static final String VIEW_NAME = "signin.ftl";
    public static final String SIGNIN_MSG_ATTR = "signin_message";
    public static final String SIGNIN_MESSAGE = "If you have an account please enter your username, otherwise create a new account";


    /**
     * * Create the Spark Route (UI controller) for the
     * * {@code GET /} HTTP request.
     *
     * @param templateEngine *   the HTML template rendering engine
     */
    public GetSignInRoute(final  GameCenter gameCenter, final TemplateEngine templateEngine) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;
        //
        LOG.config("GetSignInRoute is initialized.");
    }

    /**
     * Render the WebCheckers Sign-in page.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetSignInRoute is invoked.");
        //
        final Map<String, Object> vm = new HashMap<>();
        vm.put(TITLE_ATTR, TITLE);
        vm.put(SIGNIN_MSG_ATTR, SIGNIN_MESSAGE);
        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));

    }
}
