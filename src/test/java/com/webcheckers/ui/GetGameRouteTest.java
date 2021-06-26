package com.webcheckers.ui;

import com.webcheckers.application.GameCenter;
import com.webcheckers.model.Board;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import org.junit.jupiter.api.*;
import java.util.HashMap;

import static org.mockito.Mockito.*;

/**
 * unit tests for get game route
 * @author Priya, Merry
 *
 */
@Tag("UI-tier")
public class GetGameRouteTest {

	private GetGameRoute CuT; //unit under test

	private Session session; //all mock
	private Request request;
	private Response response;
	private TemplateEngine engine;

	private GameCenter gameCenter; //mock
	private Board gameBoard; //real
	private String newGameID; //real
	private Game game; //real
	private Player player1; //real
	private Player player2; //real
	private Player player3; //real
	public String PLAYERNAME = "player1";
	public String OPPNAME = "player2";
	public String THIRDNAME = "player3";
	HashMap<String,Player> userDataBase; //stub hashmap substitute

	@BeforeEach
	public void setup() {
		request = mock(Request.class);
		session = mock(Session.class);
		when(request.session()).thenReturn(session);
		when(session.attribute(GetGameRoute.CURRENT_PLAYER)).thenReturn(PLAYERNAME);
        engine = mock(TemplateEngine.class);
		response = mock(Response.class);

        gameCenter = mock(GameCenter.class);

		userDataBase = new HashMap<>();
		player1 = new Player(PLAYERNAME);
		player2 = new Player(OPPNAME);
		player3 = new Player(THIRDNAME);
		userDataBase.put(PLAYERNAME, player1);
		userDataBase.put(OPPNAME, player2);
		userDataBase.put(THIRDNAME, player3);

		gameCenter = mock(GameCenter.class);
		when(session.attribute(GetGameRoute.CURRENT_PLAYER)).thenReturn(player1.getUsername());
		when(gameCenter.getPlayer(eq(PLAYERNAME))).thenReturn(player1);

		CuT = new GetGameRoute(gameCenter, engine);
	}

	/**
	 * Test GetGameRoute, creating a new game, opponent is also free
	 */
	@Test
	public void new_game() throws Exception {
		final TemplateEngineTester testHelper = new TemplateEngineTester();
		when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

		//Note: currentPlayer (aka "player1") should not in a game at this point because game hasn't started
		when(request.queryParams("opponent")).thenReturn(OPPNAME);
		when(gameCenter.getPlayer(eq(OPPNAME))).thenReturn(player2);


        //creation of a new game and set up logic to connect to mock gameCenter
        game = new Game(player1, player2);
        newGameID = game.getGameID();
        gameBoard = game.getBoard();

        when(gameCenter.startGame(PLAYERNAME, OPPNAME)).thenReturn(newGameID);
        when(gameCenter.getGame(anyString())).thenReturn(game);

		try {
			CuT.handle(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}

		testHelper.assertViewModelExists();
		testHelper.assertViewModelIsaMap();

		testHelper.assertViewModelAttribute(GetGameRoute.TITLE_ATTR, GetGameRoute.TITLE);
		testHelper.assertViewModelAttribute(GetGameRoute.GAMEID, newGameID);
		testHelper.assertViewModelAttribute(GetGameRoute.CURRENT_PLAYER, PLAYERNAME);
		testHelper.assertViewModelAttribute(GetGameRoute.VIEWMODE, "PLAY");
		testHelper.assertViewModelAttribute(GetGameRoute.REDPLAYER, game.getRedPlayer().getUsername());
		testHelper.assertViewModelAttribute(GetGameRoute.WHITEPLAYER, game.getWhitePlayer().getUsername());
		testHelper.assertViewModelAttribute(GetGameRoute.ACTIVECOLOR, game.getActiveColor());
		testHelper.assertViewModelAttribute(GetGameRoute.GAMESTATE, game.getBoard().getBoard(player1));

		testHelper.assertViewName(GetGameRoute.VIEW_NAME);
	}

	/**
	 * Test GetGameRoute, trying to enter a game with another user who is already in a game
	 */
	@Test
	public void opponent_ingame() {
		final TemplateEngineTester testHelper = new TemplateEngineTester();
		when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

		//suppose that a game is already happening between players 2 and 3
		game = new Game(player2, player3);
		player2.setGameID(game.getGameID());
		player3.setGameID(game.getGameID());

		//Note: currentPlayer (aka "player1") should not in a game at this point because game hasn't started
		when(request.queryParams("opponent")).thenReturn(OPPNAME);
		when(gameCenter.getPlayer(eq(OPPNAME))).thenReturn(player2);
		when(gameCenter.getUserDatabase()).thenReturn(userDataBase);

		try {
			CuT.handle(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}

		testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE);
		testHelper.assertViewModelAttribute(GetHomeRoute.ERROR_MESSAGE_ATTR, "Error: Player in game.");
		testHelper.assertViewModelAttribute(GetHomeRoute.CURRENT_PLAYER, PLAYERNAME);
		testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST, userDataBase.keySet());

		testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
	}

