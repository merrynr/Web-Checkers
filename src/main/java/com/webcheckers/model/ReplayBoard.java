package com.webcheckers.model;

import java.util.ArrayList;

public class ReplayBoard {

    private ArrayList<Turn> turns;
    private Square[][] board;
    private int currentTurn;

    private Player perspective;
    private Player player;
    private Player opponent;
    private Piece.COLOR activeColor;


    public ReplayBoard(Player perspective, Player playerOne, Player playerTwo, Board gameBoard) {

        // Add turn
        this.turns = gameBoard.getTurns();
        // Create Board
        this.board = new Square[8][8];
        // Initialize Player
        this.player = playerOne;
        this.opponent = playerTwo;
        this.perspective = perspective;
        this.currentTurn = -1;

        Piece.COLOR opponentColor = Piece.COLOR.WHITE;
        Piece.COLOR playerColor = Piece.COLOR.RED;

        this.activeColor = playerColor;

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

    }

    /** Has next turn for replay
     */
    public boolean hasNext() {
        return this.currentTurn + 1 < this.turns.size() - 1 ;
    }

    /** Has previous turn for replay
     */
    public boolean hasPrev() {
        return this.currentTurn > -1; // should be 0??
    }

    /** Steps forwards in turn stack for replay
     */
    public void nextTurn() {

        this.currentTurn += 1;

        // Execute moves in turn
        for (Move move : this.turns.get(this.currentTurn).getMoves()) {

            // Move piece to end point
            this.board[move.getEnd().getRow()][move.getEnd().getCell()].setOccupant(
                    this.board[move.getStart().getRow()][move.getStart().getCell()].getOccupant());
            // Remove piece from source
            this.board[move.getStart().getRow()][move.getStart().getCell()].removeOccupant();

            if (move.gotKinged()) {
                this.board[move.getEnd().getRow()][move.getEnd().getCell()].getOccupant().king();
            }

            // Piece was taken
            if (move.getType().equals(Move.MOVETYPE.TAKE)) {
                // Take position
                int takenStart = move.getPositionTaken().getRow();
                int takenEnd = move.getPositionTaken().getCell();
                // Remove the piece
                this.board[takenStart][takenEnd].removeOccupant();
            }
        }

        this.invertBoard();
        this.switchColor();
        this.switchPlayer();

    }

    /** Steps backward in turn stack for replay
     */
    public void prevTurn() {

        this.invertBoard();

        // Execute moves in turn
        for (Move move : this.turns.get(this.currentTurn).getMoves()) {

            // Move piece to end point
            this.board[move.getStart().getRow()][move.getStart().getCell()].setOccupant(
                    this.board[move.getEnd().getRow()][move.getEnd().getCell()].getOccupant());
            // Remove piece from source
            this.board[move.getEnd().getRow()][move.getEnd().getCell()].removeOccupant();

            // Piece was taken
            if (move.getType().equals(Move.MOVETYPE.TAKE)) {
                // Take position
                int takenStart = move.getPositionTaken().getRow();
                int takenEnd = move.getPositionTaken().getCell();

                if (move.gotKinged()) {
                    this.board[move.getStart().getRow()][move.getStart().getCell()].getOccupant().deking();
                }

                if (move.getPieceTaken().getType().equals(Piece.TYPE.KING)) {
                    this.board[takenStart][takenEnd].setOccupant(new Piece(move.getPieceTaken().getPlayer(),
                            move.getPieceTaken().getColor()));
                    this.board[takenStart][takenEnd].getOccupant().king();
                } else {
                    // Add the piece back to board
                    this.board[takenStart][takenEnd].setOccupant(new Piece(move.getPieceTaken().getPlayer(),
                            move.getPieceTaken().getColor()));
                }
            }
        }

        this.currentTurn -= 1;
        this.switchColor();
        this.switchPlayer();

    }

    public Piece.COLOR getActiveColor() {
        return this.activeColor;
    }

    /** Renders board with correct perspective
     *
     * @return Square[][] representation of board
     * */
    public Square[][] renderBoard() {
        if (this.player.equals(this.perspective)) {
            return this.board;
        } else {
            return this.invertedBoard();
        }
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

    /** Switch the active color
     */
    private void switchColor() {
        if (this.activeColor.equals(Piece.COLOR.RED)) {
            this.activeColor = Piece.COLOR.WHITE;
        } else {
            this.activeColor = Piece.COLOR.RED;
        }
    }

    /** Switch active player
     */
    private void switchPlayer() {
        Player temp = this.player;
        this.player = this.opponent;
        this.opponent = temp;
    }
}
