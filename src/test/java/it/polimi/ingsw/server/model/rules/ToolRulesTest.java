package it.polimi.ingsw.server.model.rules;

import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.controller.channels.CommunicationChannel;
import it.polimi.ingsw.net.identifiables.Identifiable;
import it.polimi.ingsw.server.controller.channels.MockCommunicationChannel;
import it.polimi.ingsw.net.identifiables.StdId;
import it.polimi.ingsw.server.exception.DieNotAllowedException;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.dice.DieColor;
import it.polimi.ingsw.server.model.table.glasswindow.Cell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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

    @DisplayName("Test set")
    @Test
    void setActionCommand() throws DieNotAllowedException {
        List<List<Identifiable>> identifiableList= new ArrayList<>();
        Player player = new Player("player");
        identifiableList.add(new ArrayList<>());
        identifiableList.add(new ArrayList<>());
        identifiableList.get(0).add(StdId.TWO);
        identifiableList.get(1).add(StdId.ONE);
        identifiableList.get(1).add(StdId.THREE);
        Die die = new Die(DieColor.CYAN,2,1);
        game.getMap().put(die.getId(),die);
        ToolRules.setActionCommand(die.getId(),player,identifiableList).execute(game);
        Assertions.assertNotEquals(die.getNumber(),2);
        Assertions.assertTrue(die.getNumber() == 3||die.getNumber() == 1);
    }

    @DisplayName("Test random")
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

    @DisplayName("Test select")
    @Test
    void selectActionCommand() throws DieNotAllowedException {
        Player player = new Player("player");
        Die die = new Die(DieColor.CYAN,5,1);
        List<Die> dieList = new ArrayList<>();
        dieList.add(die);
        game.getTable().getPool().setDice(dieList);
        ToolRules.selectActionCommand(die.getId(), player, StdId.POOL).execute(game);
        Assertions.assertEquals(game.getMap().size(),1);
        Assertions.assertEquals(game.getMap().get(die.getId()),die);
    }

    @DisplayName("Test swap")
    @Test
    void swapActionCommand() throws DieNotAllowedException {
        Player player = new Player("player");
        Die die1 = new Die(DieColor.CYAN, 5, 1);
        game.getMap().put("d1", die1);
        Die die2 = new Die(DieColor.CYAN, 5, 10);
        List<Die> dieList = new ArrayList<>();
        dieList.add(die2);
        game.getTable().getPool().setDice(dieList);

        ToolRules.swapActionCommand(Optional.empty(), Optional.empty(), StdId.POOL, player, "d1", "d2").execute(game);

        Assertions.assertTrue(game.getTable().getPool().getDice().contains(die1));
        Assertions.assertEquals(1, game.getTable().getPool().getDice().size());
    }

    @DisplayName("Test move")
    @Test
    void moveActionCommand() throws DieNotAllowedException {
        for (ActionCommand actionCommand : DefaultRules.getDefaultRules().getSetupGameActions()) {
            actionCommand.execute(game);
        }
        Die die1 = new Die(DieColor.CYAN, 5, 1);
        game.getTable().getPlayers().get(0).getGlassWindow().getCellList().get(0).placeDie(die1,true);
        ToolRules.moveActionCommand(Optional.empty(),Optional.empty(),true,true,true,game.getTable().getPlayers().get(0)).execute(game);
        Assertions.assertFalse(game.getTable().getPlayers().get(0).getGlassWindow().getCellList().get(0).isOccupied());
        Assertions.assertEquals(1,game.getTable().getPlayers().get(0).getGlassWindow().getCellList().stream().filter(Cell::isOccupied).collect(Collectors.toList()).size());


    }

}