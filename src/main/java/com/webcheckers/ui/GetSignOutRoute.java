package com.webcheckers.ui;

import java.util.logging.Logger;

import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This is the SIGN OUT get route. goes to signout.ftl
 * @author priya, anthony, dante, merry
 *
 */
public class GetSignOutRoute implements Route {


	    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

	    private final TemplateEngine templateEngine;

	    public static final String TITLE_ATTR = "title";
	    public static final String TITLE = "logout";
	    public static final String VIEW_NAME = "signout.ftl";


	    /**
	     * * Create the Spark Route (UI controller) for the
	     * * {@code GET /} HTTP request.
	     *
	     * @param templateEngine *   the HTML template rendering engine
	     */
	    public GetSignOutRoute(final TemplateEngine templateEngine) {
	        // validation
	        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
	        //
	        this.templateEngine = templateEngine;
	        //
	        LOG.config("GetSignOutRoute is initialized.");
	    }

	    /**
	     * Render the WebCheckers Home page.
	     *
	     * @param request  the HTTP request
	     * @param response the HTTP response
	     * @return the rendered HTML for the Home page
	     */
	    @Override
	    public Object handle(Request request, Response response) {
	        LOG.finer("GetHomeRoute is invoked.");
	        //
	        Map<String, Object> vm = new HashMap<>();
	        vm.put(TITLE_ATTR, TITLE);
	        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));

	    }
}