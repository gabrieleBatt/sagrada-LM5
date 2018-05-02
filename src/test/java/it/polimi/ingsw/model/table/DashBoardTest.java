package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.exception.DashBoardException;
import it.polimi.ingsw.model.exception.DieNotAllowedException;
import it.polimi.ingsw.model.exception.EmptyCellException;
import it.polimi.ingsw.model.exception.NotValidNumberException;
import it.polimi.ingsw.model.table.dice.Die;
import it.polimi.ingsw.model.table.dice.DieColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DashBoardTest {

    @Test
    void getDifficulty() {
        for(int i=3; i<7; i++) {
            DashBoard dashBoard = new DashBoard("name", i);
            Assertions.assertEquals(dashBoard.getDifficulty(), i);
        }
    }

    @Test
    void getName() {
        DashBoard dashBoard = new DashBoard("name", 4);
        Assertions.assertEquals(dashBoard.getName(),"name");
    }

    @Test
    void getCell() throws NotValidNumberException {
        DashBoard dashBoard = new DashBoard("name", 4);
        Cell[][] window = new Cell[5][4];
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 4; j++) {
                Cell cell = new Cell(DieColor.CYAN);
                window[i][j] = cell;
            }
        dashBoard.setDashBoard(window);

        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 4; j++) {
                Assertions.assertTrue(window[i][j].toString().equals(dashBoard.getCell(i, j).toString()));
            }
        Assertions.assertThrows(NotValidNumberException.class, ()->dashBoard.getCell(5,2));
    }

    @Test
    void getCell1() throws NotValidNumberException, DieNotAllowedException, EmptyCellException, DashBoardException {
        DashBoard dashBoard = new DashBoard("name", 4);
        Cell[][] window = new Cell[5][4];
        Die die = new Die(DieColor.CYAN, 9);
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 4; j++) {
                Cell cell = new Cell(DieColor.CYAN);
                window[i][j] = cell;
            }
        dashBoard.setDashBoard(window);
        for (int i = 0, k = 1; i < 5; i++)
            for (int j = 0; j < 4; j++, k++) {
                Die die1 = new Die(DieColor.CYAN, k);
                dashBoard.getCell(i,j).placeDie(die1,true);
            }
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 4; j++) {
                String id = dashBoard.getCell(i, j).getDie().getId();
                Assertions.assertEquals(dashBoard.getCell(i, j), dashBoard.getCell(id));
            }
        Assertions.assertThrows(DashBoardException.class, ()->dashBoard.getCell(""));
    }

    @Test
    void areDiceNearby() {
    }

    @Test
    void checkNearby() {
    }

    @Test
    void emptyDashBoard() {
    }

    @Test
    void availableCells() {
    }
}