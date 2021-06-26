package com.webcheckers.model;

//import org.junit.Test;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The unit test suite for the Square class.
 *
 * @author Dante Secada-Oz
 */
public class GameTest {

    /** Test that getPlayer() returns the the correct player after game construction. */
    @Test
    public void test_game_constructor_sets_active_player() {
        Player testPlayerOne = new Player("test_player_one");
        Player testPlayerTwo = new Player("test_player_two");
        Game testGame = new Game(testPlayerOne, testPlayerTwo);

        assertEquals(testPlayerOne, testGame.getPlayer(),"getPlayer() failed to correct player.");
    }

    /** Test that getGameID returns the game id generated in the Game constructor. */
    @Test
    public void test_get_game_id() {
        Player testPlayerOne = new Player("test_player_one");
        Player testPlayerTwo = new Player("test_player_two");
        Game testGame = new Game(testPlayerOne, testPlayerTwo);

        assertNotNull(testGame.getGameID(),"getGameID() failed to return a gameID.");
    }

    /** Test that getPlayersGame. */
    /*@Test
    public void test_get_players_game() {
        Player testPlayerOne = new Player("test_player_one");
        Player testPlayerTwo = new Player("test_player_two");
        Game testGame = new Game(testPlayerOne, testPlayerTwo);

        assertNotNull(testGame.getBoard(testPlayerOne),
                "getBoard() failed to return an non-null object for test_player_one's CuT1.");
        assertNotNull(testGame.getBoard(testPlayerTwo),
                "getBoard() failed to return an non-null object for test_player_two's CuT1.");


        assertEquals(testPlayerOne, testGame.getBoard(testPlayerOne).getPlayer(),
                "getBoard() failed to return a properly formed CuT1 for test_player_one.");
        assertEquals(testPlayerTwo, testGame.getBoard(testPlayerTwo).getPlayer(),
                "getBoard() failed to return a properly formed CuT1 for test_player_two.");
    }*/

    /** Test that Game constructor correctly assigns players to boards. */
    @Test
    public void test_game_constructor_creates_valid_boards_assignments() {
        Player testPlayerOne = new Player("test_player_one");
        Player testPlayerTwo = new Player("test_player_two");
        Game testGame = new Game(testPlayerOne, testPlayerTwo);

        // Correct PLAYER returned for PLAYER COLOR GETTERS
            // Player One Board

        assertEquals(testPlayerOne, testGame.getRedPlayer(),
                "getRedPlayer() from test_player_one CuT1 failed to return correct RED PLAYER.");
        assertEquals(testPlayerTwo, testGame.getWhitePlayer(),
                "getWhitePlayer() from test_player_one CuT1 failed to return correct WHITE PLAYER.");


        /*
            // Player Two Board
        assertEquals(testPlayerOne, testGame.getBoard(testPlayerTwo).getRedPlayer(),
                "getRedPlayer() from test_player_two CuT1 failed to return correct RED PLAYER.");
        assertEquals(testPlayerTwo, testGame.getBoard(testPlayerTwo).getWhitePlayer(),
                "getWhitePlayer() from test_player_two CuT1 failed to return correct WHITE PLAYER.");
        */
    }

}
