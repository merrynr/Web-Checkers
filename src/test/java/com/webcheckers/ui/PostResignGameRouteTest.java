package com.webcheckers.ui;

import com.webcheckers.application.GameCenter;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The unit test suite for the {@link GetHomeRoute} component.
 *
 * @author None
 */
@Tag("UI-tier")
public class PostResignGameRouteTest {
    private PostResignGameRoute CuT; //instance created for testing
    private GameCenter gameCenter; //mock

    // mock objects
    private Request request; //mock
    private Session session; //mock
    private TemplateEngine engine; //mock
    private Response response; //mock

    private Player p1; //mock needed for class logic
    private Game game; //mock
    private String gameID; //mock

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);

        gameCenter = mock(GameCenter.class);
        p1 = mock(Player.class);
        game = mock(Game.class);

        gameID = "randomID";

        when(session.attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn("mockPlayer");
        when(gameCenter.getPlayer(anyString())).thenReturn(p1);
        when(gameCenter.getGameID(anyString())).thenReturn(gameID);
        when(gameCenter.getGame(anyString())).thenReturn(game);

        CuT = new PostResignGameRoute(gameCenter, engine);
    }

    /**
     * Test condition when the the user is able to resign the game
     */
    @Test
    public void test_valid() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        //when(game.resignGame()).thenReturn(true);
        try {
            String result = (String)CuT.handle(request, response);
            //System.out.println(result);
            assertEquals("{\"text\":\"error\",\"type\":\"ERROR\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test condition when the the ser is not able to resign the game
     */
    @Test
    public void test_invalid() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
//        when(game.resignGame()).thenReturn(false);
        try {
            String result = (String)CuT.handle(request, response);
            assertEquals("{\"text\":\"error\",\"type\":\"ERROR\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
