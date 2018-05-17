package it.polimi.ingsw.server.model.table.glasswindow;

import it.polimi.ingsw.server.exception.EmptyCellException;
import it.polimi.ingsw.server.exception.DieNotAllowedException;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.dice.DieColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CellTest {

    @DisplayName("Place die in cell, various restrictions")
    @Test
    void placeDie() throws DieNotAllowedException {
        Cell cell_colour_restr = new Cell("", DieColor.YELLOW);
        Cell cell_number_restr = new Cell("", 3);
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
        Assertions.assertThrows(DieNotAllowedException.class, () -> cell_colour_restr.placeDie(dieC, false));
        //placing die with numeric value 5 in a cell with numeric restriction: 3
        Assertions.assertThrows(DieNotAllowedException.class, () -> cell_number_restr.placeDie(dieC,false));
    }

    private void assertTruePlace(Cell cell, Die die , boolean isTrue) throws DieNotAllowedException {
        cell.placeDie(die,isTrue);
        Assertions.assertEquals(cell.getDie(),die);
    }

    @DisplayName("Check empty and full cell")
    @Test
    void isOccupied() throws DieNotAllowedException {
        Cell cell = new Cell("");
        Die die = new Die(DieColor.RED, 10);
        Assertions.assertFalse(cell.isOccupied());
        cell.placeDie(die, false);
        Assertions.assertTrue(cell.isOccupied());
    }

    @DisplayName("Place and get die")
    @Test
    void getDie() throws DieNotAllowedException {
        Cell cell = new Cell("");
        Die die = new Die(DieColor.RED, 10);
        Assertions.assertThrows(EmptyCellException.class, () -> cell.getDie());
        cell.placeDie(die, false);
        Assertions.assertEquals(cell.getDie(),die);
    }

    @DisplayName("Check allowed dice considering several restrictions")
    @Test
    void isAllowed() {
        Cell cell_colour_restr = new Cell("", DieColor.YELLOW);
        Cell cell_number_restr = new Cell("", 3);
        Die dieY = new Die(DieColor.YELLOW, 3,10);
        Die dieC = new Die(DieColor.CYAN, 5,11);
        Assertions.assertTrue(cell_colour_restr.isAllowed(dieY.getColor()));
        Assertions.assertTrue(cell_number_restr.isAllowed(dieY.getNumber()));
        Assertions.assertFalse(cell_colour_restr.isAllowed(dieC.getColor()));
        Assertions.assertFalse(cell_number_restr.isAllowed(dieC.getNumber()));
        Assertions.assertTrue(cell_colour_restr.isAllowed(dieY.getNumber()));
        Assertions.assertTrue(cell_number_restr.isAllowed(dieY.getColor()));
        Assertions.assertTrue(cell_colour_restr.isAllowed(dieC.getNumber()));
        Assertions.assertTrue(cell_number_restr.isAllowed(dieC.getColor()));
    }
}