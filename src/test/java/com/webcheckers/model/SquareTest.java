package com.webcheckers.model;

//import org.junit.Test;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The unit test suite for the Game class.
 *
 * @author Dante Secada-Oz, Merry
 */
public class SquareTest {

    /** Test square constructor */
    @Test
    public void test_square_constructor() {
        Square testSquare = new Square(0,0, Square.COLOR.BLACK);

        assertEquals("0-0", testSquare.getIndex(),"Square class failed to assign index.");
        assertEquals(Square.COLOR.BLACK, testSquare.getColor(),"Square class failed assign color.");
    }

    /** Test inverted square constructor */
    @Test
    public void test_inverse_constructor() {
        Square testSquare = new Square(0,0, Square.COLOR.BLACK);
        Square testInverse =  new Square(testSquare);

        assertEquals("7-7", testInverse.getIndex(), "Square class failed to assign inverted index.");
        assertEquals(Square.COLOR.BLACK, testInverse.getColor());
        assertNull(testInverse.getOccupant());
    }

    /** Test that square properly handles occupancies I/O and validity. */
    @Test
    public void test_square_occupant() {
        Square testSquare = new Square(0,0, Square.COLOR.BLACK);

        // Init State
        assertNull(testSquare.getOccupant(), "Square must be initialized as null.");
        assertTrue(testSquare.isValid(), "isValid() incorrectly identified testSquare as invalid.");

        // Assign Occupant
        Player testPlayer = new Player("TestPlayer");
        Piece testOccupant = new Piece(testPlayer, Piece.COLOR.RED);
        testSquare.setOccupant(testOccupant);
        assertEquals(testOccupant, testSquare.getOccupant(),"setOccupant() failed to set occupant.");
        assertTrue(testSquare.isOccupied());
        assertFalse(testSquare.isValid(), "isValid() incorrectly identified testSquare as valid.");

        // Remove Occupant
        testSquare.removeOccupant();
        assertNull(testSquare.getOccupant(),"removeOccupant() failed to remove occupant.");
        assertFalse(testSquare.isOccupied());
        assertTrue(testSquare.isValid(), "isValid() incorrectly identified testSquare as invalid after occupant removal.");

        //Get Row and Column
        assertEquals(0, testSquare.getRow());
        assertEquals(0, testSquare.getColumn());

        //invert
        testSquare.invert();
        assertEquals("7-7", testSquare.getIndex(), "Square class failed to irevert index.");
    }



}
