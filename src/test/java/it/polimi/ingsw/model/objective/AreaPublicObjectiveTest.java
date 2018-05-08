package it.polimi.ingsw.model.objective;

import it.polimi.ingsw.model.exception.*;
import it.polimi.ingsw.model.table.glassWindow.Cell;
import it.polimi.ingsw.model.table.glassWindow.GlassWindow;
import it.polimi.ingsw.model.table.dice.Die;
import it.polimi.ingsw.model.table.dice.DieColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class AreaPublicObjectiveTest {

    @DisplayName("Scoring column color objective")
    @Test
    void scorePoints() throws IllegalObjectiveException, IllegalGlassWindowException, CellNotFoundException, DieNotAllowedException {
        GlassWindow glassWindow;
        List<Cell> cells;

        AreaPublicObjective areaPublicObjective;

        List<Integer> area = new ArrayList<>();
        area.add(0);
        area.add(0);
        area.add(1);
        area.add(0);
        area.add(2);
        area.add(0);
        area.add(3);
        area.add(0);

        List<List<Integer>> multiplicity = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            multiplicity.add(new ArrayList<>());
            multiplicity.get(i).add(0);
            multiplicity.get(i).add(1);
            multiplicity.get(i).add(2);
            multiplicity.get(i).add(3);
            multiplicity.get(i).add(4);
        }
        for (int i = 6; i < 11; i++) {
            multiplicity.add(new ArrayList<>());
            multiplicity.get(i).add(0);
            multiplicity.get(i).add(1);
        }

        areaPublicObjective = new AreaPublicObjective("test", 5,area, multiplicity);

        cells = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            cells.add(new Cell(""+i));
        }
        glassWindow = new GlassWindow("name", 4,cells);

        Assertions.assertEquals(0, areaPublicObjective.scorePoints(glassWindow));
        glassWindow.getCell(0,0).placeDie(new Die(DieColor.CYAN, 0), false);
        glassWindow.getCell(1,0).placeDie(new Die(DieColor.RED, 0), false);
        Assertions.assertEquals(0, areaPublicObjective.scorePoints(glassWindow));
        glassWindow.getCell(2,0).placeDie(new Die(DieColor.GREEN, 0), false);
        glassWindow.getCell(3,0).placeDie(new Die(DieColor.MAGENTA, 0), false);
        Assertions.assertEquals(5, areaPublicObjective.scorePoints(glassWindow));

        glassWindow.getCell(0,2).placeDie(new Die(DieColor.CYAN, 0), false);
        glassWindow.getCell(1,2).placeDie(new Die(DieColor.RED, 0), false);
        Assertions.assertEquals(5, areaPublicObjective.scorePoints(glassWindow));
        glassWindow.getCell(2,2).placeDie(new Die(DieColor.GREEN, 0), false);
        glassWindow.getCell(3,2).placeDie(new Die(DieColor.MAGENTA, 0), false);
        Assertions.assertEquals(10, areaPublicObjective.scorePoints(glassWindow));

    }

    @DisplayName("Illegal creation of objective")
    @Test
    void scorePointsException(){
        List<Integer> area = new ArrayList<>();
        area.add(0);
        area.add(0);

        List<List<Integer>> multiplicity = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            multiplicity.add(new ArrayList<>());
        }
        Assertions.assertThrows(IllegalObjectiveException.class, ()->new AreaPublicObjective("test", 5, area, multiplicity));
    }

    @DisplayName("Illegal creation of objective")
    @Test
    void scorePointsException1(){
        List<Integer> area = new ArrayList<>();
        area.add(0);

        List<List<Integer>> multiplicity = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            multiplicity.add(new ArrayList<>());
        }
        Assertions.assertThrows(IllegalObjectiveException.class, ()->new AreaPublicObjective("test", 5, area, multiplicity));
    }
}