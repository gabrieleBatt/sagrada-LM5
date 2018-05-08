package it.polimi.ingsw.model.tool;

import it.polimi.ingsw.controller.Game;
import it.polimi.ingsw.model.exception.BagEmptyException;
import it.polimi.ingsw.model.exception.EndGameException;
import it.polimi.ingsw.model.rules.ActionCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("Get tool name")
    @Test
    void getName() {
        Assertions.assertEquals("test", tool.getName());
    }

    @DisplayName("Get tool action")
    @Test
    void getActionCommand() {
        Assertions.assertEquals(actionCommand, tool.getActionCommand());
    }

    @DisplayName("Use tool")
    @Test
    void isUsed() {
        Assertions.assertFalse(tool.isUsed());
        tool.setUsed(true);
        Assertions.assertTrue(tool.isUsed());
        tool.setUsed(false);
        Assertions.assertFalse(tool.isUsed());
    }
}