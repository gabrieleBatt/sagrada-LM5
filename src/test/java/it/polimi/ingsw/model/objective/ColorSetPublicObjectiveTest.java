package it.polimi.ingsw.model.objective;

import it.polimi.ingsw.model.exception.DieNotAllowedException;
import it.polimi.ingsw.model.exception.IllegalDashboardException;
import it.polimi.ingsw.model.exception.NotValidNumberException;
import it.polimi.ingsw.model.table.dashboard.Cell;
import it.polimi.ingsw.model.table.dashboard.DashBoard;
import it.polimi.ingsw.model.table.dice.Die;
import it.polimi.ingsw.model.table.dice.DieColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

class ColorSetPublicObjectiveTest {
    DashBoard db;
    List<Cell> cells;

    @DisplayName("Scoring color set objective")
    @Test
    void scorePoints() throws NotValidNumberException, DieNotAllowedException, IllegalDashboardException {
        cells = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            cells.add(new Cell(""+i));
        }
        Collection<DieColor> colors = new HashSet<>();
        colors.add(DieColor.CYAN);
        colors.add(DieColor.YELLOW);
        colors.add(DieColor.RED);
        colors.add(DieColor.GREEN);
        colors.add(DieColor.MAGENTA);
        ColorSetPublicObjective objective = new ColorSetPublicObjective("nome", 4, colors);

        for (int i=0; i<20; i++){
            cells.get(i).placeDie(new Die(DieColor.CYAN, 1, i ), false);
        }
        DashBoard dashBoard = new DashBoard("test", 4, new ArrayList<>(cells));
        Assertions.assertEquals(objective.scorePoints(dashBoard),0*4);

        cells.get(0).placeDie(new Die(DieColor.CYAN, 1, 0 ), false);
        cells.get(1).placeDie(new Die(DieColor.MAGENTA, 1, 1 ), false);
        cells.get(2).placeDie(new Die(DieColor.YELLOW, 1, 2), false);
        cells.get(3).placeDie(new Die(DieColor.GREEN, 1, 3 ), false);
        cells.get(4).placeDie(new Die(DieColor.RED, 1, 4 ), false);
        for (int i=5; i<20; i++){
            cells.get(i).placeDie(new Die(DieColor.CYAN, 1, i ), false);
        }
        Assertions.assertEquals(objective.scorePoints(dashBoard),1*4);

        cells.get(0).placeDie(new Die(DieColor.CYAN, 1, 0 ), false);
        cells.get(1).placeDie(new Die(DieColor.MAGENTA, 1, 1 ), false);
        cells.get(2).placeDie(new Die(DieColor.YELLOW, 1, 2), false);
        cells.get(3).placeDie(new Die(DieColor.GREEN, 1, 3 ), false);
        cells.get(4).placeDie(new Die(DieColor.RED, 1, 4 ), false);
        cells.get(5).placeDie(new Die(DieColor.CYAN, 1, 0 ), false);
        cells.get(6).placeDie(new Die(DieColor.MAGENTA, 1, 1 ), false);
        cells.get(7).placeDie(new Die(DieColor.YELLOW, 1, 2), false);
        cells.get(8).placeDie(new Die(DieColor.GREEN, 1, 3 ), false);
        cells.get(9).placeDie(new Die(DieColor.RED, 1, 4 ), false);
        cells.get(10).placeDie(new Die(DieColor.CYAN, 1, 0 ), false);
        cells.get(11).placeDie(new Die(DieColor.MAGENTA, 1, 1 ), false);
        cells.get(12).placeDie(new Die(DieColor.YELLOW, 1, 2), false);
        cells.get(13).placeDie(new Die(DieColor.GREEN, 1, 3 ), false);
        cells.get(14).placeDie(new Die(DieColor.RED, 1, 4 ), false);
        cells.get(15).placeDie(new Die(DieColor.CYAN, 1, 0 ), false);
        cells.get(16).placeDie(new Die(DieColor.MAGENTA, 1, 1 ), false);
        cells.get(17).placeDie(new Die(DieColor.YELLOW, 1, 2), false);
        cells.get(18).placeDie(new Die(DieColor.GREEN, 1, 3 ), false);
        cells.get(19).placeDie(new Die(DieColor.RED, 1, 4 ), false);
        Assertions.assertEquals(objective.scorePoints(dashBoard),4*4);

        cells.get(0).placeDie(new Die(DieColor.CYAN, 1, 0 ), false);
        cells.get(1).placeDie(new Die(DieColor.MAGENTA, 1, 1 ), false);
        cells.get(2).placeDie(new Die(DieColor.YELLOW, 1, 2), false);
        cells.get(3).placeDie(new Die(DieColor.GREEN, 1, 3 ), false);
        cells.get(4).placeDie(new Die(DieColor.RED, 1, 4 ), false);
        cells.get(5).placeDie(new Die(DieColor.CYAN, 1, 0 ), false);
        cells.get(6).placeDie(new Die(DieColor.MAGENTA, 1, 1 ), false);
        cells.get(7).placeDie(new Die(DieColor.YELLOW, 1, 2), false);
        cells.get(8).placeDie(new Die(DieColor.GREEN, 1, 3 ), false);
        cells.get(9).placeDie(new Die(DieColor.RED, 1, 4 ), false);
        cells.get(10).placeDie(new Die(DieColor.CYAN, 1, 0 ), false);
        cells.get(11).placeDie(new Die(DieColor.MAGENTA, 1, 1 ), false);
        cells.get(12).placeDie(new Die(DieColor.YELLOW, 1, 2), false);
        cells.get(13).placeDie(new Die(DieColor.GREEN, 1, 3 ), false);
        cells.get(14).placeDie(new Die(DieColor.RED, 1, 4 ), false);
        cells.get(15).placeDie(new Die(DieColor.CYAN, 1, 0 ), false);
        cells.get(16).placeDie(new Die(DieColor.MAGENTA, 1, 1 ), false);
        cells.get(17).placeDie(new Die(DieColor.YELLOW, 1, 2), false);
        cells.get(18).placeDie(new Die(DieColor.GREEN, 1, 3 ), false);
        cells.get(19).placeDie(new Die(DieColor.GREEN, 1, 4 ), false);
        Assertions.assertEquals(objective.scorePoints(dashBoard),3*4);




    }
}