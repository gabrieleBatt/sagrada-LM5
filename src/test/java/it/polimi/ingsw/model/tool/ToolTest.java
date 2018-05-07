package it.polimi.ingsw.model.tool;

import it.polimi.ingsw.controller.Game;
import it.polimi.ingsw.model.exception.BagEmptyException;
import it.polimi.ingsw.model.exception.EndGameException;
import it.polimi.ingsw.model.rules.ActionCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ToolTest {

    Tool tool;
    ActionCommand actionCommand;

    @BeforeEach
    @Test
    void setup(){
        actionCommand = actionReceiver -> {};
        tool = new Tool(actionCommand, "test");
    }

    @Test
    void getName() {
        Assertions.assertEquals("test", tool.getName());
    }

    @Test
    void getActionCommand() {
        Assertions.assertEquals(actionCommand, tool.getActionCommand());
    }

    @Test
    void isUsed() {
        Assertions.assertFalse(tool.isUsed());
        tool.setUsed(true);
        Assertions.assertTrue(tool.isUsed());
        tool.setUsed(false);
        Assertions.assertFalse(tool.isUsed());
    }
}