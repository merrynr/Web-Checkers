package com.webcheckers.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
/**
 *
 */
public class MessageTest {

    Message CuT1;
    Message CuT2;
    final String sampleText = "Happy Thanksgiving";
    final boolean boolreturn = true;
    final Message.TYPE info = Message.TYPE.info;
    final Message.TYPE error = Message.TYPE.ERROR;

    @Test
    public void testCtr() {
        CuT1 = new Message(info, sampleText);
        assertNotNull(CuT1);
        assertEquals(Message.TYPE.info , CuT1.type);
        assertEquals(sampleText, CuT1.text);
//        assertFalse(CuT1.value, "if there is a message, then the boolean value should be false");

//        CuT2 = new Message(error, boolreturn);
//        assertNotNull(CuT2);
//        assertEquals(Message.TYPE.ERROR , CuT2.type);
//        assertEquals(boolreturn, CuT2.value);
//        assertNull(CuT2.text, "if there is a boolean value, then the message should be null");
    }
}
