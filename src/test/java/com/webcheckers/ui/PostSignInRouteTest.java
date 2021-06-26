package com.webcheckers.ui;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import spark.*;

/**
 * Test Class for {@link PostSignInRoute} objects
 *
 * @author Merry Ren
 */
@Tag("ui-tier")
public class PostSignInRouteTest {

    private PostSignInRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;

    private PlayerLobby playerLobby;
    private GameCenter gameCenter;

    private String mockUser;


    /**
     * Setup mock objects
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        engine = mock(TemplateEngine.class);
        response = mock(Response.class);

        playerLobby = new PlayerLobby();
        gameCenter = new GameCenter(playerLobby);

        //Initialize a sample user in the database
        gameCenter.addUser("aLoggedInUser");
        gameCenter.setUserLoggedIn("aLoggedInUser");
        gameCenter.addUser("aNotLoggedInUser");


        CuT = new PostSignInRoute(gameCenter, engine);
    }

    /**
     * Test PostSignInRoute with a valid username
     */
    @Test
    public void signInGoodTest() {

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(request.queryParams(eq(PostSignInRoute.USERNAME_ATTR))).thenReturn("user1");

        try {
            CuT.handle(request, response);

            testHelper.assertViewModelExists();
            testHelper.assertViewModelIsaMap();

            testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE);
            testHelper.assertViewModelAttribute(PostSignInRoute.USERNAME_ATTR, "user1");
            testHelper.assertViewModelAttribute(GetHomeRoute.CURRENT_PLAYER, "user1");
            //testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST, );

            testHelper.assertViewName(GetHomeRoute.VIEW_NAME);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test PostSignInRoute with an already existing but not logged in username
     */
    @Test
    public void signInExistingUserGoodTest() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(request.queryParams(eq(PostSignInRoute.USERNAME_ATTR))).thenReturn("aNotLoggedInUser");

        try {
            CuT.handle(request, response);

            testHelper.assertViewModelExists();
            testHelper.assertViewModelIsaMap();

            testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE);
            testHelper.assertViewModelAttribute(PostSignInRoute.USERNAME_ATTR, "aNotLoggedInUser");
            testHelper.assertViewModelAttribute(GetHomeRoute.CURRENT_PLAYER, "aNotLoggedInUser");
            //testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST, );

            testHelper.assertViewName(GetHomeRoute.VIEW_NAME);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test PostSignInRoute with an invalid username
     */
    @Test
    public void signInInvalidTest() {

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(request.queryParams(eq(PostSignInRoute.USERNAME_ATTR))).thenReturn("!**invalidUser!");

        try {
            CuT.handle(request, response);

            testHelper.assertViewModelExists();
            testHelper.assertViewModelIsaMap();

            testHelper.assertViewModelAttribute(GetSignInRoute.TITLE_ATTR, GetSignInRoute.TITLE);
            testHelper.assertViewModelAttribute(GetSignInRoute.SIGNIN_MSG_ATTR, PostSignInRoute.INVALID_NAME);

            testHelper.assertViewName(GetSignInRoute.VIEW_NAME);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test PostSignInRoute with an already existing, logged in username
     */
    @Test
    public void signInExistingUserErrorTest() {

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(request.queryParams(eq(PostSignInRoute.USERNAME_ATTR))).thenReturn("aLoggedInUser"); //this was already initialized

        try {
            CuT.handle(request, response);

            testHelper.assertViewModelExists();
            testHelper.assertViewModelIsaMap();

            testHelper.assertViewModelAttribute(GetSignInRoute.TITLE_ATTR, GetSignInRoute.TITLE);
            testHelper.assertViewModelAttribute(GetSignInRoute.SIGNIN_MSG_ATTR, PostSignInRoute.ACCT_SIGNED_IN);

            testHelper.assertViewName(GetSignInRoute.VIEW_NAME);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
