package it.polimi.ingsw.server.model.objective;

import it.polimi.ingsw.server.exception.DieNotAllowedException;
import it.polimi.ingsw.server.model.objective.ColorPrivateObjective;
import it.polimi.ingsw.server.model.table.glasswindow.Cell;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.dice.DieColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ColorPrivateObjectiveTest {

    @DisplayName("Scoring cyan private objective")
    @Test
    void scorePoints() throws DieNotAllowedException {
        List<Cell> cells = new ArrayList<>();
        ColorPrivateObjective c_objective = new ColorPrivateObjective("test", DieColor.CYAN);
        int k=0;
        for (int i = 0; i < 4; i++) {
            for(int j=0; j < 5 ; j++, k++){
                cells.add(new Cell(""+k));
            }
        }
        cells.get(2).placeDie((new Die(DieColor.CYAN, 1, 2)), false);
        cells.get(7).placeDie((new Die(DieColor.CYAN, 2, 7)), true);
        cells.get(15).placeDie((new Die(DieColor.CYAN, 6, 15)), true);
        cells.get(19).placeDie((new Die(DieColor.CYAN, 5, 19)), true);
        cells.get(0).placeDie((new Die(DieColor.YELLOW, 3, 0)), true);
        cells.get(1).placeDie((new Die(DieColor.MAGENTA, 4, 1)), true);

        GlassWindow glassWindow = new GlassWindow("test", 4, new ArrayList<>(cells));
        Assertions.assertEquals(c_objective.scorePoints(glassWindow),14);


    }
}