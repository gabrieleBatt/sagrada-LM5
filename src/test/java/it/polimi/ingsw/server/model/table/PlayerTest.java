package it.polimi.ingsw.server.model.table;

import it.polimi.ingsw.server.model.exception.CellNotFoundException;
import it.polimi.ingsw.server.model.exception.DeckTooSmallException;
import it.polimi.ingsw.server.model.exception.DieNotAllowedException;
import it.polimi.ingsw.server.model.exception.GlassWindowNotFoundException;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.dice.DieColor;
import it.polimi.ingsw.server.model.table.glassWindow.Cell;
import it.polimi.ingsw.server.model.table.glassWindow.GlassWindow;
import it.polimi.ingsw.server.model.table.glassWindow.GlassWindowDeck;
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

    @DisplayName("Player connection")
    @Test
    void setConnected() {
        Player player = new Player("player");
        player.setConnected(true);
        Assertions.assertTrue(player.isConnected());
        player.setConnected(false);
        Assertions.assertFalse(player.isConnected());
    }

    @DisplayName("Testing memento")
    @Test
    void memento() throws DeckTooSmallException, GlassWindowNotFoundException, CellNotFoundException, DieNotAllowedException {
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

