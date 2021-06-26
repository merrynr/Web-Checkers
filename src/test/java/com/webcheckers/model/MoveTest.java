
package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import com.webcheckers.model.Move.MOVETYPE;
import com.webcheckers.model.Piece.COLOR;

@Tag("Model-tier")
/**
 * Test class for Move.
 * @author priya
 *
 */

public class MoveTest {
	private Position start = new Position(1,2);
	private Position end = new Position(2,3);
	private Position wrongstart = new Position(4,5);
	private Position wrongend = new Position(6,7);
	private Piece p_mv; 
	private Piece p_tk;
	private Player player1;
	private Position positionTaken;
	private Move mv1;
	private Move mv2;

	@BeforeEach
	public void setup() {

			// info message

		Player player1 = new Player("Tony");
		Player player2 = new Player("Peter");
		p_mv = new Piece(player1, COLOR.RED);
		p_tk = new Piece(player2, COLOR.WHITE);
		//Move mv = new Move(start, end, p_mv);

		//two different kinds of moves
		//mv1 = new Move(start, end, p_mv);
		mv2 = new Move(start, end, p_mv, p_tk);
	}
	
	/**
	 * new move constructor 1
	 */
	@Test
		public void new_MoveTest() {
		//assertFalse(mv1.equals(new Move(wrongstart, wrongend, p_mv)));
	}

	/**
	 * new move constructor 2
	 */
	@Test
		public void new_mv2Test() {
			assertFalse(mv2.equals(new Move(wrongstart, wrongend, p_mv, p_tk)));
		}
	/**
	 * get start position
	 */
	@Test
		public void getStartTest() {
		//assertEquals(mv1.getStart().getRow(), start.getRow());
		//assertEquals(mv1.getStart().getCell(), start.getCell());
	}
/**
 * get end position
 */
@Test
	public void getEndTest() {
//	assertEquals(mv1.getEnd().getRow(), end.getRow());
 //   assertEquals(mv1.getEnd().getCell(), end.getCell());
	
}
/**
 * get position taken
 */
@Test
	public void getPositionTakenTest() {

	Position pos_tk = new Position(1,2);
	this.positionTaken = new Position((start.getRow() + end.getRow())/2,
			(start.getCell() + end.getCell())/2);

	assertEquals(mv2.getPositionTaken().getCell(), positionTaken.getCell());
	assertEquals(mv2.getPositionTaken().getRow(), positionTaken.getRow());
}

	
	/**
	 * get piece taken
	 */
	@Test
		public void getPieceTakenTest() {
		assertEquals(mv2.getPieceTaken(), p_tk);
		}
	/**
	 * get move type
	 */
	@Test
		public void getTypeTest() {
		//assertEquals(mv1.getType(), MOVETYPE.SINGLE);
		assertEquals(mv2.getType(), MOVETYPE.TAKE);
	}
	/**
	 * get piece moved
	 */
	@Test
		public void getPieceMovedTest() {
				//assertEquals(mv1.getPieceMoved(), p_mv);
		}

	/**
	 * reversing move
	 */
	@Test
		public void reverse() {

	}

}

	

