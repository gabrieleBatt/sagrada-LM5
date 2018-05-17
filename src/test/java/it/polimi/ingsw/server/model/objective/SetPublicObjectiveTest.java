package it.polimi.ingsw.server.model.objective;

import it.polimi.ingsw.server.exception.DieNotAllowedException;
import it.polimi.ingsw.server.model.table.glasswindow.Cell;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.dice.DieColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

class SetPublicObjectiveTest {
    GlassWindow glassWindow;
    List<Cell> cells;

    @DisplayName("Scoring set of 3 and 5")
    @Test
    void scorePoints1() throws DieNotAllowedException {
        cells = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            cells.add(new Cell(""+i));
        }
        Collection<Integer> numbers = new HashSet<>();
        numbers.add(3);
        numbers.add(5);
        SetPublicObjective objective = new SetPublicObjective("nome", 4, numbers, new HashSet<>());

        for (int i=0; i<20; i++){
            cells.get(i).placeDie(new Die(DieColor.CYAN, 1, i ), false);
        }
        glassWindow = new GlassWindow("test", 4, new ArrayList<>(cells));
        Assertions.assertEquals(objective.scorePoints(glassWindow),0*4);

        cells.get(0).placeDie(new Die(DieColor.CYAN, 1, 0 ), false);
        cells.get(1).placeDie(new Die(DieColor.MAGENTA, 2, 1 ), false);
        cells.get(2).placeDie(new Die(DieColor.YELLOW, 3, 2), false);
        cells.get(3).placeDie(new Die(DieColor.GREEN, 4, 3 ), false);
        cells.get(4).placeDie(new Die(DieColor.RED, 5, 4 ), false);
        cells.get(5).placeDie(new Die(DieColor.RED, 6, 4 ), false);
        for (int i=6; i<20; i++){
            cells.get(i).placeDie(new Die(DieColor.CYAN, 1, i ), false);
        }
        Assertions.assertEquals(objective.scorePoints(glassWindow),1*4);

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


        Assertions.assertEquals(objective.scorePoints(glassWindow),3*4);

        for (int i= 0; i< 10; i++){
            cells.get(i).placeDie(new Die(DieColor.YELLOW, 3,i), false);
        }
        for (int i= 10; i< 20; i++) {
            cells.get(i).placeDie(new Die(DieColor.YELLOW, 5, i), false);
        }
        Assertions.assertEquals(objective.scorePoints(glassWindow),10*4);

