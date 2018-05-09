package it.polimi.ingsw.model.objective;

import it.polimi.ingsw.model.exception.CellNotFoundException;
import it.polimi.ingsw.model.exception.DeckTooSmallException;
import it.polimi.ingsw.model.exception.DieNotAllowedException;
import it.polimi.ingsw.model.exception.IllegalGlassWindowException;
import it.polimi.ingsw.model.table.dice.Die;
import it.polimi.ingsw.model.table.dice.DieColor;
import it.polimi.ingsw.model.table.glassWindow.Cell;
import it.polimi.ingsw.model.table.glassWindow.GlassWindow;
import it.polimi.ingsw.model.table.glassWindow.GlassWindowDeck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PublicObjectiveDeckTest {

    @DisplayName("Draw 4 PublicObjective")
    @Test
    void draw() throws DeckTooSmallException {
        Assertions.assertEquals(4, PublicObjectiveDeck.getPublicObjectiveDeck().draw(4).size());
    }

    @DisplayName("Draw too many PublicObjective")
    @Test
    void drawTooMany() throws DeckTooSmallException {
        Assertions.assertThrows(DeckTooSmallException.class, () -> PublicObjectiveDeck.getPublicObjectiveDeck().draw(100));
    }

    @DisplayName("Parsing control")
    @Test
    void drawRealTest() throws DeckTooSmallException, CellNotFoundException, DieNotAllowedException, IllegalGlassWindowException {
        List<PublicObjective> cards = PublicObjectiveDeck.getPublicObjectiveDeck().draw(5);

        PublicObjective publicObjective = cards.stream().filter(d -> d.getName().equals("Column Color Variety")).findFirst().get();

        List<Cell> cells = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            cells.add(new Cell(""+i));
        }
        GlassWindow glassWindow = new GlassWindow("name", 4,cells);

        Assertions.assertEquals(0, publicObjective.scorePoints(glassWindow));
        glassWindow.getCell(0,0).placeDie(new Die(DieColor.CYAN, 0), false);
        glassWindow.getCell(1, 0).placeDie(new Die(DieColor.RED, 0), false);
        Assertions.assertEquals(0, publicObjective.scorePoints(glassWindow));
        glassWindow.getCell(2, 0).placeDie(new Die(DieColor.GREEN, 0), false);
        glassWindow.getCell(3, 0).placeDie(new Die(DieColor.MAGENTA, 0), false);
        Assertions.assertEquals(5, publicObjective.scorePoints(glassWindow));

        glassWindow.getCell(1,2).placeDie(new Die(DieColor.RED, 0), false);
        Assertions.assertEquals(5, publicObjective.scorePoints(glassWindow));
        glassWindow.getCell(2,2).placeDie(new Die(DieColor.GREEN, 0), false);
        glassWindow.getCell(3,2).placeDie(new Die(DieColor.MAGENTA, 0), false);
        Assertions.assertEquals(5, publicObjective.scorePoints(glassWindow));

    }

}