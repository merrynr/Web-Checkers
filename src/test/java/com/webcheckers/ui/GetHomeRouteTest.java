package com.webcheckers.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.webcheckers.model.Game;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.webcheckers.application.GameCenter;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import java.util.ArrayList;

/**
     * The unit test suite for the {@link GetHomeRoute} component.
     *
     * @author Anthony Cianfrocco, Merry
     */
    @Tag("UI-tier")
public class GetHomeRouteTest {

        /**
         * The component-under-test (CuT).
         *
         * <p>
         * This is a stateless component so we only need one.
         * The {@link GameCenter} component is thoroughly tested so
         * we can use it safely as a "friendly" dependency.
         */
        private GetHomeRoute CuT;

        // mock objects
        private Request request;
        private Session session;
        private TemplateEngine engine;
        private Response response;

        private GameCenter gameCenter; //mock
        private Player one; //real
        private Player two; //real
        private Game game; //real
        private String gameId; //real
        private ArrayList<String> activeUserList; //substitute stub object
        /**
         * Setup new mock objects for each test.
         */
        @BeforeEach
        public void setup() {
            request = mock(Request.class);
            session = mock(Session.class);
            when(request.session()).thenReturn(session);
            response = mock(Response.class);
            engine = mock(TemplateEngine.class);

            // create a unique CuT for each test
            // the GameCenter is friendly but the engine mock will need configuration
            gameCenter = mock(GameCenter.class);
            one = new Player("one");
            two = new Player("two");

            //active user list to be returned cases
            activeUserList = new ArrayList<>();
            activeUserList.add("one");
            activeUserList.add("two");

            game = new Game(one, two);
            gameId = game.getGameID();

            CuT = new GetHomeRoute(gameCenter, engine);
        }

        /**
         * Test that CuT shows the Home view when the session is brand new.
         */
        @Test
        public void new_session() {
            final TemplateEngineTester testHelper = new TemplateEngineTester();
            when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
            //when(request.queryParams(eq(session.attribute(GetHomeRoute.CURRENT_PLAYER)))).thenReturn(Null);

            try {
                CuT.handle(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }

            testHelper.assertViewModelExists();
            testHelper.assertViewModelIsaMap();

            testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE);
            testHelper.assertViewModelAttribute(GetHomeRoute.CURRENT_PLAYER, session.attribute(GetHomeRoute.CURRENT_PLAYER)); //null
            testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST, gameCenter.getActiveUserList());
            testHelper.assertViewModelAttribute(GetHomeRoute.NUMBER_OF_PLAYERS_ATTR, gameCenter.getNumPlayers());
            testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
        }

        /**
         * Test that CuT shows the Home view when the session is not brand new but a new game was created.
         */
        @Test
        public void current_session_new_game() {
            final TemplateEngineTester testHelper = new TemplateEngineTester();
            when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
            when(session.attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn("one");

            one.setGameID(gameId);
            two.setGameID(gameId);

            when(gameCenter.getPlayer(anyString())).thenReturn(one);
            when(gameCenter.gameJustStarted()).thenReturn(true);
            when(gameCenter.getGameID(anyString())).thenReturn(gameId);
            when(gameCenter.getGame(eq(gameId))).thenReturn(game);

            try {
                CuT.handle(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }

            testHelper.assertViewModelExists();
            testHelper.assertViewModelIsaMap();

            testHelper.assertViewModelAttribute(GetGameRoute.TITLE_ATTR, GetGameRoute.TITLE);
            testHelper.assertViewModelAttribute(GetGameRoute.GAMEID, gameCenter.getGameID("one"));
            testHelper.assertViewModelAttribute(GetHomeRoute.CURRENT_PLAYER, "one");
            testHelper.assertViewModelAttribute(GetGameRoute.VIEWMODE, "PLAY");
            testHelper.assertViewModelAttribute(GetGameRoute.MODEOPTIONS, "modeOptionsAsJSON!'{}'");
            testHelper.assertViewModelAttribute(GetGameRoute.REDPLAYER, "one");
            testHelper.assertViewModelAttribute(GetGameRoute.WHITEPLAYER, "two");
            testHelper.assertViewModelAttribute(GetGameRoute.ACTIVECOLOR, Piece.COLOR.RED);
            testHelper.assertViewModelAttribute(GetGameRoute.GAMESTATE, game.getBoard().getBoard(one));

            testHelper.assertViewName(GetGameRoute.VIEW_NAME);
        }

        /**
         * Test that CuT shows the Home view when the session is not brand new and nor the game.
         */
        @Test
        public void current_session_resumed_game() {
            final TemplateEngineTester testHelper = new TemplateEngineTester();
            when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
            when(session.attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn("one");

            one.setGameID(gameId); //player status becomes in-game
            two.setGameID(gameId); //opponent status becomes in-game

            when(gameCenter.getPlayer(anyString())).thenReturn(one);
            when(gameCenter.gameJustStarted()).thenReturn(false);
            when(gameCenter.getActiveUserList()).thenReturn(activeUserList);

            try {
                CuT.handle(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }

            testHelper.assertViewModelExists();
            testHelper.assertViewModelIsaMap();

            testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE);
            testHelper.assertViewModelAttribute(GetHomeRoute.CURRENT_PLAYER, session.attribute(GetHomeRoute.CURRENT_PLAYER)); //null
            testHelper.assertViewModelAttribute(GetHomeRoute.GAME_ATTR, "true");
            testHelper.assertViewModelAttribute(GetHomeRoute.IN_GAME_ATTR, "true");
            testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST, activeUserList); //mocked gameCenter so shouldn't exist
            testHelper.assertViewModelAttribute(GetHomeRoute.NUMBER_OF_PLAYERS_ATTR, gameCenter.getNumPlayers());

            testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
        }
}

