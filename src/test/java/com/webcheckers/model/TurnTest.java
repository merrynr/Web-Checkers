package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

@Tag("Model-tier")
public class TurnTest {

    private Turn UuT; //test instance
    private ArrayList moves; //may not need to use
    private Player player;
    private Move single;
    private Move take;
    private Move taketwo; //add-on for multi move

    @BeforeEach
    public void setup() {
        player = new Player("testPlayer");
        UuT = new Turn(player);
    }

    @Test
    public void testCtr() {
        assertNotNull(UuT, "Turn class failed to initialize turn object");
    }

    @Test
    public void test_get_turn_type() {
        assertEquals(Turn.TURNTYPE.EMPTY, UuT.getType());
        //UuT.add();
    }

    @Test
    public void test_valid_turn() {
    }

    @Test
    public void test_last_invalid_move() {

    }

    @Test
    public void test_get_last_move() {
    }
}
