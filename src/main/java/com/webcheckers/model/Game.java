package com.webcheckers.model;

import java.util.*;

/**
 * Model for a single game.
 *
 * @author Dante Secada-Oz
 */
public class Game {

    /** Unique identifier for game */
    private String gameID;
    /** Game Ended*/
    private boolean isOver;
    /** Was Game Resigned */
    private boolean wasResigned;
    /** Winner */
    private Player winner;

    /** Gameboard */
    private Board board;

    /** Player One */
    private Player playerOne;
    /** Player One Color*/
    private Piece.COLOR playerOneColor;

    /** Player Two */
    private Player playerTwo;
    /** Player Two Color*/
    private Piece.COLOR playerTwoColor;


    /** Constructor for Game class. Initializes creates game board.
     * 1) Creates game id
     * 2) Assign player
     * 3) Creates gameboard
     *
     * @param player Player One
     * @param opponent Player Two
    */
    public Game(Player player, Player opponent) {
        // Create game ID
        this.gameID = UUID.randomUUID().toString();
        this.gameID = this.gameID.replace("-","");

        // Assign Players to Game
        this.playerOne = player;
        this.playerTwo = opponent;

        // Assign Colors to Players
        this.playerOneColor = Piece.COLOR.RED;
        this.playerTwoColor = Piece.COLOR.WHITE;

        // Create player boards
        this.board = new Board(player, opponent, this.playerOneColor, this.playerTwoColor);

        this.isOver = false;
    }

    // ----------------------------------------------GAME GETTERS-------------------------------------------------------

    /** Returns game identifier
    *
    * @return game identifier
    */
    public String getGameID() {
        return this.gameID;
    }

    // --- COLOR ---
    /** Get active color
     *
     * @return color of active player
     * */
    public Piece.COLOR getActiveColor() {
        if (this.playerOne.equals(this.board.getPlayer())) {
            return this.playerOneColor;
        } else {
            return this.playerTwoColor;
        }
    }

    // ----------------------------- END STATE ---------------------------------------------------//

    /**
     * Check if a game is over.
     * @return true if the game is over
     */
    public boolean isOver(){
        return this.isOver;
    }

    /**
     * Get the winner of the game
     * @return the winner of the game
     */
    public Player getWinner(){
        return this.winner;
    }

    /**
     * Check if a player resigned
     * @return true if the game was resigned
     */
    public boolean isResigned() {
        return this.wasResigned;
    }

    // ----------------------------------------------PLAYER GETTERS-----------------------------------------------------

    /** Returns if it is a given players turn
     *
     * @return is current player
     */
    public Boolean isTurn(Player player) {
        return this.board.getPlayer().equals(player);
    }
    /** Get Current Player
     *
     * @return current player
     */
    public Player getPlayer() {
        return this.board.getPlayer();
    }
    /** Get Opponent Player
     *
     * @return current player*/
    public Player getOpponent() {return this.board.getOpponent(); }
    /** Get player assigned red color
     *
     * @return player
     */
    public Player getRedPlayer() {
        if (this.playerOneColor.equals(Piece.COLOR.RED)) {
            return this.playerOne;
        } else {
            return this.playerTwo;
        }
    }
    /** Get player assigned red color
     *
     * @return player
     */
    public Player getWhitePlayer() {
        if (this.playerOneColor.equals(Piece.COLOR.WHITE)) {
            return this.playerOne;
        } else {
            return this.playerTwo;
        }
    }
    /** Return game of given player
     *
     * @return game board
     */
    public Board getBoard() {
        return this.board;
    }

    // ----------------------------------------------VALIDATE MOVE------------------------------------------------------
    /** Validate a move made by a given user
     *
     * @param startRow x position for start square
     * @param startCol y position for start square
     * @param endRow x position for end square
     * @param endCol y position for end square
     *
     * @return whether or not move is valid
     * */
    public Move.MOVETYPE validateMove(int startRow, int startCol, int endRow, int endCol) {
        return this.board.validateMove(startRow, startCol, endRow, endCol);
    }

    // -----------------------------------------------BACKUP MOVE-------------------------------------------------------

    /**
     * Back up a move
     * @return true if the backup was successful
     */
    public boolean backupMove() {
        return this.board.backupMove();
    }

    // --------------------------------------------------SUBMIT TURN----------------------------------------------------

    /**
     * Checks if a turn is valid and then
     * checks if the game is over after this turn.
     * @return a MOVETYPE with a message of what turn is
     */
    public Move.MOVETYPE submitTurn() {

        if (this.board.isTurnValid()) {
            if (this.board.hasMulti()) {
                return Move.MOVETYPE.PARTIALMULTI;
            } else {
                if (!this.board.playerHasPieces(this.getOpponent())) {
                    this.isOver = true;
                    this.wasResigned = false;
                    this.winner = this.getPlayer();
                }
                this.board.endTurn();
                return Move.MOVETYPE.VALID;
            }
        } else {
            return this.board.whatIsTurnError();
        }

    }

    // --------------------------------------------------RESIGN GAME----------------------------------------------------
    /** Resigns a game for a given player
     * */
    public boolean resignGame(Player player) {
        if (!this.isOver) {
            // Set game as over
            this.isOver = true;
            // Set game as resigned
            this.wasResigned = true;
            // Set opponent as winner
            if (this.playerOne.equals(player)) {
                this.winner = this.playerTwo;
            } else {
                this.winner = this.playerOne;
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if the opponent resigned
     * @return true if the opponent resigned
     */
    public boolean opponentResigned(){
        return wasResigned;
    }

    //--------------------------------------------------REPLAY GAME-----------------------------------------------------

    /** Create board to represent a replay
     *
     * @return replay board
     * */
    public ReplayBoard createReplay(Player perspective) {
        return new ReplayBoard(perspective, this.playerOne, this.playerTwo, this.board);
    }
}
