package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 * class to test replay board functionality
 * @author merry, priya
 *
 */
public class ReplayBoardTest {
    
	Board board;
	ReplayBoard CuT;
	Player thisPlayer;
	Player opponent;
	Player perspective;
	Piece.COLOR activeColor;
	
    ArrayList<Turn> turns;
    int currentTurn;

	
	final Piece.COLOR playerColor = Piece.COLOR.WHITE;
	final Piece.COLOR opponentColor = Piece.COLOR.RED;
	
	@BeforeEach
    public void setup() {
    	thisPlayer = new Player("player1");
    	opponent = new Player("player2");
    	perspective = new Player("perspective");

    	
    	
        Piece.COLOR opponentColor = Piece.COLOR.WHITE;
        Piece.COLOR playerColor = Piece.COLOR.RED;
        this.activeColor = playerColor;

    	board = new Board(thisPlayer,opponent, playerColor, opponentColor);
    	
    	turns = new ArrayList<Turn>();
    	//board needs to have all its turns;
    	this.turns = board.getTurns();
    	
    	CuT =  new ReplayBoard(perspective,thisPlayer, opponent, board);
    	
    }

    @Test
    public void ctor_test() {
        assertNotEquals(null, CuT.toString());

    }
    /**
     * test to check if there's a next turn to replay
     */
    @Test
    public void test_hasNext() {
    	Turn testTurn = new Turn(thisPlayer);
    	Turn testTurn2 = new Turn(opponent);
    	ArrayList<Turn> testArray = board.getTurns();
    	testArray.add(testTurn);
    	testArray.add(testTurn2);
    	assertTrue(CuT.hasNext());
    }
    /**
     * test to check if there's a previous turn to replay
     */
    @Test
    public void test_hasPrev() {
    	Turn testTurn = new Turn(thisPlayer);
    	Turn testTurn2 = new Turn(opponent);
    	ArrayList<Turn> testArray = board.getTurns();
    	testArray.add(testTurn);
    	testArray.add(testTurn2);
    	assertFalse(CuT.hasPrev());
    }
    /**
     * test to make the next turn in replay
     */
    @Test
    public void test_nextTurn() {
    	
    }
    /**
     * test to make the previous turn in replay
     */
    @Test
    public void test_prevTurn() {
    	
    }
    /**
     * test to get the active color from replay
     */
    @Test
	public void test_getActiveColor() {
		assertEquals(this.activeColor, CuT.getActiveColor());
	}
    /**
     * Test to render the Square[][] representation of the board
     */
    @Test
    public void test_renderBoard() {
    	Player testPlayer = new Player("testP");
    	Player testPlayer2 = new Player("testP2");
    	Board testBoard = new Board(testPlayer, testPlayer2, playerColor, opponentColor);
    	assertNotEquals(CuT.renderBoard(), testBoard.getBoard(testPlayer));
    	assertNotEquals(CuT.renderBoard(), testBoard.getBoard(testPlayer2));
    	
    }
	
    /**
     * Test to render the Square[][] representation of an inverted board
     */
    @Test
    public void test_renderinvertedBoard() {
    	// NOTE: This is a private method so can't call on CuT.invertedBoard();
    	}
    /**
     * test to invert the board
     * @return 
     */
    @Test
    public void test_invertBoard() {
    	// NOTE: This is a private method so can't call on CuT
    }
    /**
     * test to switch the color of the pieces
     */
    @Test
    public void test_switchColor() {
    	// NOTE: This is a private method so can't call on CuT
    }
    
    /**
     * test to switch the players
     */
    @Test
    public void test_switchPlayer() {
    	// NOTE: This is a private method so can't call on CuT
    }
}
