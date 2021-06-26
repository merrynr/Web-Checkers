package com.webcheckers.application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.webcheckers.model.Game;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The unit test suite for the {@link GameCenter} component.
 *
 * @author Anthony Cianfrocco
 */

@Tag("Application-tier")

public class GameCenterTest {

    //keeps track of all application users
    private PlayerLobby playerLobby = new PlayerLobby();
    private GameCenter gameCenter = new GameCenter(playerLobby);
    //keeps track of number of players in the game
    private int numPlayers;

    /** Manages all games.*/
    private HashMap<String, Game> games;

    /**
     * Test the ability to make a new PlayerService.
     */
    @Test
    public void test_make_player_lobby() {
        final PlayerLobby playerLbby = new PlayerLobby();

        // Analyze results
        assertNotNull(playerLbby);
    }

    /**
     * Test the ability to add a user to the system
     */
    @Test
    public void test_add_user(){
        String testUserName = "myName";
        this.gameCenter.addUser(testUserName);

        // test if userName is in the playerLobby
        assertNotNull(this.playerLobby.getUser(testUserName));
    }

    /**
     * Test the ability to remove a user from the system
     */
    @Test
    public void test_remove_user(){
        String testUserName = "myName";
        this.gameCenter.addUser(testUserName);

        this.gameCenter.removeUser(testUserName);

        assertNull(this.playerLobby.getUser(testUserName));
    }

    /**
     * Test that when a user logs in
     * that that user's state in
     * set to logged in (true)
     */
    @Test
    public void test_set_user_logged_in() {
        String testUserName = "myName";
        this.gameCenter.addUser(testUserName);
        this.gameCenter.setUserLoggedIn(testUserName);

        // test if user login state is set to true
        assertEquals(playerLobby.checkLoggedIn(testUserName), true);

    }

    /**
     *  Test that when a user logs out
     *  that that user's state is set
     *  to logged out (false)
     */
    public void test_set_user_logged_out(){
        String testUserName = "myName";
        this.gameCenter.addUser(testUserName);
        this.gameCenter.setUserLoggedOut(testUserName);

        // test if user login state is set to true
        assertEquals(playerLobby.checkLoggedIn(testUserName), false);
    }

    /**
     *  Test the ability to check if
     *  a user exists in the system.
     */
    @Test
    public void test_check_user_exists(){
        String testUserName = "myName";
        this.gameCenter.addUser(testUserName);
        // test when username in the system
        assertTrue(this.gameCenter.checkUserExists(testUserName));
        this.gameCenter.removeUser(testUserName);
        // test when username not in system
        assertFalse(this.gameCenter.checkUserExists(testUserName));

    }

    /**
     * Test the ability to get
     * the active user list
     */
    @Test
    public void test_get_active_user_list(){
        String test1 = "name1";
        this.gameCenter.addUser(test1);
        this.gameCenter.setUserLoggedIn(test1);
        // test if the active user list is not null
        assertNotNull(this.gameCenter.getActiveUserList());
        this.gameCenter.setUserLoggedOut(test1);
        this.gameCenter.removeUser(test1);
        // test if the active user list is empty
        assertEquals(this.gameCenter.getActiveUserList().size(), 0);
    }

    /**
     * Test the ability to get verses list (empty)
     */
    @Test
    public void test_get_verses_list(){
        assertNull(this.gameCenter.getVersesList());
    }

    /**
     * Test the ability to the number of online players
     */
    @Test
    public void test_num_players(){
        assertEquals(0, this.gameCenter.getNumPlayers());
        String test1 = "name1";
        this.gameCenter.addUser(test1);
        this.gameCenter.setUserLoggedIn(test1);
        assertEquals(1, this.gameCenter.getNumPlayers());
    }

    /**
     * Test the ability to get the Hashmap
     * UserDatabase
     */
    @Test
    public void test_get_user_database(){
        String test1 = "name1";
        this.gameCenter.addUser(test1);
        // test if the active user database is not null
        assertNotNull(this.gameCenter.getUserDatabase());
        this.gameCenter.removeUser(test1);
        // test if the active user database is empty
        assertEquals(this.gameCenter.getUserDatabase().keySet().size(), 0);
    }

    /**
     * Test the ability to start a game
     */
    @Test
    public void test_start_game(){
        String playerOne = "one";
        String playerTwo = "two";
        this.gameCenter.addUser(playerOne);
        this.gameCenter.addUser(playerTwo);
        this.gameCenter.setUserLoggedIn(playerOne);
        this.gameCenter.setUserLoggedIn(playerTwo);
        String gameID = this.gameCenter.startGame(playerOne, playerTwo);
        assertNotNull(gameID);
        assertTrue(gameID.length() > 0 );
    }

    /**
     * Test the ability to get the gameboard.
     */
    @Test
    public void test_get_board(){
        String playerOne = "one";
        String playerTwo = "two";
        this.gameCenter.addUser(playerOne);
        this.gameCenter.addUser(playerTwo);
        this.gameCenter.setUserLoggedIn(playerOne);
        this.gameCenter.setUserLoggedIn(playerTwo);
        String gameId = this.gameCenter.startGame(playerOne, playerTwo);
        //Board board = this.gameCenter.getBoard(gameId, playerOne);
        //assertNotNull(board);
    }

    /**
     * test setting and getting the game just started boolean
     */
    @Test
    public void test_justStarted( ) {
    	this.gameCenter.setGameJustStarted(true);
    	assertTrue(this.gameCenter.gameJustStarted());
    }

    /**
     * check setting the game is over, and adding games to the finished list
     */
    @Test 
    public void test_finishedGames() {
        String playerOne = "one";
        String playerTwo = "two";
        this.gameCenter.addUser(playerOne);
        this.gameCenter.addUser(playerTwo);
        this.gameCenter.setUserLoggedIn(playerOne);
        this.gameCenter.setUserLoggedIn(playerTwo);
        String gameId = this.gameCenter.startGame(playerOne, playerTwo);
        
        gameCenter.getGame(gameId).isOver();
        Game game = gameCenter.getGame(gameId);
        assertFalse(gameCenter.getGame(gameId).isOver());
        ArrayList<Game> finishedGames = gameCenter.getFinishedGamesList();
        finishedGames.add(game);
        assertEquals(finishedGames.get(0), gameCenter.getGame(gameId));
    }

}
