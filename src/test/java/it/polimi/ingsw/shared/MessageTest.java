package it.polimi.ingsw.shared;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @DisplayName("Convert Message")
    @Test
    void convertMessage() {
        Assertions.assertEquals("SKIP", Message.decodeMessage(Message.convertMessage("SKIP")));
    }

    @DisplayName("Convert name")
    @Test
    void convertName() {
        Assertions.assertEquals(Message.convertName("testTest"), "Test Test");
        Assertions.assertEquals(Message.decodeName("Test Test"), "testTest");
    }

}