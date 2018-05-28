package it.polimi.ingsw.server.model.tool;

import it.polimi.ingsw.server.controller.rules.ActionCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ToolTest {

    Tool tool;
    ActionCommand actionCommand;

    @BeforeEach
    @Test
    void setup(){
        List<ActionCommand> acl = new ArrayList<>();
        actionCommand = actionReceiver -> {};
        acl.add(actionCommand);
        List<ToolConditions> toolConditions = new ArrayList<>();
        tool = new Tool(acl, "test", toolConditions);
    }

    @DisplayName("Get tool name")
    @Test
    void getName() {
        Assertions.assertEquals("test", tool.getName());
    }

    @DisplayName("Get tool action")
    @Test
    void getActionCommand() {
        Assertions.assertTrue(tool.getActionCommandList().contains(actionCommand));
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