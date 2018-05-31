package it.polimi.ingsw.server.controller.deck;

import it.polimi.ingsw.server.controller.deck.PublicObjectiveDeck;
import it.polimi.ingsw.server.exception.*;
import it.polimi.ingsw.server.model.objective.PublicObjective;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.dice.DieColor;
import it.polimi.ingsw.server.model.table.glasswindow.Cell;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class PublicObjectiveDeckTest {

    @DisplayName("Draw 4 PublicObjective")
    @Test
    void draw(){
        Assertions.assertEquals(4, PublicObjectiveDeck.getPublicObjectiveDeck().draw(4).size());
    }

    @DisplayName("Draw too many PublicObjective")
    @Test
    void drawTooMany(){
        Assertions.assertThrows(DeckTooSmallException.class, () -> PublicObjectiveDeck.getPublicObjectiveDeck().draw(100));
    }

    @DisplayName("Target parsing control")
    @Test
    void parseTest1() throws DieNotAllowedException {
        List<PublicObjective> cards = PublicObjectiveDeck.getPublicObjectiveDeck().draw(10);

        PublicObjective publicObjective = cards.stream().filter(d -> d.getName().equalsIgnoreCase("ColumnColorVariety")).findFirst().get();

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

    @DisplayName("Set parsing control")
    @Test
    void parseTest2() throws DieNotAllowedException{
        List<PublicObjective> cards = PublicObjectiveDeck.getPublicObjectiveDeck().draw(10);

        PublicObjective publicObjective = cards.stream().filter(d -> d.getName().equalsIgnoreCase("MediumShades")).findFirst().get();


        List<Cell> cells = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            cells.add(new Cell(""+i));
        }

        for (int i=0; i<20; i++){
            cells.get(i).placeDie(new Die(DieColor.CYAN, 1, i ), false);
        }
        GlassWindow glassWindow = new GlassWindow("test", 4, new ArrayList<>(cells));
        Assertions.assertEquals(publicObjective.scorePoints(glassWindow),0*2);

        cells.get(0).placeDie(new Die(DieColor.CYAN, 1, 0 ), false);
        cells.get(1).placeDie(new Die(DieColor.MAGENTA, 2, 1 ), false);
        cells.get(2).placeDie(new Die(DieColor.YELLOW, 3, 2), false);
        cells.get(3).placeDie(new Die(DieColor.GREEN, 4, 3 ), false);
        cells.get(4).placeDie(new Die(DieColor.RED, 5, 4 ), false);
        cells.get(5).placeDie(new Die(DieColor.RED, 6, 4 ), false);
        for (int i=6; i<20; i++){
            cells.get(i).placeDie(new Die(DieColor.CYAN, 1, i ), false);
        }
        Assertions.assertEquals(publicObjective.scorePoints(glassWindow),1*2);

        cells.get(0).placeDie(new Die(DieColor.CYAN, 1, 0 ), false);
        cells.get(1).placeDie(new Die(DieColor.CYAN, 2, 1 ), false);
        cells.get(2).placeDie(new Die(DieColor.CYAN, 3, 2), false);
        cells.get(3).placeDie(new Die(DieColor.CYAN, 4, 3 ), false);
        cells.get(4).placeDie(new Die(DieColor.CYAN, 5, 4 ), false);
        cells.get(5).placeDie(new Die(DieColor.CYAN, 6, 0 ), false);
        cells.get(6).placeDie(new Die(DieColor.CYAN, 1, 0 ), false);
        cells.get(7).placeDie(new Die(DieColor.CYAN, 2, 1 ), false);
        cells.get(8).placeDie(new Die(DieColor.CYAN, 3, 2), false);
        cells.get(9).placeDie(new Die(DieColor.CYAN, 4, 3 ), false);
        cells.get(10).placeDie(new Die(DieColor.CYAN, 5, 4 ), false);
        cells.get(11).placeDie(new Die(DieColor.CYAN, 6, 0 ), false);
        cells.get(12).placeDie(new Die(DieColor.CYAN, 1, 0 ), false);
        cells.get(13).placeDie(new Die(DieColor.CYAN, 2, 1 ), false);
        cells.get(14).placeDie(new Die(DieColor.CYAN, 3, 2), false);
        cells.get(15).placeDie(new Die(DieColor.CYAN, 4, 3 ), false);
        cells.get(16).placeDie(new Die(DieColor.CYAN, 5, 4 ), false);
        cells.get(17).placeDie(new Die(DieColor.CYAN, 6, 0 ), false);
        cells.get(18).placeDie(new Die(DieColor.CYAN, 1, 0 ), false);
        cells.get(19).placeDie(new Die(DieColor.CYAN, 3, 1 ), false);


        Assertions.assertEquals(publicObjective.scorePoints(glassWindow),3*2);

        for (int i= 0; i< 10; i++){
            cells.get(i).placeDie(new Die(DieColor.YELLOW, 3,i), false);
        }
        for (int i= 10; i< 20; i++) {
            cells.get(i).placeDie(new Die(DieColor.YELLOW, 4, i), false);
        }
        Assertions.assertEquals(publicObjective.scorePoints(glassWindow),10*2);

        for (int i= 0; i< 19; i++){
            cells.get(i).placeDie(new Die(DieColor.YELLOW, 3,i), false);
        }
        cells.get(19).placeDie(new Die(DieColor.YELLOW, 4, 19), false);
        Assertions.assertEquals(publicObjective.scorePoints(glassWindow),1*2);

    }


}