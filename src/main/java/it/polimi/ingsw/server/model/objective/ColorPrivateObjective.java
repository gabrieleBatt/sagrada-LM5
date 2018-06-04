package it.polimi.ingsw.server.model.objective;

import it.polimi.ingsw.shared.LogMaker;
import it.polimi.ingsw.server.exception.EmptyCellException;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;
import it.polimi.ingsw.server.model.table.dice.DieColor;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A public objective that, for each die of a specific color, awards its
 * value in points
 */
public final class ColorPrivateObjective extends PrivateObjective {

    private static final Logger logger = LogMaker.getLogger(ColorPrivateObjective.class.getName(), Level.ALL);
    private DieColor color;

    public ColorPrivateObjective(String name, DieColor color) {

        super(name);
        this.color = color;
    }

    /**
     * scores the points of the objective only
     * @param glassWindow on witch score the point of the objective
     * @return points scored on the glasswindow
     */
    @Override
    public int scorePoints(GlassWindow glassWindow) {
        int ret = 0;
        for (int i = 0; i < GlassWindow.ROWS; i++) {
            for (int j= 0; j < GlassWindow.COLUMNS; j++) {
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
