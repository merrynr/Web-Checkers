package com.webcheckers.ui;

import com.webcheckers.application.GameCenter;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test Class for {@link PostCheckTurnRoute} objects
 *
 * @author None
 */
@Tag("ui-tier")
public class PostCheckTurnRouteTest {

    private PostCheckTurnRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;

    private GameCenter gameCenter;
    private Game game; //mock

    private String newGameID; //needed for handle block

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        engine = mock(TemplateEngine.class);
        response = mock(Response.class);

        gameCenter = mock(GameCenter.class);

        game = mock(Game.class);
        newGameID = "randomID";

        when(session.attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn("p1");
        when(gameCenter.getGameID(anyString())).thenReturn(newGameID);
        when(gameCenter.getGame(anyString())).thenReturn(game);

        CuT = new PostCheckTurnRoute(gameCenter, engine);
    }

    @Test
    public void test_isTurn() {
        when(game.isTurn(any(Player.class))).thenReturn(true);

        try {
            String result = (String)CuT.handle(request, response);
            assertEquals("{\"text\":\"false\",\"type\":\"info\"}", result);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_oppResigned() {
        when(game.isTurn(any(Player.class))).thenReturn(false);
        when(game.opponentResigned()).thenReturn(true);

        try {
            String result = (String)CuT.handle(request, response);
            assertEquals("{\"text\":\"true\",\"type\":\"info\"}", result);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_notTurn() {
        when(game.isTurn(any(Player.class))).thenReturn(false);
        when(game.opponentResigned()).thenReturn(false);

        try {
            String result = (String)CuT.handle(request, response);
            assertEquals("{\"text\":\"false\",\"type\":\"info\"}", result);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
