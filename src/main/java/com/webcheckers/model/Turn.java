package com.webcheckers.model;

import java.util.ArrayList;

/**
 * Class handles Turns.
 * @author Dante Secada-Oz
 *
 */
public class Turn {

    /** Player */
    private Player player;
    /** Moves in Turn */
    private ArrayList<Move> moves;
    /** Turn type */
    public enum TURNTYPE {
        EMPTY, SINGLE, TAKE, MULTI
    }

    /** Constructor for turn
     *
     * @param player player whose turn this represents
     * */
    public Turn(Player player) {
        this.player = player;
        this.moves = new ArrayList<>();
    }

    /** Move type
     *
     * @return Move type based on move array size
     * */
    public TURNTYPE getType() {
        if (this.moves.size() == 0) {
            return TURNTYPE.EMPTY;
        } else if (this.moves.size() == 1) {
            if (this.moves.get(0).getType().equals(Move.MOVETYPE.SINGLE)) {
                return TURNTYPE.SINGLE;
            } else {
                return TURNTYPE.TAKE;
            }
        } else {
            return TURNTYPE.MULTI;
        }
    }

    /** Returns whether of not turn is valid
     *
     * @return Turn validity
     * */
    public boolean isTurnValid() {
        // Test each move for move's validity
        for (Move move : moves) {
            // Move invalid if it is NOT a SINGLE or TAKE
            if (!(move.getType().equals(Move.MOVETYPE.SINGLE) ||
                    move.getType().equals(Move.MOVETYPE.TAKE))) {
                return false;
            }
        }

        return true;
    }

    /** Last move resulted in piece being kinged
     *
     * @return last piece kinged
     * */
    public boolean isKinged() {
        if (this.moves.size() == 0) {
            return false;
        } else {
            return this.moves.get(this.moves.size() - 1).gotKinged();
        }
    }

    /** Returns the MOVETYPE of the last invalid move.
     *  If no invalid move it returns the MOVETYPE of the first move
     *
     * @return MOVETYPE of the last invalid move
     * */
    public Move.MOVETYPE getLastInvalidMove() {
        for (int iter = this.moves.size()-1; iter >=0 ; iter--) {
            if (this.moves.get(iter).getType().equals(Move.MOVETYPE.SINGLE) ||
                    this.moves.get(iter).getType().equals(Move.MOVETYPE.TAKE)) {
                continue;
            } else {
                return this.moves.get(iter).getType();
            }
        }

        return this.moves.get(0).getType();
    }

    /** Add a move the moves list of the turn
     *
     * @param move move being added to the
     * */
    public void add(Move move) {
        this.moves.add(move);
    }

    /** Returns the MOVETYPE of the last invalid move.
     *  If no invalid move it returns the MOVETYPE of the first move
     *
     * @return MOVETYPE of the last invalid move
     * */
    public Move pop() {
        Move move = this.moves.get(this.moves.size() - 1);
        this.moves.remove(this.moves.size() - 1);
        return move;
    }

    /** Returns the MOVETYPE of the last invalid move.
     *  If no invalid move it returns the MOVETYPE of the first move
     *
     * @return MOVETYPE of the last invalid move
     * */
    public Move lastMove() {
        return this.moves.get(this.moves.size() - 1);
    }

    public ArrayList<Move> getMoves() {
        return this.moves;
    }
}
