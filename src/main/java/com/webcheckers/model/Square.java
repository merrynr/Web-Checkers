package com.webcheckers.model;

public class Square {

    private int row;
    private int column;
    private COLOR color;
    private Piece occupant;

    enum COLOR { WHITE, BLACK; }

    public Square(Square orgininal) {
        this.row = 7 - orgininal.getRow();
        this.column = 7 - orgininal.getColumn();
        this.color = orgininal.getColor();
        this.occupant = orgininal.getOccupant();
    }

    public Square(int row, int column, COLOR color) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.occupant = null;
    }

    public COLOR getColor() {
        return this.color;
    }

    public boolean isValid() {
        return this.color == COLOR.BLACK && occupant == null;
    }

    // ----------------------------------------------Occupancy--------------------------------------------------------

    public Piece getOccupant() {
        return this.occupant;
    }

    public void setOccupant(Piece occupant) {
        this.occupant = occupant;
    }

    public void removeOccupant() {
        this.occupant = null;
    }

    public boolean isOccupied() { return this.occupant != null; }

    // ----------------------------------------------Indexing----------------------------------------------------------

    public String getIndex() {
        return String.format("%d-%d", this.row, this.column);
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public void invert() {
        this.row = 7 - row;
        this.column= 7 - column;
    }

}
