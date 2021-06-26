package com.webcheckers.application;

import com.webcheckers.model.*;
import com.webcheckers.ui.GetGameRoute;
import com.webcheckers.ui.GetSignInRoute;

import com.webcheckers.ui.PostSignInRoute;
import spark.Request;

import java.util.*;

/**
 * Class that manages all of the games played by the online users/etc
 */
public class GameCenter {
    //Code Review - Merry
    //Line 190: validateMove is never called.
    //This suggests that routes in the UI are calling classes in the model tier which calls view, model, controller
    // design into question.
    //severity medium

    //keeps track of all application users
    private PlayerLobby playerLobby;
    //keeps track of number of players in the game
    private int numPlayers;
    //keeps track of when a new game is started
    private boolean newGame;

    /** Manages all games.*/
    private HashMap<String, Game> games;

    /*manages user matchups*/
    private HashMap<Player, Player> versesList;
    /**
     * GameCenter constructor
     * @param playerLobby
     */
    public GameCenter(PlayerLobby playerLobby) {
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        this.playerLobby = playerLobby;
        this.games = new HashMap<>();
        this.versesList = new HashMap<>();
        this.numPlayers = playerLobby.getActiveUserList().size();
    }

    // ~ PLAYER METHODS ~

    /**
     * add a user to the database
     * @param username
     */
    public void addUser(String username){
        this.playerLobby.addUser(username);
    }

    /**
     * remove a user from the database
     * @param username
     */
    public void removeUser(String username){
        this.playerLobby.removeUser(username);
    }

    /**
     * method to set user status as logged in
     * @param username
     */
    public void setUserLoggedIn(String username) {
        this.playerLobby.setUserLoggedIn(username);
        this.numPlayers ++;
    }

    /**
     * method to set user status logged out
     * @param username
     */
    public void setUserLoggedOut(String username) {
        this.playerLobby.setUserLoggedOut(username);
        this.numPlayers --;
    }

    /**
     * checks if the user exists in the database
     * @param username
     * @return boolean
     */
    public boolean checkUserExists(String username) {
        return this.playerLobby.checkUserExists(username);
    }

    /** Checks if user is logged in
     * @param username
     * @return boolean
     */
    public boolean checkLoggedIn(String username) {
        return this.playerLobby.checkLoggedIn(username);
    }

    //Check design, if someone has a better way, please revise this, likely should copy object and iterate over it
    /** Gets the user Database to display player list

     * @return active player list
     */
    public ArrayList<String> getActiveUserList(){
    	return this.playerLobby.getActiveUserList();
    	//purpose is to display user list
    }

    /** Gets Verses List
     * */
    public HashMap <Player, Player> getVersesList() {
    		return playerLobby.getVersesList();
    		//purpose is to add to pairings (might not be needed here)
    }

    /** Return user database
     *
     * @return user database
     */
    public HashMap<String, Player> getUserDatabase() {
        return this.playerLobby.getUserDatabase();
    }

    /**
     * get the current user in this session
     * @param request
     * @return String of username
     */
    public String getUser(Request request) {
        return request.session().attribute(PostSignInRoute.USERNAME_ATTR);
    }

   /**
    * get the number of signed in players
    * @return
    */
    public int getNumPlayers(){
        return this.numPlayers;
    }

    // ~ GAME METHODS ~

    /** Creates a new game given a player and opponent
     *
     * @param playerName name of user who initiated the game
     * @param opponentName name of user who player selected
     */
    public String startGame(String playerName, String opponentName) {
        // Get players from player lobby
    	
        Player player = this.playerLobby.getUser(playerName);
        Player opponent = this.playerLobby.getUser(opponentName);

        // Create new game and return game id
        Game newGame = new Game(player, opponent);
        this.games.put(newGame.getGameID(),newGame);

        // Add player opponent key value pair to verses directory
        this.versesList.put(player, opponent);
        this.versesList.put(opponent, player); //both ways

        // Set players in game
        player.setGameID(newGame.getGameID());
        opponent.setGameID(newGame.getGameID());

        return newGame.getGameID();
    }

    /** Returns whether a game exists based of its gameID
     *
     * @param gameID identifier of game in games hashmap
     * @return whether the provided game exists
     */
    public boolean gameExists(String gameID) {
        return (!this.games.get(gameID).equals( "-1"));
    }
    /**
     * gets the game given the GameID
     * @param gameID String
     * @return Game
     */
    public Game getGame(String gameID) {
        return this.games.get(gameID);
    }
    
    /**
     * validates moves
     * @return
     */
    public String validateMove(String gameID, String username, String data) {
    	//return type can change
    	Game game = games.get(gameID);
    	Player player = playerLobby.getUser(username);
    	//data is JSON, from session.body();
    	String validate = "temp return value";
    	return validate;
    }

    /**
     * method to get hash map of games
     * @return
     */
    public HashMap<String, Game> getGames(){
        return games;
    }
    
    /**
     * return the game ID given an username
     * @param username
     * @return String, gameID
     */
    public String getGameID(String username){
        return playerLobby.getGameID(username);
    }
    /**
     * get the Player object given String username 
     * @param username
     * @return Player
     */
    public Player getPlayer(String username){
        return getUserDatabase().get(username);
    }

    /**
     * Set the newGame variable to value
     * (true if the game just started)
     * @param value the boolean value for if
     *              the game just started
     */
    public void setGameJustStarted(boolean value){
        newGame = value;
    }

    /**
     * Checks if the game was just started for
     * routing purposes
     * @return true if the game just started
     */
    public boolean gameJustStarted(){
        return newGame;
    }

    /** Checks if a current game is over
     *
     * @param username player in the game
     * @return true if the game is over, false otherwise
     */
    public boolean gameIsOver(String username) {
        String gameID = this.getGameID(username);
        Game game = this.getGame(gameID);
        return game.isOver();
    }


    public Player getWinner(String username){
        String gameID = this.getGameID(username);
        Game game = this.getGame(gameID);
        return game.getWinner();
    }

    public boolean isResigned(String username){
        String gameID = this.getGameID(username);
        Game game = this.getGame(gameID);
        return game.isResigned();
    }

    //-----------------------------------------------Replay Mode ---------------------------------//

    /*** Get the list of games that are already over to display in the replay menu
     * */
    public ArrayList<Game> getFinishedGamesList() {
        ArrayList<Game> finishedGames = new ArrayList<>();
        for (Game val : games.values()) {
            if(val.isOver()){
                finishedGames.add(val);
            }
        }
        return finishedGames;
    }


}