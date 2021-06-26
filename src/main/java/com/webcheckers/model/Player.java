package com.webcheckers.model;

/**
 * The guideline for data regarding a single player
 *
 * @author Priya, Anthony, Dante, Merry
 */
public class Player {
    /** Player Username */
    private String username;
    /** Game ID */
    private String gameID;
    /** Replay board*/
    private ReplayBoard replayBoard;

    /**
     * Class Constructor
     * @param username player username
     */
    public Player(String username) {
        this.username = username;
        this.gameID = "-1";
    }

    /** Determines whether a player is current in a game
     *
     * @return true if this player is currently in
     * a game and false otherwise
     */
    public boolean isInGame() {
        return this.gameID != "-1";
    }

    /** Determines whether a player is current in a game
     *
     * @return true if this player is currently in
     * a game and false otherwise
     */
    public void setGameID(String gameID){
        this.gameID = gameID;
    }

    /** Game ID
     *
     * @return Game ID of current game
     * */
    public String getGameID(){
        return this.gameID;
    }

    /** Removes the game id */
    public void removeGame() { this.gameID = "-1"; }

    /** Gets player's username
     *
     * @return player username
     */
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean equals(Object other){
        Player otherPlayer = (Player) other;
        if(this.username.equals(otherPlayer.username)){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Assign this player to this replay board
     * @param replayBoard
     */
    public void setReplayBoard(ReplayBoard replayBoard){
        this.replayBoard = replayBoard;
    }

    /**
     * Get this players replayBoard
     * @return this.replayBoard
     */
    public ReplayBoard getReplayBoard() {
        return replayBoard;
    }
}