        for (int i= 0; i< 19; i++){
            cells.get(i).placeDie(new Die(DieColor.YELLOW, 3,i), false);
        }
        cells.get(19).placeDie(new Die(DieColor.YELLOW, 5, 19), false);
        Assertions.assertEquals(objective.scorePoints(glassWindow),1*4);

    }

    @DisplayName("Scoring color set objective")
    @Test
    void scorePoints2() throws  DieNotAllowedException {
        cells = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            cells.add(new Cell("" + i));
        }
        Collection<DieColor> colors = new HashSet<>();
        colors.add(DieColor.CYAN);
        colors.add(DieColor.YELLOW);
        colors.add(DieColor.RED);
        colors.add(DieColor.GREEN);
        colors.add(DieColor.MAGENTA);
        SetPublicObjective objective = new SetPublicObjective("nome", 4, new HashSet<>(), colors);

        for (int i = 0; i < 20; i++) {
            cells.get(i).placeDie(new Die(DieColor.CYAN, 1, i), false);
        }
        GlassWindow glassWindow = new GlassWindow("test", 4, new ArrayList<>(cells));
        Assertions.assertEquals(objective.scorePoints(glassWindow), 0 * 4);

        cells.get(0).placeDie(new Die(DieColor.CYAN, 1, 0), false);
        cells.get(1).placeDie(new Die(DieColor.MAGENTA, 1, 1), false);
        cells.get(2).placeDie(new Die(DieColor.YELLOW, 1, 2), false);
        cells.get(3).placeDie(new Die(DieColor.GREEN, 1, 3), false);
        cells.get(4).placeDie(new Die(DieColor.RED, 1, 4), false);
        for (int i = 5; i < 20; i++) {
            cells.get(i).placeDie(new Die(DieColor.CYAN, 1, i), false);
        }
        Assertions.assertEquals(objective.scorePoints(glassWindow), 1 * 4);

        cells.get(0).placeDie(new Die(DieColor.CYAN, 1, 0), false);
        cells.get(1).placeDie(new Die(DieColor.MAGENTA, 1, 1), false);
        cells.get(2).placeDie(new Die(DieColor.YELLOW, 1, 2), false);
        cells.get(3).placeDie(new Die(DieColor.GREEN, 1, 3), false);
        cells.get(4).placeDie(new Die(DieColor.RED, 1, 4), false);
        cells.get(5).placeDie(new Die(DieColor.CYAN, 1, 0), false);
        cells.get(6).placeDie(new Die(DieColor.MAGENTA, 1, 1), false);
        cells.get(7).placeDie(new Die(DieColor.YELLOW, 1, 2), false);
        cells.get(8).placeDie(new Die(DieColor.GREEN, 1, 3), false);
        cells.get(9).placeDie(new Die(DieColor.RED, 1, 4), false);
        cells.get(10).placeDie(new Die(DieColor.CYAN, 1, 0), false);
        cells.get(11).placeDie(new Die(DieColor.MAGENTA, 1, 1), false);
        cells.get(12).placeDie(new Die(DieColor.YELLOW, 1, 2), false);
        cells.get(13).placeDie(new Die(DieColor.GREEN, 1, 3), false);
        cells.get(14).placeDie(new Die(DieColor.RED, 1, 4), false);
        cells.get(15).placeDie(new Die(DieColor.CYAN, 1, 0), false);
        cells.get(16).placeDie(new Die(DieColor.MAGENTA, 1, 1), false);
        cells.get(17).placeDie(new Die(DieColor.YELLOW, 1, 2), false);
        cells.get(18).placeDie(new Die(DieColor.GREEN, 1, 3), false);
        cells.get(19).placeDie(new Die(DieColor.RED, 1, 4), false);
        Assertions.assertEquals(objective.scorePoints(glassWindow), 4 * 4);

        cells.get(0).placeDie(new Die(DieColor.CYAN, 1, 0), false);
        cells.get(1).placeDie(new Die(DieColor.MAGENTA, 1, 1), false);
        cells.get(2).placeDie(new Die(DieColor.YELLOW, 1, 2), false);
        cells.get(3).placeDie(new Die(DieColor.GREEN, 1, 3), false);
        cells.get(4).placeDie(new Die(DieColor.RED, 1, 4), false);
        cells.get(5).placeDie(new Die(DieColor.CYAN, 1, 0), false);
        cells.get(6).placeDie(new Die(DieColor.MAGENTA, 1, 1), false);
        cells.get(7).placeDie(new Die(DieColor.YELLOW, 1, 2), false);
        cells.get(8).placeDie(new Die(DieColor.GREEN, 1, 3), false);
        cells.get(9).placeDie(new Die(DieColor.RED, 1, 4), false);
        cells.get(10).placeDie(new Die(DieColor.CYAN, 1, 0), false);
        cells.get(11).placeDie(new Die(DieColor.MAGENTA, 1, 1), false);
        cells.get(12).placeDie(new Die(DieColor.YELLOW, 1, 2), false);
        cells.get(13).placeDie(new Die(DieColor.GREEN, 1, 3), false);
        cells.get(14).placeDie(new Die(DieColor.RED, 1, 4), false);
        cells.get(15).placeDie(new Die(DieColor.CYAN, 1, 0), false);
        cells.get(16).placeDie(new Die(DieColor.MAGENTA, 1, 1), false);
        cells.get(17).placeDie(new Die(DieColor.YELLOW, 1, 2), false);
        cells.get(18).placeDie(new Die(DieColor.GREEN, 1, 3), false);
        cells.get(19).placeDie(new Die(DieColor.GREEN, 1, 4), false);
        Assertions.assertEquals(objective.scorePoints(glassWindow), 3 * 4);


    }
}