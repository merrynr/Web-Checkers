package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test Class for {@link Player} objects
 *
 * @author Merry Ren
 */
@Tag("Model-tier")
public class PlayerTest {

    public static final String testName1 = "HelloWorld123";
    public static final String testGameID1 = "0001A";
    public static final String testName2 = "321dlroWolleH";
    public static final String testGameID2 = "B2000";

    /**
     * Test that the constructor successfully initializes a Player object
     */
    @Test
    public void ctor_withArg() {
        final Player CuT1 = new Player(testName1);
        assertEquals(testName1, CuT1.getUsername());
        assertEquals("-1",CuT1.getGameID());

        final Player CuT2 = new Player(testName1);
        assertEquals(testName1, CuT2.getUsername());
        assertEquals("-1",CuT2.getGameID());
    }

    /**
     * Test for ability to set the gameID field correctly and retrieve it
     */
    @Test
    public void test_set_getGameID() {
        final Player CuT = new Player(testName1);

        //pre-condition
        assertEquals("-1",CuT.getGameID());

        //set
        CuT.setGameID(testGameID1);

        //post-condition
        assertEquals(testGameID1, CuT.getGameID());
    }

    @Test
    public void test_equals() {
        final Player CuT1 = new Player(testName1);
        final Player differentCuT = new Player(testName2);
        final Player sameCuT = new Player(testName1);

        assertTrue(CuT1.equals(sameCuT));
        assertFalse(CuT1.equals(differentCuT));
    }
}
