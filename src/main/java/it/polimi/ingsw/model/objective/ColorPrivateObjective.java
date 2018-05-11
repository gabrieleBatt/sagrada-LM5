package it.polimi.ingsw.model.objective;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.model.exception.CellNotFoundException;
import it.polimi.ingsw.model.exception.EmptyCellException;
import it.polimi.ingsw.model.table.glassWindow.GlassWindow;
import it.polimi.ingsw.model.table.dice.DieColor;

import java.nio.file.Watchable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ColorPrivateObjective extends PrivateObjective {

    private static final Logger logger = LogMaker.getLogger(ColorPrivateObjective.class.getName(), Level.ALL);
    private DieColor color;

    public ColorPrivateObjective(String name, DieColor color) {

        super(name);
        this.color = color;
    }

    /**
     * Returns the score the player gets due to the private objective
     * @param glassWindow
     * @return
     */
    @Override
    public int scorePoints(GlassWindow glassWindow) {
        int ret = 0;
        for (int i = 0; i < 4; i++) {
            for (int j= 0; j < 5; j++) {
                try {
                    if (glassWindow.getCell(i, j).isOccupied()) {
                        if (glassWindow.getCell(i, j).getDie().getColor().equals(color)) {
                            ret += glassWindow.getCell(i, j).getDie().getNumber();
                        }
                    }
                } catch (CellNotFoundException | EmptyCellException e) {
                    logger.log(Level.WARNING, e.getMessage(), e);
                }
            }
        }
        return ret;
    }
}
