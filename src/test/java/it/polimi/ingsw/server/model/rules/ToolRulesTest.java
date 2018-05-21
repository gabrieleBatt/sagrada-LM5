package it.polimi.ingsw.server.model.rules;

import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.controller.channels.CommunicationChannel;
import it.polimi.ingsw.server.controller.channels.Identifiable;
import it.polimi.ingsw.server.controller.channels.MockCommunicationChannel;
import it.polimi.ingsw.server.controller.channels.StdId;
import it.polimi.ingsw.server.exception.DieNotAllowedException;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.dice.DieColor;
import it.polimi.ingsw.server.model.tool.Tool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ToolRulesTest {

    Game game;

    @BeforeEach
    @Test
    void createGame(){
        List<CommunicationChannel> cc = new ArrayList<>();
        cc.add(new MockCommunicationChannel("player"));
        cc.add(new MockCommunicationChannel("player2"));
        game = new Game(cc);
    }

    @DisplayName("Testing set")
    @Test
    void setActionCommand() throws DieNotAllowedException {
        List<List<Identifiable>> identifiableList= new ArrayList<>();
        Player player = new Player("player");
        identifiableList.get(0).add(StdId.TWO);
        identifiableList.get(1).add(StdId.ONE);
        identifiableList.get(1).add(StdId.THREE);
        Die die = new Die(DieColor.CYAN,2,1);
        game.getMap().put(die.getId(),die);
        ToolRules.setActionCommand(die.getId(),player,identifiableList).execute(game);
        Assertions.assertNotEquals(die.getNumber(),2);
        Assertions.assertTrue(die.getNumber() == 3||die.getNumber() == 1);
    }

    @DisplayName("Testing random")
    @Test
    void randomActionCommand() throws DieNotAllowedException {
        List<Identifiable> identifiableList= new ArrayList<>();
        Player player = new Player("player");
        identifiableList.add(StdId.ONE);
        identifiableList.add(StdId.THREE);
        Die die = new Die(DieColor.CYAN,5,1);
        game.getMap().put(die.getId(),die);
        ToolRules.randomActionCommand(die.getId(),identifiableList).execute(game);
        Assertions.assertNotEquals(die.getNumber(),5);
        Assertions.assertTrue(die.getNumber() == 3||die.getNumber() == 1);
    }

    @DisplayName("Testing select")
    @Test
    void selectActionCommand() throws DieNotAllowedException {
        Player player = new Player("player");
        Die die = new Die(DieColor.CYAN,5,1);
        List<Die> dieList = new ArrayList<>();
        dieList.add(die);
        game.getTable().getPool().setDice(dieList);
        ToolRules.selectActionCommand(die.getId(),player,StdId.POOL,game).execute(game);
        Assertions.assertEquals(game.getMap().size(),1);
        Assertions.assertEquals(game.getMap().get(die.getId()),die);
    }

    @DisplayName("Testing swap")
    @Test
    void swapActionCommand() throws DieNotAllowedException {
        Player player = new Player("player");
        Die die1 = new Die(DieColor.CYAN, 5, 1);
        Die die2 = new Die(DieColor.CYAN, 5, 10);
        List<Die> dieList = new ArrayList<>();
        dieList.add(die1);
        dieList.add(die2);
        Integer i = 5;
        game.getTable().getPool().setDice(dieList);
        ToolRules.selectActionCommand(die1.getId(), player, StdId.POOL, game).execute(game);
        ToolRules.swapActionCommand(Optional.empty(), Optional.empty(), game, StdId.POOL, player, die2.getId(), die1.getId()).execute(game);
        Assertions.assertTrue(game.getTable().getPool().getDice().contains(die2));
        Assertions.assertTrue(game.getTable().getPool().getDice().size() == 1);

    }

    @DisplayName("Testing move")
    @Test
    void moveActionCommand() throws DieNotAllowedException {
        for (ActionCommand actionCommand : DefaultRules.getDefaultRules().getSetupGameActions()) {
            actionCommand.execute(game);
        }
        Die die1 = new Die(DieColor.CYAN, 5, 1);
        game.getTable().getPlayers().get(0).getGlassWindow().getCellList().get(0).placeDie(die1,true);
        ToolRules.moveActionCommand(Optional.empty(),Optional.empty(),true,true,true,game.getTable().getPlayers().get(0)).execute(game);
        Assertions.assertFalse(game.getTable().getPlayers().get(0).getGlassWindow().getCellList().get(0).isOccupied());
        Assertions.assertEquals(1,game.getTable().getPlayers().get(0).getGlassWindow().getCellList().size());


    }

}