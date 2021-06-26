package com.webcheckers.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.webcheckers.model.Game;
import com.webcheckers.model.Player;

@Tag("Appl-tier")
/**
 * testing class for PlayerLobby. Might need more tests.
 * @author priya
 *
 */

public class PlayerLobbyTest {
	
	private PlayerLobby UuT; //unit under test

	private static final String tNm1 = "player1";
	private static final String tNm2 = "player2";
	private static final String testValidName = "aNewUser";
	private static final String testEmptyName = " ";
	private static final String testInvalidName = "BadUser!";
	private static final String testExistingName = "player1"; //also invalid
	private static final String noSuchName = "doesNotExist";
	private Player player1;
	private Player player2;
	private HashMap<String,Player> userList; 
	private ArrayList<String> activeUserList;
	private GameCenter gameCenter;


	@BeforeEach
	public void setup() {

		UuT =  new PlayerLobby();

		//Create mock player objects
		player1 = new Player(tNm1);
		player2 = new Player(tNm2);
		HashMap<String, Player> userDatabase = new HashMap<String, Player>();
		ArrayList<String> activeUserlist = new ArrayList<String>();
		UuT.addUser(tNm1);
		UuT.addUser(tNm2);
		gameCenter = new GameCenter(UuT);
		
	}

	@Test
	public void test_addUser() {

		//True case
		UuT.addUser(testValidName);
		userList = UuT.getUserDatabase();
		assertTrue(userList.containsKey(testValidName)); 
		//If the hashmap has the username in there as a key, this will return true

		//False case: invalid username
		UuT.addUser(testInvalidName);
		assertFalse(userList.containsKey(testInvalidName));

		// /False case: empty name whitespace
		UuT.addUser(testEmptyName);
		assertFalse(userList.containsKey(testInvalidName));


		//False case: existing username
		UuT.addUser(testExistingName);
		assertTrue(userList.containsKey(testExistingName));

	}
	
	@Test
	public void test_removeUser() {
		userList = UuT.getUserDatabase();
		userList.put(tNm1, player1); //add user to userlist
		UuT.removeUser(tNm1);
		assertNull(userList.get(tNm1)); //user no longer there
		
	}

	@Test
	public void test_checkUserExists() {
		assertTrue(UuT.checkUserExists(tNm1)); //checking player exists
		assertTrue(UuT.checkUserExists(tNm2)); //checking player exists
		assertFalse(UuT.checkUserExists(testInvalidName)); //invalid
	}

	@Test
	public void test_checkLoggedIn() {
		//pre-check
		assertFalse(UuT.checkLoggedIn(tNm1));
		assertFalse(UuT.checkLoggedIn(tNm2));
	}

	@Test
	public void test_getUser() {
		assertEquals(UuT.getUser(tNm1).getUsername(), tNm1);
		assertEquals(UuT.getUser(tNm2).getUsername(), tNm2);
	}

	@Test
	public void set_tests() {

		UuT.setUserLoggedOut(noSuchName);
		UuT.setUserLoggedIn(tNm1);
		//Note: user2 is not logged-in

		assertTrue(UuT.checkUserExists(tNm1));
		assertTrue(UuT.checkLoggedIn(tNm1));

		assertTrue(UuT.checkUserExists(tNm2));
		assertFalse(UuT.checkLoggedIn(tNm2));

		assertFalse(UuT.checkLoggedIn(noSuchName));
	}

	@Test
	public void test_VersesList() {
		assertNull(UuT.getVersesList());
	}

	@Test
	public void test_getGameId() {
		assertEquals("-1", UuT.getGameID(player1.getUsername()));
		player1.setGameID("testID");
		assertEquals("testID", player1.getGameID());
		//assertEquals("testID", UuT.getGameID(tNm1));
	}
	
	@Test
	public void setUserLoggedOut() {
		ArrayList<String> activeList = UuT.getActiveUserList();
		activeList.add("player1");
		
	}
	@Test
	public void test_getVersesList() {
		HashMap<Player, Player> versesList = UuT.getVersesList();
		assertNull(versesList);
	}
	
	@Test
	public void test_getGameID() {
		String testP = "testP";
		Player test = new Player(testP);
		
		String testP2 = "testP2";
		Player test2 = new Player(testP2);		
		
		this.gameCenter.addUser("testP");
		this.gameCenter.addUser("testP2");
		this.gameCenter.setUserLoggedIn("testP");
		this.gameCenter.setUserLoggedIn("testP2");
		
		String gameID = gameCenter.startGame(testP, testP2);
		
		userList = UuT.getUserDatabase();
		Player testPlayer = userList.get(testP);
		String testGameID = testPlayer.getGameID();
		
		assertEquals(gameID, testGameID);
	}
	
}

