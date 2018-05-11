package it.polimi.ingsw.server.model.table.glassWindow;

import it.polimi.ingsw.server.model.exception.CellNotFoundException;
import it.polimi.ingsw.server.model.exception.DieNotAllowedException;
import it.polimi.ingsw.server.model.exception.IllegalGlassWindowException;
import it.polimi.ingsw.server.model.exception.NotValidNumberException;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.dice.DieColor;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

class GlassWindowTest {

    GlassWindow db;
    List<Cell> cells;

    @Test
    @BeforeEach
    void setup() throws NotValidNumberException, DieNotAllowedException {
        cells = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            cells.add(new Cell(""+i));
        }
        cells.get(5).placeDie((new Die(DieColor.YELLOW, 5, 0)), false);
        cells.add(new Cell("18", 1));
        cells.add(new Cell("19", DieColor.CYAN));
        try {
            db = new GlassWindow("test", 4, new ArrayList<>(cells));
        } catch (IllegalGlassWindowException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("Find cell by die inside")
    @Test
    void getCellByDie() throws CellNotFoundException {
        Assertions.assertEquals(db.getCellByDie("5Y0"), cells.get(5));
        Assertions.assertThrows(CellNotFoundException.class, () -> db.getCellByDie("3R3"));
    }

    @DisplayName("Get row in which a die is")
    @Test
    void getRow() throws CellNotFoundException {
        Assertions.assertEquals(1, db.getRow(cells.get(5)));
    }

    @DisplayName("Get column in which a die is")
    @Test
    void getColumn() throws CellNotFoundException {
        Assertions.assertEquals(0, db.getColumn(cells.get(5)));
    }

    @DisplayName("Check for surrounding dice")
    @Test
    void hasSurroundingDice(){
        Assertions.assertFalse(db.hasSurroundingDice(1, 0));
        Assertions.assertTrue(db.hasSurroundingDice(2, 0));
    }

    @DisplayName("Check if a cell has dice adjacent and similar to a die")
    @Test
    void hasAdjacentSimilar() throws NotValidNumberException {
        Assertions.assertFalse(db.hasAdjacentSimilar(2, 0, (new Die(DieColor.RED, 4, 1))));
        Assertions.assertTrue(db.hasAdjacentSimilar(2, 0, (new Die(DieColor.RED, 5, 1))));
    }

    @DisplayName("Available cells for a die in glassWindow")
    @Test
    void availableCells() throws NotValidNumberException {
        List<Cell> list = new ArrayList<>(db.availableCells(new Die(DieColor.RED, 4, 1), false));
        Assertions.assertTrue(list.contains(cells.get(0)));
        Assertions.assertTrue(list.contains(cells.get(1)));
        Assertions.assertTrue(list.contains(cells.get(6)));
        Assertions.assertTrue(list.contains(cells.get(10)));
        Assertions.assertTrue(list.contains(cells.get(11)));
        Assertions.assertEquals(5, list.size());
        list = new ArrayList<>(db.availableCells(new Die(DieColor.RED, 4, 1), true));
        Assertions.assertFalse(list.contains(cells.get(5)));
        Assertions.assertEquals(19, list.size());
    }

}