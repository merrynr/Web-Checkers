package com.webcheckers.ui;

import com.webcheckers.application.GameCenter;

import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import spark.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The unit test suite for the {@link GetReplayMenuRoute} component.
 *
 * @author Priya, Merry
 */
@Tag("UI-tier")
public class GetReplayMenuRouteTest {
    private GetReplayMenuRoute CuT; //unit under test

    private Session session; //all mock
    private Request request;
    private Response response;
    private TemplateEngine engine;

    public String PLAYERNAME = "one";
    private Player player;
    private ArrayList<Game> gamesList; //substitute item

    private GameCenter gameCenter; //mock

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        engine = mock(TemplateEngine.class);
        response = mock(Response.class);

        gameCenter = mock(GameCenter.class);
        player = mock(Player.class);
        gamesList = new ArrayList<>();
        //Setup

        CuT = new GetReplayMenuRoute(gameCenter, engine);
    }

    @Test
    public void unsuccessful_redirect() { //handle redirect block (should be working)
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute(GetGameRoute.CURRENT_PLAYER)).thenReturn(null);

        try {
            CuT.handle(request, response);
            fail("Redirects invoke halt exceptions.");
        } catch (HaltException e) {
            // expected
        } catch (Exception e) {
            e.printStackTrace();
        }

        verify(response).redirect(WebServer.HOME_URL);
    }

    @Test
    public void successful_menu() { //TODO: setup and attributes
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute(GetGameRoute.CURRENT_PLAYER)).thenReturn(PLAYERNAME);

        when(gameCenter.getPlayer(anyString())).thenReturn(player);
        when(gameCenter.getFinishedGamesList()).thenReturn(gamesList);
        when(gameCenter.getNumPlayers()).thenReturn(2);

        try {
            CuT.handle(request, response);
        } catch(Exception e) {
            e.printStackTrace();
        }

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        testHelper.assertViewModelAttribute(GetReplayMenuRoute.TITLE_ATTR, GetReplayMenuRoute.TITLE);
        testHelper.assertViewModelAttribute(GetReplayMenuRoute.CURRENT_PLAYER, PLAYERNAME);
        testHelper.assertViewModelAttribute(GetReplayMenuRoute.GAME_LIST, gamesList);
        testHelper.assertViewModelAttribute(GetReplayMenuRoute.NUMBER_OF_PLAYERS_ATTR, 2);

        testHelper.assertViewName(GetReplayMenuRoute.VIEW_NAME);
    }
}
