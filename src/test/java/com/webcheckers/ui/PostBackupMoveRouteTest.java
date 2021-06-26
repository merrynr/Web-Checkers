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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test Class for {@link PostSubmitTurnRoute} objects
 *
 * @author None
 */
@Tag("ui-tier")
public class PostBackupMoveRouteTest {

    private PostBackupMoveRoute CuT;

    private Request request; //mock
    private Session session; //mock
    private Response response; //mock
    private TemplateEngine engine; //mock

    private GameCenter gameCenter; //mock
    private Game game; //mock
    private Player p1; //mock

    private String newGameID; //needed for handle block

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        engine = mock(TemplateEngine.class);
        response = mock(Response.class);

        gameCenter = mock(GameCenter.class);
        p1 = mock(Player.class);
        game = mock(Game.class);
        newGameID = "randomID";

        when(session.attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn("p1");
        when(gameCenter.getGameID(anyString())).thenReturn(newGameID);
        when(gameCenter.getGame(anyString())).thenReturn(game);
        when(game.getPlayer()).thenReturn(p1);

        //System.out.println("Test class check invocation gameID: " + gameCenter.getGameID("p1"));
        //System.out.println("Test class check invocation game: " + gameCenter.getGame("oeoeoe"));

        CuT = new PostBackupMoveRoute(gameCenter, engine);
    }

    @Test
    public void test_valid() {
        when(game.isTurn(any(Player.class))).thenReturn(true);
        when(game.backupMove()).thenReturn(true);

        try {
            String result = (String)CuT.handle(request, response);
            //System.out.println(result);
            assertEquals("{\"text\":\"Reverted piece back to last move\",\"type\":\"info\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_invalid() {
        when(game.isTurn(any(Player.class))).thenReturn(true);
        when(game.backupMove()).thenReturn(false);

        try {
            String result = (String)CuT.handle(request, response);
            //System.out.println(result);
            assertEquals("{\"text\":\"Cannot undo a move if there are none made yet\",\"type\":\"info\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_not_turn() {
        when(game.isTurn(any(Player.class))).thenReturn(false);
        //when(game.backupMove()).thenReturn(false);

        try {
            String result = (String)CuT.handle(request, response);
            //System.out.println(result);
            assertEquals("{\"text\":\"Cannot undo a move if it is not your turn\",\"type\":\"ERROR\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
