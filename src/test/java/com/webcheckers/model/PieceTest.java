package com.webcheckers.model;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


import java.awt.Color;


import com.webcheckers.model.Piece.COLOR;


/**
 * JUnit for Piece
 * @author priya
 *
 */
public class PieceTest {
	private final Color RED = Color.RED;
	private final Color WHITE = Color.WHITE;
	private Player player1;
	private Player player2;
	
	/**
	 * test a red piece
	 */
	@Test
	public void red_piece() {
		player1 = new Player("player1");
		final Piece UuT = new Piece(player1, COLOR.RED);
		assertNotEquals(null, UuT.toString());
	}

	/**
	 * test a white piece (creation)
	 */
	@Test
	public void white_piece() {
		player2 = new Player("player2");
		final Piece UuT = new Piece(player2, COLOR.WHITE);
		assertNotEquals(null, UuT.toString());
	}

	/**
	 * prove there's two different colors
	 */
	@Test
	public void color_diff() {

		final Piece UuT = new Piece(player1, COLOR.RED);
		final Piece UuT2 = new Piece(player2, COLOR.WHITE);
		assertNotEquals(UuT.getColor(), UuT2.getColor());
	}

	/**
	 * test you can get the color, player and type
	 */
	@Test
	public void test_getters() {
		final Piece UuT = new Piece(player1, COLOR.RED);
		assertEquals(COLOR.RED, UuT.getColor());
		assertEquals(player1, UuT.getPlayer());
		assertEquals(Piece.TYPE.SINGLE, UuT.getType());

		UuT.king();
		assertEquals(Piece.TYPE.KING, UuT.getType());
		UuT.deking();
		assertEquals(Piece.TYPE.SINGLE, UuT.getType());
	}
}
