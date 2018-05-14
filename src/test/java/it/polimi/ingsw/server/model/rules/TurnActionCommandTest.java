package it.polimi.ingsw.server.model.rules;

import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.controller.commChannel.CommunicationChannel;
import it.polimi.ingsw.server.controller.commChannel.MockCommunicationChannel;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.table.glassWindow.Cell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TurnActionCommandTest {

    Game game;
    List<CommunicationChannel> ccl;

    @BeforeEach
    @Test
    void setup(){
        ccl = new ArrayList<>();
        ccl.add(new MockCommunicationChannel("p1"));
        ccl.add(new MockCommunicationChannel("p2"));
        game = new Game(ccl);
    }

    @DisplayName("Testing turn")
    @Test
    void execute() throws BagEmptyException, GlassWindowNotFoundException, EndGameException, DeckTooSmallException, CellNotFoundException, DieNotAllowedException {
        for (ActionCommand actionCommand : DefaultRules.getDefaultRules().getSetupGameActions()) {
            actionCommand.execute(game);
        }
        DefaultRules.getDefaultRules().getSetupRoundAction().execute(game);
        DefaultRules.getDefaultRules().getTurnAction(game.getTable().getPlayer("p1")).execute(game);
        Assertions.assertTrue( 4 <= game.getTable().getPool().getDice().size());
        Assertions.assertTrue( 5 >= game.getTable().getPool().getDice().size());
        Assertions.assertTrue(1 >= game.getTable().getPlayer("p1").getGlassWindow().getCellList().stream().filter(Cell::isOccupied).count());
        Assertions.assertTrue(0 <= game.getTable().getPlayer("p1").getGlassWindow().getCellList().stream().filter(Cell::isOccupied).count());
        //TODO
        //Tools missing
    }

}