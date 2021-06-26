package com.webcheckers.model;

public class Piece {

    /** Player who owns piece */
    private Player owner;
    /** Type of Piece */
    private TYPE pieceType;
    /** Color of Piece */
    private COLOR pieceColor;

    /** All Piece Colors */
    public enum COLOR { WHITE, RED; }
    /** All Piece Types */
    public enum TYPE { SINGLE, KING; }

    /** Constructor for a piece
     *
     * @param owner player who owns piece
     * @param color color of piece
     * */
    public Piece(Player owner, COLOR color) {
        this.owner = owner;
        this.pieceType = TYPE.SINGLE;
        this.pieceColor = color;
    }

    /** Color of piece
     *
     * @return Piece color
     * */
    public COLOR getColor() {
        return this.pieceColor;
    }

    /** Player who owns piece
     *
     * @return Piece owner
     * */
    public Player getPlayer() {
        return this.owner;
    }

    /** Type of piece
     *
     * @return Piece type
     * */
    public TYPE getType() {
        return this.pieceType;
    }

    /** King a piece */
    public void king() {
        this.pieceType = TYPE.KING;
    }

    /** Deking a piece */
    public void deking() {
        this.pieceType = TYPE.SINGLE;
    }

}
