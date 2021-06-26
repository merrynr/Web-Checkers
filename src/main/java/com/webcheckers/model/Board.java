package com.webcheckers.model;

import java.util.ArrayList;

/**
 * Model for a game board from a player's perspective.
 *
 * @author Dante Secada-Oz
 */
public class Board {

    /** Gameboard as multi-dimensional array of Squares */
    private Square[][] board;
    /** Current Player - Same Orientation as board */
    private Player player;
    /** Current Opponent */
    private Player opponent;
    /** Array of all turns made on board */
    private ArrayList<Turn> turns;

    /** Constructor for board class. Initializes creates game board and assigns pieces.
     * 1) Assigns players to roles
     * 2) Creates gameboard
     * 3) Creates and place squares on board
     * 4) Creates pieces and places on squares
     *
     * @param player Player One
     * @param opponent Player Two
     */
    public Board(Player player, Player opponent, Piece.COLOR playerColor, Piece.COLOR opponentColor) {

        // Create Board
        this.board = new Square[8][8];

        // Initialize Player
        this.player = player;
        this.opponent = opponent;

        // Initialize Board with Squares
        Square.COLOR colorAssignment = Square.COLOR.WHITE;
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {

                // Create and assign square to location on board
                this.board[row][column] = new Square(row, column, colorAssignment);

                // Create piece when square is black
                if (colorAssignment.equals(Square.COLOR.BLACK)) {
                    if (row <= 2) {
                        this.board[row][column].setOccupant(new Piece(opponent, opponentColor));
                    } else if (row >= 5) {
                        this.board[row][column].setOccupant(new Piece(player, playerColor));
                    }
                }

                // Switch Color Assignment for next square
                if (column == 7) {
                    continue;
                } else if (colorAssignment.equals(Square.COLOR.BLACK)) {
                    colorAssignment = Square.COLOR.WHITE;
                } else{
                    colorAssignment = Square.COLOR.BLACK;
                }

            }
        }