	/**
	 * Test GetGameRoute, when user is already in a game and the game is not over
	 */
	@Test
	public void user_ingame_not_finished() {
		final TemplateEngineTester testHelper = new TemplateEngineTester();
		when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

		when(request.queryParams("opponent")).thenReturn(OPPNAME);
		when(gameCenter.getPlayer(eq(OPPNAME))).thenReturn(player2);

		//set up substitute game instance such that both players are already in it
		game = new Game(player1, player2);
		newGameID = game.getGameID();
		gameBoard = game.getBoard();
		player1.setGameID(newGameID);
		player2.setGameID(newGameID);

		when(gameCenter.getGameID(eq(PLAYERNAME))).thenReturn(newGameID);
		when(gameCenter.getGame(anyString())).thenReturn(game);

		try {
			CuT.handle(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}

		testHelper.assertViewModelAttribute(GetGameRoute.TITLE_ATTR, GetGameRoute.TITLE);
		testHelper.assertViewModelAttribute(GetGameRoute.GAMEID, newGameID);
		testHelper.assertViewModelAttribute(GetGameRoute.CURRENT_PLAYER, PLAYERNAME);
		testHelper.assertViewModelAttribute(GetGameRoute.VIEWMODE, "PLAY");
		testHelper.assertViewModelAttribute(GetGameRoute.REDPLAYER, game.getRedPlayer().getUsername());
		testHelper.assertViewModelAttribute(GetGameRoute.WHITEPLAYER, game.getWhitePlayer().getUsername());
		testHelper.assertViewModelAttribute(GetGameRoute.ACTIVECOLOR, game.getActiveColor());
		testHelper.assertViewModelAttribute(GetGameRoute.GAMESTATE, game.getBoard().getBoard(player1));

		testHelper.assertViewName(GetGameRoute.VIEW_NAME);
	}

	/**
	 * Test GetGameRoute, when user was already in a game, and the game IS over. (the opponent resigned)
	 */
	@Test
	public void user_ingame_finished_by_opponent() {
		final TemplateEngineTester testHelper = new TemplateEngineTester();
		when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

		//Note: currentPlayer (aka "player1") should not in a game at this point because game hasn't started
		when(request.queryParams("opponent")).thenReturn(OPPNAME);
		when(gameCenter.getPlayer(eq(OPPNAME))).thenReturn(player2);

		//no way to set up real game and have it end immediately -> mocking game this time. block doesn't need details
		game = mock(Game.class);
		player1.setGameID("mockGameID");
		player2.setGameID("mockGameID");

		when(gameCenter.getGameID(eq(PLAYERNAME))).thenReturn("mockGameID");
		when(gameCenter.getGame(anyString())).thenReturn(game);
		when(game.isOver()).thenReturn(true);
		when(game.opponentResigned()).thenReturn(true);
		when(gameCenter.getUserDatabase()).thenReturn(userDataBase);

		try {
			CuT.handle(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}

		testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE);
		testHelper.assertViewModelAttribute(GetHomeRoute.CURRENT_PLAYER, PLAYERNAME);
		testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST, userDataBase.keySet());
		testHelper.assertViewModelAttribute(GetHomeRoute.IN_GAME_ATTR, "true");
		testHelper.assertViewModelAttribute(GetHomeRoute.END_GAME_MESSAGE_ATTR, "Opponent resigned.");

		testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
	}

	/**
	 * Test GetGameRoute, when user was already in a game, and the game IS over. (the opponent resigned)
	 */
	@Test
	public void user_ingame_finished_by_user() {
		final TemplateEngineTester testHelper = new TemplateEngineTester();
		when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

		//Note: currentPlayer (aka "player1") should not in a game at this point because game hasn't started
		when(request.queryParams("opponent")).thenReturn(OPPNAME);
		when(gameCenter.getPlayer(eq(OPPNAME))).thenReturn(player2);

		//no way to set up real game and have it end immediately -> mocking game this time. block doesn't need details
		game = mock(Game.class);
		player1.setGameID("mockGameID");
		player2.setGameID("mockGameID");

		when(gameCenter.getGameID(eq(PLAYERNAME))).thenReturn("mockGameID");
		when(gameCenter.getGame(anyString())).thenReturn(game);
		when(game.isOver()).thenReturn(true);
		when(game.opponentResigned()).thenReturn(false);
		when(gameCenter.getUserDatabase()).thenReturn(userDataBase);

		try {
			CuT.handle(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}

		testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE);
		testHelper.assertViewModelAttribute(GetHomeRoute.CURRENT_PLAYER, PLAYERNAME);
		testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST, userDataBase.keySet());
		testHelper.assertViewModelAttribute(GetHomeRoute.IN_GAME_ATTR, "true");
		testHelper.assertViewModelAttribute(GetHomeRoute.END_GAME_MESSAGE_ATTR, "You lost.");

		testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
	}
}