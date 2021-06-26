package com.webcheckers.application;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import com.webcheckers.model.Player;

/**
 * The @code for storing and handling user data
 *
 * @author Priya, Anthony, Dante, Merry
 */
public class PlayerLobby {

    //Code Review - Dante Secada-Oz
    //Line 23: verses list doesn't belong in playerlobby. Player lobby manages players.
    //Verse list is game information and should thus be managed by game center under the information expert principle.
    //severity MEDIUM


    //handles sign in actions

    private HashMap<String, Player> userDatabase;
    private ArrayList<String> activeUserList;
    private HashMap<Player, Player> versesList;
    /**
     * Constructor for PlayerLobby
     */
    public PlayerLobby() {
        userDatabase = new HashMap<>();
        activeUserList = new ArrayList<>();

        //create a single test user & insert into the database for testing purposes
    }

    /**
     * Creates a record of a new user  in the database
     * @param username
     * @return successful or not
     */
    protected boolean addUser(String username) {
        //check if the username is valid: only letters and numbers & another user already has this username
        if(!username.matches("[ a-zA-Z0-9]+$") || !(username.trim().length() > 0)) { //COVERED BY JAVASCRIPT
            return false;
        }
        else if(userDatabase.containsKey(username))  {
            return false;
        }
        else {
            userDatabase.put(username, new Player(username));
            return true;
        }
    }

    /**
     * Erases the record of a user from the database
     * @param username
     */
    protected void removeUser(String username){
        //can change to boolean if have need to verify successful or not
        userDatabase.remove(username);
    }

    /**
     * Sets a user to the status logged in
     */
    protected void setUserLoggedIn(String username) {
        activeUserList.add(username);
    }

    /**
     *Sets a user to the status logged in
     */
    protected void setUserLoggedOut(String username) {
        if(activeUserList.contains(username)) {
            activeUserList.remove(username);
        }
    }

    /**
     * Checks if the user has an account
     * @param username
     * @return boolean 
     */
    protected boolean checkUserExists(String username) {
        return userDatabase.containsKey(username);
    }

    /**
     *Checks if the user is logged in
     * @param username
     * @return boolean
     */
    protected boolean checkLoggedIn(String username) {
        return activeUserList.contains(username);
    }

    /**
     * get online users
     * @return ArrayList<String>
     */
    protected ArrayList<String> getActiveUserList() {
        return activeUserList;
    }

    /**
     * Retrieve a player by their username
     * @return Player
     */
    public Player getUser(String username) {
        return userDatabase.get(username);
    }
    /**
     * Get list of all user accounts
     * @return HashMap<String, Player>
     */
   public HashMap<String, Player> getUserDatabase() {
	   return userDatabase;
   }
   
   /**
    * Get list of players and opponents
    * @return HashMap<Player, Player>
    */
   public HashMap<Player, Player> getVersesList() {
	   return versesList;
   }
   
   /**
    * get the game ID given the username of someone in the game
    * @param username
    * @return String
    */

   public String getGameID(String username){ //this is not returning the same player object
       //System.out.println("player is " + getUser(username) + ", and ID is " + getUser(username).getGameID());
        return getUser(username).getGameID();
   }


  }