        // Initialize Turns Array
        this.turns = new ArrayList<>();
        this.turns.add(new Turn(player));

    }

    /** Return game board for the given player
     *
     * @param perspective perspective to render board view for
     *
     * @return player board
     */
    public Square[][] getBoard(Player perspective) {
        if (this.player.equals(perspective)) {
            return this.board;
        } else {
            return this.invertedBoard();
        }
    }

    // ----------------------------------------BOARD ATTRIBUTES-------------------------------------------------------

    /** Returns the current player
     *
     * @return player
     * */
    public Player getPlayer() {
        return this.player;
    }

    /** Returns the current opponent
     *
     * @return player
     * */
    public Player getOpponent() {
        return this.opponent;
    }

    /**
     * Checks if the player sent in has any pieces.
     * Used to check if game is over.
     * @param player
     * @return true if this player has no pieces on the board
     */
    public boolean playerHasPieces(Player player) {
        for (Square[] row : this.board) {
            for (Square cell : row) {
                if (cell.isOccupied() && cell.getOccupant().getPlayer().equals(player)) {
                    return true;
                }
            }
        }
        return false;
    }

    // -----------------------------------------BOARD MODIFIERS-------------------------------------------------------

    /** Inverts board to reflect board shift
     */
    private void invertBoard() {
        Square[][] tempBoard = new Square[8][8];

        // Initialize Board with Squares
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {

                // Create and assign square to location on board
                this.board[row][column].invert();
                tempBoard[7-row][7-column] = this.board[row][column];
            }
        }

        this.board = tempBoard;
    }

    /** Returns an invert copy of the board
     *
     * @return inverted board
     */
    private Square[][] invertedBoard() {
        Square[][] tempBoard = new Square[8][8];

        // Initialize Board with Squares
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {

                // Create and assign square to location on board
                tempBoard[7-row][7-column] = new Square(this.board[row][column]);
            }
        }

        return tempBoard;
    }

    /** Current Turn
     *
     * @return current turn
     */
    private Turn currentTurn() {
        return this.turns.get(this.turns.size() - 1);
    }

    /** Makes a move on the board and adds it to the Turn array of moves
     *
     * @param startRow x position for start square
     * @param startCol y position for start square
     * @param endRow x position for end square
     * @param endCol y position for end square
     *
     * */
    public void makeMove(int startRow, int startCol, int endRow, int endCol, Move.MOVETYPE movetype) {

        Move move = null;

        if (movetype.equals(Move.MOVETYPE.TAKE)) {
            move = new Move(new Position(startRow, startCol),
                    new Position(endRow, endCol),
                    this.board[startRow][startCol].getOccupant(),
                    this.board[(startRow+endRow)/2][(startCol+endCol)/2].getOccupant());
        } else {
            move = new Move(new Position(startRow, startCol),
                    new Position(endRow, endCol),
                    this.board[startRow][startCol].getOccupant(), movetype);
        }

        // Add move to turn
        this.currentTurn().add(move);

        // Move piece on board
        this.board[endRow][endCol].setOccupant(move.getPieceMoved());
        this.board[startRow][startCol].removeOccupant();
        if (move.gotKinged()) {
            move.getPieceMoved().king();
        }

        // PIECE TAKEN
        if (move.getType().equals(Move.MOVETYPE.TAKE)) {
            this.board[(startRow+endRow)/2][(startCol+endCol)/2].removeOccupant();
        }

    }

    // -----------------------------------------VALIDATE MOVE--------------------------------------------------------

    /** Validates whether or not a move is valid
     *
     * @param startRow x position for start square
     * @param startCol y position for start square
     * @param endRow x position for end square
     * @param endCol y position for end square
     *
     * @return whether or not move is valid
     * */
    public Move.MOVETYPE validateMove(int startRow, int startCol, int endRow, int endCol) {

        if (this.currentTurn().isKinged()) {
            this.makeMove(startRow, startCol, endRow, endCol, Move.MOVETYPE.KINGINGENDSTURN);
            return Move.MOVETYPE.KINGINGENDSTURN;
        }

        Move.MOVETYPE takeValidation = this.validateTake(startRow, startCol, endRow, endCol);
        Move.MOVETYPE singleValidation = this.validateSingle(startRow, startCol, endRow, endCol);

        if ( takeValidation.equals(Move.MOVETYPE.TAKE) ) {

            this.makeMove(startRow, startCol, endRow, endCol, takeValidation);
            return takeValidation;
            
        } else if ( singleValidation.equals(Move.MOVETYPE.SINGLE) ) {

            this.makeMove(startRow, startCol, endRow, endCol, singleValidation);
            return singleValidation;

        } else if ( (Math.abs(endRow - startRow) == 2 && Math.abs(endCol - startCol) == 2) &&
                        takeValidation != Move.MOVETYPE.INVALIDSTARTCOORD &&
                        takeValidation != Move.MOVETYPE.INVALIDENDCOORD &&
                        takeValidation != Move.MOVETYPE.INVALIDEND) {

            this.makeMove(startRow, startCol, endRow, endCol, takeValidation);
            return takeValidation;

        } else {

            this.makeMove(startRow, startCol, endRow, endCol, singleValidation);
            return singleValidation;
        }

    }

    /** Validates whether or not a move is a valid take
     *
     * @param startRow x position for start square
     * @param startCol y position for start square
     * @param endRow x position for end square
     * @param endCol y position for end square
     *
     * @return whether or not move is a valid take
     * */
    private Move.MOVETYPE validateTake (int startRow, int startCol, int endRow, int endCol) {

        // ---> TEST #1: INVALID START CO-ORDINATES <---
        // Start position IS out of bounds
        if ( (startRow > 7 || startRow < 0) || (startCol > 7 || startCol < 0) )  {
            return Move.MOVETYPE.INVALIDSTARTCOORD;
        }

        // ---> TEST #2: INVALID END CO-ORDINATES <---
        // End position IS out of bounds
        if ((endRow > 7 || endRow < 0) || (endCol > 7 || endCol < 0)) {
            return Move.MOVETYPE.INVALIDENDCOORD;
        }

        // ---> TEST #3: INVALID ENDPOINT <---
        // End position is improper direction and/or distance from Start position
        if ( !(Math.abs(endRow - startRow) == 2 && Math.abs(endCol - startCol) == 2) ) {

            return Move.MOVETYPE.INVALIDEND;
        }

        // ---> TEST #4 END OCCUPIED <---
        // End position is already occupied
        if ( this.board[endRow][endCol].isOccupied() ) {

            return Move.MOVETYPE.ENDOCCUPIED;
        }

        // ---> TEST #5: INVALID DIRECTION <---
        // Piece IS NOT King    AND     Piece IS moving backwwards
        if (this.board[startRow][startCol].getOccupant().getType().equals(Piece.TYPE.SINGLE) &&
                (endRow > startRow)) {

            return Move.MOVETYPE.WRONGDIRECTION;
        }

        // ---> TEST #6: INVALID CHAIN <---
        // Take must be either the FIRST move or PROCEED a take move
        if ( !this.currentTurn().getType().equals(Turn.TURNTYPE.EMPTY) &&
                this.currentTurn().lastMove().getType().equals(Move.MOVETYPE.SINGLE)) {

            return Move.MOVETYPE.INVALIDCHAIN;
        }

        // ---> TEST #7: VALIDATE TAKE PIECE <---
        int takeRow = (startRow+endRow)/2;
        int takeCol = (startCol+endCol)/2;

        // CASE #1: No piece to take
        if ( !this.board[takeRow][takeCol].isOccupied() ) {

            return Move.MOVETYPE.NOPIECE;

        // CASE #2: Piece to take BUT Piece is owned by Player
        } else if ( this.board[takeRow][takeCol].getOccupant().getPlayer().equals(this.player) ) {

            return Move.MOVETYPE.SELFTAKE;
        }

        return Move.MOVETYPE.TAKE;

    }

    /** Validates whether or not a move is a valid single
     *
     * @param startRow x position for start square
     * @param startCol y position for start square
     * @param endRow x position for end square
     * @param endCol y position for end square
     *
     * @return whether or not move is a valid single
     * */
    private Move.MOVETYPE validateSingle (int startRow, int startCol, int endRow, int endCol) {

        // ---> TEST #1: INVALID START CO-ORDINATES <---
        // Start position IS out of bounds
        if ( (startRow > 7 || startRow < 0) || (startCol > 7 || startCol < 0) )  {
            return Move.MOVETYPE.INVALIDSTARTCOORD;
        }

        // ---> TEST #2: INVALID END CO-ORDINATES <---
        // End position IS out of bounds
        if ((endRow > 7 || endRow < 0) || (endCol > 7 || endCol < 0)) {
            return Move.MOVETYPE.INVALIDENDCOORD;
        }

        // ---> TEST #3: END OCCUPIED <---
        // End position is improper direction and/or distance from Start position
        if ( this.board[endRow][endCol].isOccupied() ) {

            return Move.MOVETYPE.ENDOCCUPIED;
        }

        // ---> TEST #4: INVALID DIRECTION <---
        // Piece IS NOT King    AND     Piece IS moving backwards
        if (this.board[startRow][startCol].getOccupant().getType().equals(Piece.TYPE.SINGLE) &&
                (endRow > startRow)) {

            return Move.MOVETYPE.WRONGDIRECTION;
        }


        // ---> TEST #5: INVALID ENDPOINT <---
        // End position is improper direction and/or distance from Start position
        if ( !(Math.abs(endRow - startRow) == 1 && Math.abs(endCol - startCol) == 1) ) {

            return Move.MOVETYPE.INVALIDEND;
        }

        // ---> TEST #6: SINGLE FIRST <---
        // Single must be the FIRST move
        if ( !this.currentTurn().getType().equals(Turn.TURNTYPE.EMPTY) ) {

            return Move.MOVETYPE.SINGLEFIRST;
        }


        // ---> TEST #7: HAS BETTER MOVE <---
        // Single is invalid is the player has a take avalible
        if ( this.hasTake() ) {

            return Move.MOVETYPE.BETTERMOVE;
        }


        return Move.MOVETYPE.SINGLE;

    }

    /** Determines whether or not the current player has an available take
     *
     * @return whether or not the current player has an available take
     * */
    private boolean hasTake() {

        for ( Square[] row : this.board) {
            for ( Square cell : row) {
                if (cell.isOccupied() && cell.getOccupant().getPlayer().equals(this.player)) {
                    if (this.validateTake(cell.getRow(), cell.getColumn(), cell.getRow() - 2, cell.getColumn() - 2) == Move.MOVETYPE.TAKE ||
                            this.validateTake(cell.getRow(), cell.getColumn(), cell.getRow() - 2, cell.getColumn() + 2) == Move.MOVETYPE.TAKE ||
                            this.validateTake(cell.getRow(), cell.getColumn(), cell.getRow() + 2, cell.getColumn() - 2) == Move.MOVETYPE.TAKE ||
                            this.validateTake(cell.getRow(), cell.getColumn(), cell.getRow() + 2, cell.getColumn() + 2) == Move.MOVETYPE.TAKE) {

                        return true;
                    }
                }
            }
        }

        return false;
    }

    /** Determines whether or not the last piece moved has a take in a multi take avalible
     *
     * @return whether or not the current player has an available take
     * */
    public boolean hasMulti() {

        // Current Turn is either a TAKE or MULTI
        System.out.println(this.currentTurn().getType());
        if (this.currentTurn().getType().equals(Turn.TURNTYPE.TAKE) ||
                this.currentTurn().getType().equals(Turn.TURNTYPE.MULTI)) {

            // Is piece being moved kinged
            if (this.currentTurn().lastMove().getPieceMoved().getType().equals(Piece.TYPE.KING)) {

                int startRow = this.currentTurn().lastMove().getEnd().getRow();
                int startCol = this.currentTurn().lastMove().getEnd().getCell();

                return this.validateTake(startRow, startCol, startRow - 2, startCol - 2) == Move.MOVETYPE.TAKE ||
                        this.validateTake(startRow, startCol, startRow - 2, startCol + 2) == Move.MOVETYPE.TAKE ||
                        this.validateTake(startRow, startCol, startRow + 2, startCol - 2) == Move.MOVETYPE.TAKE ||
                        this.validateTake(startRow, startCol, startRow + 2, startCol + 2) == Move.MOVETYPE.TAKE ;
            } else {

                int startRow = this.currentTurn().lastMove().getEnd().getRow();
                int startCol = this.currentTurn().lastMove().getEnd().getCell();

                return this.validateTake(startRow, startCol, startRow - 2, startCol - 2) == Move.MOVETYPE.TAKE ||
                        this.validateTake(startRow, startCol, startRow - 2, startCol + 2) == Move.MOVETYPE.TAKE ;
            }

        } else {
            System.out.println("NOT TAKE");
            return false;
        }

    }

    // ------------------------------------------Back-up Move--------------------------------------------------------
    /** Backs up last move in turn
     *
     * @return successful backup
     * */
    public boolean backupMove() {
        // No moves made in turn
        if (this.currentTurn().getType().equals(Turn.TURNTYPE.EMPTY)) {
            return false;
        }

        // Get last move
        Move move = this.currentTurn().pop();

        // Reverse piece movement
        this.board[move.getStart().getRow()][move.getStart().getCell()].setOccupant(move.getPieceMoved());
        this.board[move.getEnd().getRow()][move.getEnd().getCell()].removeOccupant();

        // Reverse piece king
        if (move.gotKinged()) {
            this.board[move.getStart().getRow()][move.getStart().getCell()].getOccupant().deking();
        }

        // If piece was taken
        if (move.getType().equals(Move.MOVETYPE.TAKE)) {
            int takenRow = (move.getStart().getRow() + move.getEnd().getRow())/2 ;
            int takenCol = (move.getStart().getCell() + move.getEnd().getCell())/2 ;
            // Add piece back to board
            this.board[takenRow][takenCol].setOccupant(move.getPieceTaken());
        }


        return true;
    }

    // -----------------------------------------Submit Move----------------------------------------------------------

    /**
     * Updates who the current player is and
     * adds the turn that was just made to
     * the turns list.
     */
    public void endTurn() {

        // Switch Player and Opponent
        Player temp = this.player;
        this.player = this.opponent;
        this.opponent = temp;
        // Create New Turn
        this.turns.add(new Turn(this.player));
        // Invert Board
        this.invertBoard();

    }

    /**
     * Checks if a turn is valid
     * @return true if the turn is valid
     */
    public boolean isTurnValid() {
        return this.currentTurn().isTurnValid();
    }

    /**
     * Check why a turn was invalid
     * @return the MOVETYPE that describes the error
     */
    public Move.MOVETYPE whatIsTurnError() {
        return this.currentTurn().getLastInvalidMove();
    }

    /** Returns the array of turns
     *
     * @return turn
     * */
    public ArrayList<Turn> getTurns() {
        return this.turns;
    }

   
}
