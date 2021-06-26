package com.webcheckers.ui;

import com.google.gson.*;
import com.webcheckers.application.GameCenter;
import com.webcheckers.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test Class for {@link PostSignInRoute} objects
 *
 * @author None
 */
@Tag("ui-tier")
public class PostValidateMoveRouteTest {

    private PostValidateMoveRoute CuT;

    private Request request; //mock
    private Session session; //mock
    private Response response; //mock
    private TemplateEngine engine; //mock

    private GameCenter gameCenter; //mock
    private Game game; //mock

    private String newGameID; //needed for handle block


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

        newGameID = "randomID";
        game = mock(Game.class);//new Game(p1, p2);
        gameCenter = mock(GameCenter.class);

        when(session.attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn("p1");
        when(gameCenter.getGameID(anyString())).thenReturn(newGameID);
        when(gameCenter.getGame(anyString())).thenReturn(game);

        when(request.body()).thenReturn("{\"start\":{\"row\":5,\"cell\":6},\"end\":{\"row\":4,\"cell\":7}}");

        CuT = new PostValidateMoveRoute(gameCenter, engine);
    }

    @Test
    public void test_wrongDirection() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        when(game.validateMove(5,6,4,7)).thenReturn(Move.MOVETYPE.WRONGDIRECTION);

        try {
            String result = (String)CuT.handle(request, response);
            //System.out.println(result);
            assertEquals("{\"text\":\"Can not move a non-king piece backwards.\",\"type\":\"ERROR\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_invalidChain() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        when(game.validateMove(5,6,4,7)).thenReturn(Move.MOVETYPE.INVALIDCHAIN);

        try {
            String result = (String)CuT.handle(request, response);
            //System.out.println(result);
            assertEquals("{\"text\":\"Cannot perform a take after jumping a single square.\",\"type\":\"ERROR\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_invalidEnd() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        when(game.validateMove(5,6,4,7)).thenReturn(Move.MOVETYPE.INVALIDEND);

        try {
            String result = (String)CuT.handle(request, response);
            //System.out.println(result);
            assertEquals("{\"text\":\"Invalid drop location.\",\"type\":\"ERROR\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_noPiece() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        when(game.validateMove(5,6,4,7)).thenReturn(Move.MOVETYPE.NOPIECE);

        try {
            String result = (String)CuT.handle(request, response);
            //System.out.println(result);
            assertEquals("{\"text\":\"There must be a piece for you to take.\",\"type\":\"ERROR\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_selfTake() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        when(game.validateMove(5,6,4,7)).thenReturn(Move.MOVETYPE.SELFTAKE);

        try {
            String result = (String)CuT.handle(request, response);
            //System.out.println(result);
            assertEquals("{\"text\":\"Cannot take your own piece.\",\"type\":\"ERROR\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_singleFirst() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        when(game.validateMove(5,6,4,7)).thenReturn(Move.MOVETYPE.SINGLEFIRST);

        try {
            String result = (String)CuT.handle(request, response);
            //System.out.println(result);
            assertEquals("{\"text\":\"Cannot move a single square after taking a piece.\",\"type\":\"ERROR\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_single() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        when(game.validateMove(5,6,4,7)).thenReturn(Move.MOVETYPE.SINGLE);

        try {
            String result = (String)CuT.handle(request, response);
            assertEquals("{\"text\":\"\",\"type\":\"info\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_take() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        when(game.validateMove(5,6,4,7)).thenReturn(Move.MOVETYPE.TAKE);

        try {
            String result = (String)CuT.handle(request, response);
            assertEquals("{\"text\":\"\",\"type\":\"info\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_betterMove() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        when(game.validateMove(5,6,4,7)).thenReturn(Move.MOVETYPE.BETTERMOVE);

        try {
            String result = (String)CuT.handle(request, response);
            assertEquals("{\"text\":\"Must take jump move\",\"type\":\"ERROR\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_endOccupied() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        when(game.validateMove(5,6,4,7)).thenReturn(Move.MOVETYPE.ENDOCCUPIED);

        try {
            String result = (String)CuT.handle(request, response);
            assertEquals("{\"text\":\"Drop point is occupied\",\"type\":\"ERROR\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
