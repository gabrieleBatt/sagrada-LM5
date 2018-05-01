package it.polimi.ingsw.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    void placeDie() throws NotValidNumberException, dieNotAllowedException, EmptyCellException {
        Cell cell_colour_restr = new Cell(DieColor.YELLOW);
        Cell cell_number_restr = new Cell(3);
        Die dieY = new Die(DieColor.YELLOW, 3,10);
        Die dieC = new Die(DieColor.CYAN, 5,11);
        //placing a yellow die in a cell with colour restriction: yellow
        assertTruePlace(cell_colour_restr, dieY, false);
        //placing die with numeric value 3 in a cell with numeric restriction: 3
        assertTruePlace(cell_number_restr,dieY, false);
        //placing a yellow die in a cell with colour restriction: yellow, ignoring restrictions
        assertTruePlace(cell_colour_restr,dieY, true);
        //placing die with numeric value 3 in a cell with numeric restriction: 3, ignoring restrictions
        assertTruePlace(cell_number_restr,dieY,true);
        //placing a cyan die in a cell with colour restriction: yellow, ignoring restrictions
        assertTruePlace(cell_colour_restr, dieC,true);
        //placing die with numeric value 5 in a cell with numeric restriction: 3, ignoring restrictions
        assertTruePlace(cell_number_restr, dieC, true);
        //placing a cyan die in a cell with colour restriction: yellow
        Assertions.assertThrows(dieNotAllowedException.class, () -> cell_colour_restr.placeDie(dieC, false));
        //placing die with numeric value 5 in a cell with numeric restriction: 3
        Assertions.assertThrows(dieNotAllowedException.class, () -> cell_number_restr.placeDie(dieC,false));
    }

    private void assertTruePlace(Cell cell, Die die , boolean isTrue) throws dieNotAllowedException, EmptyCellException {
        cell.placeDie(die,isTrue);
        Assertions.assertTrue(cell.getDie().equals(die));
    }

    @Test
    void isOccupied() {

    }

    @Test
    void getDie() {
    }

    @Test
    void isAllowed() {
    }
}