package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;

@Tag("Model-tier")
/**
 * testing class for Board
 *
 * @author Merry, Priya
 */
public class BoardTest {
	
    // The scenarios

    Board CuT;
    Player thisPlayer;
    Player opponent;

    final Piece.COLOR thisColor = Piece.COLOR.WHITE;
    final Piece.COLOR opponentColor = Piece.COLOR.RED;


    @BeforeEach
    public void setup() {
        thisPlayer = new Player("Thor");
        opponent = new Player("Loki");
        this.CuT = new Board(thisPlayer, opponent, thisColor, opponentColor); //user is white, opp is red
    }

    @Test
    public void ctor_test() {
        assertNotEquals(null, CuT.toString());

    }

    @Test
    public void test_board() {
        Square[][] toTest = CuT.getBoard(thisPlayer);

        assertNotNull(toTest, "the board shouldn't be null");

        for(int row = 0; row <= 7; row++ ) {
            for(int col = 0; col <= 7; col++ ) {
                assertNotNull(toTest[row][col], "the squares on the board shouldn't be null");
                //Shortcut: black squares are always on odd (row + col)
                if((row + col) % 2 != 0) {
                    assertEquals(Square.COLOR.BLACK, toTest[row][col].getColor());
                    if (row <= 2) {
                        assertEquals(opponentColor, toTest[row][col].getOccupant().getColor());
                    } else if (row >= 5) {
                        assertEquals(thisColor, toTest[row][col].getOccupant().getColor());
                    }
                }
                else {
                    assertEquals(Square.COLOR.WHITE, toTest[row][col].getColor());
                    assertEquals(null, toTest[row][col].getOccupant());
                }
            }
        }
    }

    @Test
    public void player_has_piece() {
        assertTrue(CuT.playerHasPieces(thisPlayer));
        assertTrue(CuT.playerHasPieces(opponent));
        Square[][] board = CuT.getBoard(thisPlayer);

        for(int i = 0; i <= 7; i++) {
            for(int j = 0; j <= 7; j++) {
                if(board[i][j].getColor() == Square.COLOR.BLACK) {
                    if(board[i][j].getOccupant() != null) {
                        board[i][j].removeOccupant();
                    }
                }
            }
        }

        assertFalse(CuT.playerHasPieces(thisPlayer));
        assertFalse(CuT.playerHasPieces(opponent));
    }

    //invert_board -> test with submit_turn
    //current_turn -> private

    @Test
    public void validate_move() {
        //assertEquals(Move.MOVETYPE.INVALIDSTARTCOORD, CuT.validateMove(8,1,5,0));
        //assertEquals(Move.MOVETYPE.INVALIDENDCOORD, CuT.validateMove(5,1,8,0));
        assertEquals(Move.MOVETYPE.ENDOCCUPIED, CuT.validateMove(6,1,5,0));
        assertEquals(Move.MOVETYPE.INVALIDEND, CuT.validateMove(5,0,3,0));
        assertEquals(Move.MOVETYPE.WRONGDIRECTION, CuT.validateMove(5,2,6,1));
    }

    @Test
    public void test_backup() {
        assertFalse(CuT.backupMove());
        CuT.makeMove(6,1,5,0, Move.MOVETYPE.SINGLE);
        assertTrue(CuT.backupMove());

    }

    @Test
    public void end_turn() {
        CuT.makeMove(6,1,5,0, Move.MOVETYPE.SINGLE);
        CuT.endTurn();
        assertEquals(CuT.getPlayer(), opponent);

    }

    @Test
    public void is_turn_valid() {
        CuT.makeMove(6,1,5,0, Move.MOVETYPE.SINGLE);
        assertTrue(CuT.isTurnValid());
    }

//    @Test
//    public void test_reflect() {
//        Turn testGoodTurn = new Turn(new Position(6,1), new Position(5,0));
//        assertTrue(CuT.reflectTurn(testGoodTurn));
//
//        Turn testBadTurn = new Turn(new Position(6,1), new Position(7,2));
//        assertFalse(CuT.reflectTurn(testBadTurn));
//    }
    /**
     * test getting the board, the white perspective is not equal to the red perspective
     */
    @Test
    public void test_getBoard() {   
    	Square[][] redBoard = CuT.getBoard(thisPlayer);
    	Square[][] whiteBoard = CuT.getBoard(opponent);
    	assertNotEquals(redBoard,whiteBoard);
    }
    /**
     * testing getting the player
     */
    
    @Test
    public void test_getPlayer() {
    	assertEquals(thisPlayer, CuT.getPlayer());
    }
    /**
     * testing getting the opponent.
     */
    @Test
    public void test_getOpponent() {
    	assertEquals(opponent, CuT.getOpponent());    
    }
    
}
