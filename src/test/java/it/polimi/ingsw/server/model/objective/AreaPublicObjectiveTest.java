package it.polimi.ingsw.server.model.objective;

import it.polimi.ingsw.server.exception.*;
import it.polimi.ingsw.server.model.table.glasswindow.Cell;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.dice.DieColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class AreaPublicObjectiveTest {

    @DisplayName("Scoring column color objective")
    @Test
    void scorePoints1() throws DieNotAllowedException {
        GlassWindow glassWindow;
        List<Cell> cells;

        AreaPublicObjective areaPublicObjective;

        List<Coordinate> area = new ArrayList<>();
        area.add(new Coordinate(0, 0));
        area.add(new Coordinate(1, 0));
        area.add(new Coordinate(2, 0));
        area.add(new Coordinate(3, 0));

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

    @DisplayName("Scoring diagonal color objective")
    @Test
    void scorePoints2() throws DieNotAllowedException {
        GlassWindow glassWindow;
        List<Cell> cells;

        AreaPublicObjective areaPublicObjective;

        List<List<Integer>> multiplicity = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            multiplicity.add(new ArrayList<>());
            multiplicity.get(i).add(0);
            multiplicity.get(i).add(1);
            multiplicity.get(i).add(2);
        }
        for (int i = 6; i < 11; i++) {
            multiplicity.add(new ArrayList<>());
            multiplicity.get(i).add(0);
            multiplicity.get(i).add(2);
        }

        List<Coordinate> area = new ArrayList<>();
        area.add(new Coordinate(0, 0));
        area.add(new Coordinate(1, 1));

        areaPublicObjective = new AreaPublicObjective("test", 1, area, multiplicity);

        area = new ArrayList<>();
        area.add(new Coordinate(0, 0));
        area.add(new Coordinate(-1, 1));

        areaPublicObjective.addArea(area, multiplicity);

        area = new ArrayList<>();
        area.add(new Coordinate(0, 0));
        area.add(new Coordinate(1, -1));

        areaPublicObjective.addArea(area, multiplicity);

        area = new ArrayList<>();
        area.add(new Coordinate(0, 0));
        area.add(new Coordinate(-1, -1));

        areaPublicObjective.addArea(area, multiplicity);


        cells = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            cells.add(new Cell(""+i));
        }
        glassWindow = new GlassWindow("name", 4, cells);

        Assertions.assertEquals(0, areaPublicObjective.scorePoints(glassWindow));
        glassWindow.getCell(0,0).placeDie(new Die(DieColor.CYAN, 0), false);
        Assertions.assertEquals(0, areaPublicObjective.scorePoints(glassWindow));
        glassWindow.getCell(1,1).placeDie(new Die(DieColor.CYAN, 0), false);
        glassWindow.getCell(2,2).placeDie(new Die(DieColor.MAGENTA, 0), false);
        Assertions.assertEquals(2, areaPublicObjective.scorePoints(glassWindow));

        glassWindow.getCell(3,3).placeDie(new Die(DieColor.MAGENTA, 0), false);
        glassWindow.getCell(3,1).placeDie(new Die(DieColor.MAGENTA, 0), false);
        Assertions.assertEquals(5, areaPublicObjective.scorePoints(glassWindow));

    }

    @DisplayName("Illegal creation of objective")
    @Test
    void scorePointsException(){
        List<Coordinate> area = new ArrayList<>();
        area.add(new Coordinate(0, 0));

        List<List<Integer>> multiplicity = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            multiplicity.add(new ArrayList<>());
        }
        Assertions.assertThrows(IllegalArgumentException.class, ()->new AreaPublicObjective("test", 5, area, multiplicity));
    }
}