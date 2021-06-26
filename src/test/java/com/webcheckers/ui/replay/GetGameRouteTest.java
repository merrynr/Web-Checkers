package com.webcheckers.ui.replay;

import com.webcheckers.application.GameCenter;
import com.webcheckers.model.Game;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.model.ReplayBoard;
import com.webcheckers.ui.TemplateEngineTester;
import com.webcheckers.ui.WebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetGameRouteTest {
    private GetGameRoute CuT; //instance created for testing
    private GameCenter gameCenter; //mock

    // mock objects
    private Request request; //mock
    private Session session; //mock
    private TemplateEngine engine; //mock
    private Response response; //mock

    private Player p1; //mock needed for class logic
    private Player p2;
    private Game game; //mock
    private String gameID; //mock
    private ReplayBoard replayBoard; //mock

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);

        gameCenter = mock(GameCenter.class);
        replayBoard = mock(ReplayBoard.class);
        p1 = new Player("mockPlayer");
        p2 = new Player("mockOpponent");
        p1.setReplayBoard(replayBoard);
        game = mock(Game.class);
        gameID = "randomID";

        //when(session.attribute(GetGameRoute.CURRENT_PLAYER)).thenReturn("mockPlayer");
        //when(gameCenter.getGameID(anyString())).thenReturn(gameID);
        //when(gameCenter.getGame(anyString())).thenReturn(game);
        //when(gameCenter.getPlayer(anyString())).thenReturn(p1);
        //to checkpoint 1

        CuT = new GetGameRoute(gameCenter, engine);
    }

    @Test
    public void error_case_no_game() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        when(session.attribute(GetGameRoute.CURRENT_PLAYER)).thenReturn("mockPlayer");
        when(gameCenter.getGameID(anyString())).thenReturn(null); //GameID = null

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
    public void error_case_not_loggedIn() {
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
    public void Sucessfully_load_menu() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        when(session.attribute(GetGameRoute.CURRENT_PLAYER)).thenReturn("mockPlayer");
        when(request.queryParams(eq("gameID"))).thenReturn(gameID);
        when(gameCenter.getGame(anyString())).thenReturn(game);
        when(gameCenter.getPlayer(anyString())).thenReturn(p1);

        when(replayBoard.hasNext()).thenReturn(false);
        when(replayBoard.hasPrev()).thenReturn(false);

        when(game.getRedPlayer()).thenReturn(p1);
        when(game.getWhitePlayer()).thenReturn(p2);
        when(replayBoard.getActiveColor()).thenReturn(Piece.COLOR.RED);

        try {
            CuT.handle(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        testHelper.assertViewModelAttribute(GetGameRoute.TITLE_ATTR, GetGameRoute.TITLE);
        testHelper.assertViewModelAttribute(GetGameRoute.CURRENT_PLAYER, "mockPlayer");
        testHelper.assertViewModelAttribute(GetGameRoute.REDPLAYER, p1.getUsername());
        testHelper.assertViewModelAttribute(GetGameRoute.WHITEPLAYER, p2.getUsername());
        testHelper.assertViewModelAttribute(GetGameRoute.ACTIVECOLOR, Piece.COLOR.RED);

        testHelper.assertViewName(GetGameRoute.VIEW_NAME);
    }
}
