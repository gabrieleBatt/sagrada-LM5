package it.polimi.ingsw.server.controller.rules;

import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.controller.channels.CommunicationChannel;
import it.polimi.ingsw.server.controller.channels.MockCommunicationChannel;
import it.polimi.ingsw.server.exception.*;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.glasswindow.Cell;
import it.polimi.ingsw.server.controller.deck.GlassWindowDeck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
        Player player = new Player("p1");
        player.setGlassWindow(GlassWindowDeck.getGlassWindowDeck().draw(1).get(0));
        game.addAction(new TurnActionCommand(player,false));
    }

    @DisplayName("Testing turn")
    @Test
    void execute() throws DieNotAllowedException {
        for (ActionCommand actionCommand : DefaultRules.getDefaultRules().getSetupGameActions()) {
            actionCommand.execute(game);
        }
        DefaultRules.getDefaultRules().getSetupRoundAction().execute(game);

        TurnActionCommand turnActionCommand =DefaultRules.getDefaultRules().getTurnAction(game.getTable().getPlayer("p1"), false);
        game.addAction(turnActionCommand);
        turnActionCommand.execute(game);
        Assertions.assertTrue( 3 <= game.getTable().getPool().getDice().size());
        Assertions.assertTrue( 5 >= game.getTable().getPool().getDice().size());
        Assertions.assertTrue(2 >= game.getTable().getPlayer("p1").getGlassWindow().getCellList().stream().filter(Cell::isOccupied).count());
        Assertions.assertTrue(0 <= game.getTable().getPlayer("p1").getGlassWindow().getCellList().stream().filter(Cell::isOccupied).count());

    }

}