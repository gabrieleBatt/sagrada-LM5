package it.polimi.ingsw.server.model.objective;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.exception.EmptyCellException;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;
import it.polimi.ingsw.server.model.table.dice.DieColor;

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
                    if (glassWindow.getCell(i, j).isOccupied()
                            && glassWindow.getCell(i, j).getDie().getColor().equals(color))
                        ret += glassWindow.getCell(i, j).getDie().getNumber();
                } catch (EmptyCellException e) {
                    logger.log(Level.WARNING, e.getMessage(), e);
                }
            }
        }
        return ret;
    }
}
