package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
/**
 * Unit test for the Position class.
 *
 * @author Merry
 */
public class PositionTest {

    final int testRow = 2, testCol = 3;
    Position CuT;

    /**
     * Test Constructor (create a new position)
     */
    @Test
    public void ctor() {
        CuT = new Position(testRow, testCol);
        assertNotEquals(null, CuT.toString());
    }

    /**
     * Test for getRow
     */
    @Test
    public void testGetRow() {
        CuT = new Position(testRow, testCol);
        assertEquals(testRow, CuT.getRow());
    }

    /**
     * Test for getCell
     */
    @Test
    public void testGetCell() {
        CuT = new Position(testRow, testCol);
        assertEquals(testCol, CuT.getCell());
    }

    @Test
    public void testReflectRow() {
        CuT = new Position(testRow, testCol);
        assertEquals(7 - testRow, CuT.reflectRow());
    }

    @Test
    public void testReflectCol() {
        CuT = new Position(testRow, testCol);
        assertEquals(7 - testCol, CuT.reflectCell());
    }
}
