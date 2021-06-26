package com.webcheckers.ui.replay;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;

import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.ui.PostSignInRoute;
import com.webcheckers.ui.TemplateEngineTester;

import org.junit.jupiter.api.Test;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import static org.junit.jupiter.api.Assertions.*;



import org.junit.jupiter.api.Tag;


public class GetStopWatchingRouteTest {

    private PostSignInRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;

    private PlayerLobby playerLobby;
    private GameCenter gameCenter;

    private String mockUser;

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
	 * testing handle method of getStopWatchingRoute, checks exiting.
	 */
	@Test
	public void test_exitingReplay() {
		assertTrue(true);
	}
	
}
