package it.polimi.ingsw.model.objective;

import it.polimi.ingsw.model.exception.DieNotAllowedException;
import it.polimi.ingsw.model.exception.IllegalDashboardException;
import it.polimi.ingsw.model.exception.NotValidNumberException;
import it.polimi.ingsw.model.table.Cell;
import it.polimi.ingsw.model.table.DashBoard;
import it.polimi.ingsw.model.table.dice.Die;
import it.polimi.ingsw.model.table.dice.DieColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NumberSetPublicObjectiveTest {
    DashBoard dashBoard;
    List<Cell> cells;

    @Test
    void scorePoints() throws NotValidNumberException, DieNotAllowedException, IllegalDashboardException {
        cells = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            cells.add(new Cell(""+i));
        }
        Collection<Integer> numbers = new HashSet<>();
        numbers.add(3);
        numbers.add(5);
        NumberSetPublicObjective objective = new NumberSetPublicObjective("nome", 4, numbers);

        for (int i=0; i<20; i++){
            cells.get(i).placeDie(new Die(DieColor.CYAN, 1, i ), false);
        }
        dashBoard = new DashBoard("test", 4, new ArrayList<>(cells));
        Assertions.assertEquals(objective.scorePoints(dashBoard),0*4);

        cells.get(0).placeDie(new Die(DieColor.CYAN, 1, 0 ), false);
        cells.get(1).placeDie(new Die(DieColor.MAGENTA, 2, 1 ), false);
        cells.get(2).placeDie(new Die(DieColor.YELLOW, 3, 2), false);
        cells.get(3).placeDie(new Die(DieColor.GREEN, 4, 3 ), false);
        cells.get(4).placeDie(new Die(DieColor.RED, 5, 4 ), false);
        cells.get(5).placeDie(new Die(DieColor.RED, 6, 4 ), false);
        for (int i=6; i<20; i++){
            cells.get(i).placeDie(new Die(DieColor.CYAN, 1, i ), false);
        }
        Assertions.assertEquals(objective.scorePoints(dashBoard),1*4);

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


        Assertions.assertEquals(objective.scorePoints(dashBoard),3*4);

        for (int i= 0; i< 10; i++){
            cells.get(i).placeDie(new Die(DieColor.YELLOW, 3,i), false);
        }
        for (int i= 10; i< 20; i++) {
            cells.get(i).placeDie(new Die(DieColor.YELLOW, 5, i), false);
        }
        Assertions.assertEquals(objective.scorePoints(dashBoard),10*4);

        for (int i= 0; i< 19; i++){
            cells.get(i).placeDie(new Die(DieColor.YELLOW, 3,i), false);
        }
        cells.get(19).placeDie(new Die(DieColor.YELLOW, 5, 19), false);
        Assertions.assertEquals(objective.scorePoints(dashBoard),1*4);

    }
}