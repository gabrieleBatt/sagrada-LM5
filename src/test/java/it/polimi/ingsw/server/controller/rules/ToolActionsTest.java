package it.polimi.ingsw.server.controller.rules;

import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.controller.channels.CommunicationChannel;
import it.polimi.ingsw.shared.identifiables.Identifiable;
import it.polimi.ingsw.server.controller.channels.MockCommunicationChannel;
import it.polimi.ingsw.shared.identifiables.StdId;
import it.polimi.ingsw.server.exception.BagEmptyException;
import it.polimi.ingsw.server.exception.DieNotAllowedException;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.dice.DieColor;
import it.polimi.ingsw.server.model.table.glasswindow.Cell;
import it.polimi.ingsw.server.controller.deck.GlassWindowDeck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


class ToolActionsTest {

    Game game;
    Player player;

    @BeforeEach
    @Test
    void createGame(){
        List<CommunicationChannel> cc = new ArrayList<>();
        cc.add(new MockCommunicationChannel("player"));
        cc.add(new MockCommunicationChannel("player2"));
        game = new Game(cc);
        player = new Player("player");
        player.setGlassWindow(GlassWindowDeck.getGlassWindowDeck().draw(1).get(0));
        game.addAction(new TurnActionCommand(player,false));
    }

    @DisplayName("Test set")
    @Test
    void setActionCommand() throws DieNotAllowedException {
        List<List<Identifiable>> identifiableList= new ArrayList<>();
        identifiableList.add(new ArrayList<>());
        identifiableList.add(new ArrayList<>());
        identifiableList.get(0).add(StdId.TWO);
        identifiableList.get(1).add(StdId.ONE);
        identifiableList.get(1).add(StdId.THREE);
        Die die = new Die(DieColor.CYAN,2,1);
        game.getMap().put(die.getId(),die);
        ToolActions.setActionCommand(die.getId(), identifiableList).execute(game);
        Assertions.assertNotEquals(die.getNumber(),2);
        Assertions.assertTrue(die.getNumber() == 3||die.getNumber() == 1);
    }

    @DisplayName("Test random")
    @Test
    void randomActionCommand() throws DieNotAllowedException {
        List<List<Identifiable>> identifiableList= new ArrayList<>();
        identifiableList.add(new ArrayList<>());
        identifiableList.add(new ArrayList<>());
        identifiableList.add(new ArrayList<>());
        identifiableList.add(new ArrayList<>());
        identifiableList.add(new ArrayList<>());
        identifiableList.add(new ArrayList<>());
        identifiableList.get(0).add(StdId.ONE);
        identifiableList.get(0).add(StdId.THREE);
        Die die = new Die(DieColor.CYAN,1,1);
        game.getMap().put(die.getId(),die);
        ToolActions.randomActionCommand(die.getId(),identifiableList).execute(game);
        Assertions.assertNotEquals(die.getNumber(),5);
        Assertions.assertTrue(die.getNumber() == 3||die.getNumber() == 1);
    }

    @DisplayName("Test select")
    @Test
    void selectActionCommand() throws DieNotAllowedException {
        Die die = new Die(DieColor.CYAN,5,1);
        List<Die> dieList = new ArrayList<>();
        dieList.add(die);
        game.getTable().getPool().setDice(dieList);
        ToolActions.selectActionCommand(die.getId(), StdId.POOL).execute(game);
        Assertions.assertEquals(game.getMap().size(),1);
        Assertions.assertEquals(game.getMap().get(die.getId()),die);
    }

    @DisplayName("Testing swap from Pool")
    @Test
    void swapFromPoolActionCommand() throws DieNotAllowedException {
        Die die1 = new Die(DieColor.CYAN, 5, 1);
        game.getMap().put("d1", die1);
        Die die2 = new Die(DieColor.CYAN, 5, 10);
        List<Die> dieList = new ArrayList<>();
        dieList.add(die2);
        game.getTable().getPool().setDice(dieList);

        ToolActions.swapActionCommand(null, null, StdId.POOL, "d1", "d2").execute(game);

        Assertions.assertTrue(game.getTable().getPool().getDice().contains(die1));
        Assertions.assertEquals(1, game.getTable().getPool().getDice().size());
    }

    @DisplayName("Testing swap from RoundTrack")
    @Test
    void swapFromRoundTrackActionCommand() throws DieNotAllowedException {
        Die die1 = new Die(DieColor.CYAN, 5, 1);
        game.getMap().put("d1", die1);
        Collection<Die> dieCollection = new ArrayList<>();
        Die die2 = new Die(DieColor.CYAN, 5, 10);
        dieCollection.add(die2);
        game.getTable().getRoundTrack().endRound(dieCollection);
        ToolActions.swapActionCommand(null, null, StdId.ROUND_TRACK, "d1", "d2").execute(game);
        Assertions.assertEquals(die2,game.getMap().get("d2"));
        Assertions.assertEquals(2,game.getMap().size());
        Assertions.assertTrue(game.getTable().getRoundTrack().getDice(1).contains(die1));
    }


    @DisplayName("Testing swap from DiceBag")
    @Test
    void swapFromDiceBagActionCommand() throws DieNotAllowedException {
        Die die = game.getTable().getDiceBag().drawDice(1).iterator().next();
        Die die1 = new Die (die.getColor(), die.getNumber(), Integer.parseInt(die.getId().substring(2)));
        game.getTable().getDiceBag().placeDie(die1);
        game.getMap().put("d1", die1);
        ToolActions.swapActionCommand(null, null, StdId.DICE_BAG, "d2", "d1").execute(game);
        Assertions.assertEquals(2,game.getMap().size());
        game.getTable().getDiceBag().drawDice(89);
        Assertions.assertThrows(BagEmptyException.class,()-> game.getTable().getDiceBag().drawDice(1));
    }

    @DisplayName("Test move")
    @Test
    void moveActionCommand() throws DieNotAllowedException {
        for (ActionCommand actionCommand : DefaultRules.getDefaultRules().getSetupGameActions()) {
            actionCommand.execute(game);
        }
        Die die1 = new Die(DieColor.CYAN, 5, 1);
        player.getGlassWindow().getCellList().get(0).placeDie(die1,true);
        ToolActions.moveActionCommand(null, null,true,true,true,false).execute(game);
        Assertions.assertFalse(player.getGlassWindow().getCellList().get(0).isOccupied());
        Assertions.assertEquals(1,player.getGlassWindow().getCellList().stream().filter(Cell::isOccupied).collect(Collectors.toList()).size());


    }

}