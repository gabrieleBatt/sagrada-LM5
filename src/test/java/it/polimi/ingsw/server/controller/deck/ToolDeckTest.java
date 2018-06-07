package it.polimi.ingsw.server.controller.deck;

import it.polimi.ingsw.server.controller.deck.ToolDeck;
import it.polimi.ingsw.server.model.tool.Tool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class ToolDeckTest {

    @DisplayName("Draw tool card")
    @Test
    void draw() {
        List<Tool> toolList = ToolDeck.getToolDeck().draw(12);
        Tool tool1 = toolList.stream().filter(t -> t.getName().equals("copperFoilBurnisher")).findFirst().get();
        Assertions.assertEquals(1,tool1.getActionCommandList().size());
        Tool tool2 = toolList.stream().filter(t -> t.getName().equals("corkBakedStraightedge")).findFirst().get();
        Assertions.assertEquals(2,tool2.getActionCommandList().size());
        Tool tool3 = toolList.stream().filter(t -> t.getName().equals("eglomiseBrush")).findFirst().get();
        Assertions.assertEquals(1,tool3.getActionCommandList().size());
        Tool tool4 = toolList.stream().filter(t -> t.getName().equals("fluxBrush")).findFirst().get();
        Assertions.assertEquals(3,tool4.getActionCommandList().size());
        Tool tool5 = toolList.stream().filter(t -> t.getName().equals("fluxRemover")).findFirst().get();
        Assertions.assertEquals(4,tool5.getActionCommandList().size());
        Tool tool6 = toolList.stream().filter(t -> t.getName().equals("glazingHammer")).findFirst().get();
        Assertions.assertEquals(1,tool6.getActionCommandList().size());
        Tool tool7 = toolList.stream().filter(t -> t.getName().equals("grindingStone")).findFirst().get();
        Assertions.assertEquals(3,tool7.getActionCommandList().size());
        Tool tool8 = toolList.stream().filter(t -> t.getName().equals("grozingPliers")).findFirst().get();
        Assertions.assertEquals(3,tool8.getActionCommandList().size());
        Tool tool9 = toolList.stream().filter(t -> t.getName().equals("lathekin")).findFirst().get();
        Assertions.assertEquals(2,tool9.getActionCommandList().size());
        Tool tool10 = toolList.stream().filter(t -> t.getName().equals("lensCutter")).findFirst().get();
        Assertions.assertEquals(3,tool10.getActionCommandList().size());
        Tool tool11 = toolList.stream().filter(t -> t.getName().equals("runningPliers")).findFirst().get();
        Assertions.assertEquals(3,tool11.getActionCommandList().size());
        Tool tool12 = toolList.stream().filter(t -> t.getName().equals("tapWheel")).findFirst().get();
        Assertions.assertEquals(3,tool12.getActionCommandList().size());
    }
}