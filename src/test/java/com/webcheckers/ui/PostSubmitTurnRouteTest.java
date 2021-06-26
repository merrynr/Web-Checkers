package com.webcheckers.ui;
import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;
import com.webcheckers.model.Game;
import com.webcheckers.model.Move;
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
 * Test Class for {@link PostSubmitTurnRoute} objects
 *
 * @author None
 */
@Tag("ui-tier")
public class PostSubmitTurnRouteTest {

    private PostSubmitTurnRoute CuT;

    private Request request; //mock
    private Session session; //mock
    private Response response; //mock
    private TemplateEngine engine; //mock

    private GameCenter gameCenter; //mock
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

        //System.out.println("Test class check invocation gameID: " + gameCenter.getGameID("p1"));
        //System.out.println("Test class check invocation game: " + gameCenter.getGame("oeoeoe"));

        CuT = new PostSubmitTurnRoute(gameCenter, engine);
    }

    @Test
    public void wrong_Direction() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(game.submitTurn()).thenReturn(Move.MOVETYPE.WRONGDIRECTION);

        try {
            String result = (String)CuT.handle(request, response);
            //System.out.println(result); //FIXME: how to get JSON
            assertEquals("{\"text\":\"Can not move a non-king piece backwards.\",\"type\":\"ERROR\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void invalid_chain() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(game.submitTurn()).thenReturn(Move.MOVETYPE.INVALIDCHAIN);

        try {
            String result = (String)CuT.handle(request, response);
            //System.out.println(result);
            assertEquals("{\"text\":\"Cannot perform a take after jumping a single square.\",\"type\":\"ERROR\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void invalid_end() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(game.submitTurn()).thenReturn(Move.MOVETYPE.INVALIDEND);

        try {
            String result = (String)CuT.handle(request, response);
            //System.out.println(result);
            assertEquals("{\"text\":\"Invalid drop location.\",\"type\":\"ERROR\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void no_piece() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(game.submitTurn()).thenReturn(Move.MOVETYPE.NOPIECE);

        try {
            String result = (String)CuT.handle(request, response);
            //System.out.println(result);
            assertEquals("{\"text\":\"There must be a piece for you to take.\",\"type\":\"ERROR\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void self_take() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(game.submitTurn()).thenReturn(Move.MOVETYPE.SELFTAKE);

        try {
            String result = (String)CuT.handle(request, response);
            //System.out.println(result);
            assertEquals("{\"text\":\"Cannot take your own piece.\",\"type\":\"ERROR\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void single_first() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(game.submitTurn()).thenReturn(Move.MOVETYPE.SINGLEFIRST);

        try {
            String result = (String)CuT.handle(request, response);
            //System.out.println(result);
            assertEquals("{\"text\":\"Cannot move a single square after taking a piece.\",\"type\":\"ERROR\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void better_move() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(game.submitTurn()).thenReturn(Move.MOVETYPE.BETTERMOVE);

        try {
            String result = (String)CuT.handle(request, response);
            System.out.println(result);
            assertEquals("{\"text\":\"Must take jump move\",\"type\":\"ERROR\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void end_occupied() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(game.submitTurn()).thenReturn(Move.MOVETYPE.ENDOCCUPIED);

        try {
            String result = (String)CuT.handle(request, response);
            System.out.println(result);
            assertEquals("{\"text\":\"Drop point is occupied\",\"type\":\"ERROR\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void partial_multi() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(game.submitTurn()).thenReturn(Move.MOVETYPE.PARTIALMULTI);

        try {
            String result = (String)CuT.handle(request, response);
            //System.out.println(result);
            assertEquals("{\"text\":\"Must take next jump\",\"type\":\"ERROR\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void valid() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(game.submitTurn()).thenReturn(Move.MOVETYPE.VALID);

        try {
            String result = (String)CuT.handle(request, response);
            //System.out.println(result);
            assertEquals("{\"text\":\"\",\"type\":\"info\"}", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
