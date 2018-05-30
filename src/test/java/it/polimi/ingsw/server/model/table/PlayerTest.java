package it.polimi.ingsw.server.model.table;

import it.polimi.ingsw.server.exception.DieNotAllowedException;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.dice.DieColor;
import it.polimi.ingsw.server.model.table.glasswindow.Cell;
import it.polimi.ingsw.server.controller.deck.GlassWindowDeck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PlayerTest {

    @DisplayName("Player nickname")
    @Test
    void getNickname() {
        Player player = new Player("player");
        Assertions.assertEquals(player.getNickname(),"player");
    }

    @DisplayName("Player tokens")
    @Test
    void getTokens() {
        Player player = new Player("player");
        for(int i = 3; i<7; i++) {
            player.setTokens(i);
            Assertions.assertEquals(player.getTokens(), i);
        }

    }

    @DisplayName("Testing memento")
    @Test
    void memento() throws DieNotAllowedException {
        Player pl = new Player("name");
        pl.setGlassWindow(GlassWindowDeck.getGlassWindowDeck().draw(1).get(0));
        pl.addMemento();
        pl.getMemento();
        Assertions.assertTrue(pl.getGlassWindow().getCellList().stream().noneMatch(Cell::isOccupied));
        pl.getGlassWindow().getCell(0,0).placeDie(new Die(DieColor.CYAN, 6), true);
        pl.getMemento();
        Assertions.assertTrue(pl.getGlassWindow().getCellList().stream().noneMatch(Cell::isOccupied));
    }
}

