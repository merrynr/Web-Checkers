package com.webcheckers.ui;

import com.webcheckers.application.GameCenter;
import spark.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.webcheckers.application.GameCenter;

/**
 * This is the SIGN IN post route. goes to signout.ftl if sign in fails and home.ftl if successful.
 *
 * @author Priya, Anthony, Dante, Merry
 */
public class PostSignInRoute implements Route{
  
	private final GameCenter gameCenter;

    private static final Logger LOG = Logger.getLogger(PostSignInRoute.class.getName());

    public static final String USERNAME_ATTR = "username";
    public static final String CURRENT_PLAYER = "currentPlayer";

    static final String INVALID_NAME = "invalid username";
    static final String ACCT_SIGNED_IN = "account already online";

    private final TemplateEngine templateEngine;

    /**
     * PostSignInRoute constructor
     * @param gameCenter
     * @param templateEngine
     */
    
    public PostSignInRoute(GameCenter gameCenter, TemplateEngine templateEngine) {
        //create instance of GameCenter
        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;
    }

    /**
     * Handles data obtained from user by sign-in page, processes it, and goes to Sign-in or home pages accordingly.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the Sign-in page or Home page
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

        final String username = request.queryParams(USERNAME_ATTR);
        final Session session = request.session();

        LOG.config("The username you entered is: " + username);

        final Map<String, Object> vm = new HashMap<>();


        if((username.matches("[a-zA-Z0-9]+$")) && (username.trim().length() > 0) || username.trim().equals(gameCenter.checkUserExists(username.trim()))) {
             //check if user is in the database
            if(gameCenter.checkUserExists(username)) {
                //check if user is signed in
                if(gameCenter.checkLoggedIn(username)) {
                    vm.put(GetSignInRoute.TITLE_ATTR, GetSignInRoute.TITLE);
                    vm.put(GetSignInRoute.SIGNIN_MSG_ATTR, ACCT_SIGNED_IN);

                    return templateEngine.render(new ModelAndView(vm, GetSignInRoute.VIEW_NAME));
                }
                else { //user is NOT signed in
                    //just login
                    gameCenter.setUserLoggedIn(username); //add user to arrayList (active users) & numPlayers increments

                    final Session httpSession = request.session();
                    httpSession.attribute(GetHomeRoute.CURRENT_PLAYER, username);
                    session.attribute(USERNAME_ATTR, username);

                    vm.put(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE);
                    vm.put(USERNAME_ATTR, username);
                    vm.put(GetHomeRoute.CURRENT_PLAYER, username);

                    response.redirect("/");

                    return templateEngine.render(new ModelAndView(vm, GetHomeRoute.VIEW_NAME));
                }
            }
            else { //the user is not in the database
                //create the account and then login
                gameCenter.addUser(username); //add user to hashMap (database)
                gameCenter.setUserLoggedIn(username); //add user to arrayList (active users) & numPlayers increments

                final Session httpSession = request.session();
                httpSession.attribute(GetHomeRoute.CURRENT_PLAYER, username);

                vm.put(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE);
                vm.put(USERNAME_ATTR, username);
                vm.put(GetHomeRoute.CURRENT_PLAYER, username);
                ArrayList<String> playerList = gameCenter.getActiveUserList();
                vm.put(GetHomeRoute.PLAYER_LIST, playerList);
                response.redirect(WebServer.HOME_URL);
                return templateEngine.render(new ModelAndView(vm, GetHomeRoute.VIEW_NAME));
            }
        }
        else { //the name entered is invalid
            vm.put(GetSignInRoute.TITLE_ATTR, GetSignInRoute.TITLE);
            vm.put(GetSignInRoute.SIGNIN_MSG_ATTR, INVALID_NAME);

            return templateEngine.render(new ModelAndView(vm, GetSignInRoute.VIEW_NAME));
        }
    }
}
