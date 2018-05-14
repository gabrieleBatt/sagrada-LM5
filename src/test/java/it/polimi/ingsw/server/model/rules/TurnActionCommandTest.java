package it.polimi.ingsw.server.model.rules;

import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.controller.commChannel.CommunicationChannel;
import it.polimi.ingsw.server.controller.commChannel.MockCommunicationChannel;
import it.polimi.ingsw.server.model.exception.*;
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
    void execute() throws PlayerNotFoundException, BagEmptyException, GlassWindowNotFoundException, EndGameException, DeckTooSmallException {
        for (ActionCommand actionCommand : DefaultRules.getDefaultRules().getSetupGameActions()) {
            actionCommand.execute(game);
        }
        DefaultRules.getDefaultRules().getSetupRoundAction().execute(game);
        DefaultRules.getDefaultRules().getTurnAction(game.getTable().getPlayer("p1")).execute(game);
        Assertions.assertEquals((5|4),game.getTable().getPool().getDice().size());
        //TODO
        //Tools missing
    }

}