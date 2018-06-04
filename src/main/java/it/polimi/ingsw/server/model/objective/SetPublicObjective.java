package it.polimi.ingsw.server.model.objective;

import it.polimi.ingsw.server.model.table.dice.DieColor;
import it.polimi.ingsw.server.model.table.glasswindow.Cell;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;

import java.util.Collection;
import java.util.HashSet;

/**
 * A public objective that awards points for each set, specific to the objective,
 * found in the glass window.
 */
public final class SetPublicObjective extends PublicObjective {

    private int points;
    private Collection<Integer> numbers;
    private Collection<DieColor> colors;

    public SetPublicObjective(String name, int points, Collection<Integer> numbers, Collection<DieColor> colors) {
        super(name);
        this.points = points;
        this.numbers = new HashSet<>(numbers);
        this.colors = new HashSet<>(colors);
    }

    /**
     * scores the points of the objective only
     * @param glassWindow on witch score the point of the objective
     * @return points scored on the glassWindow
     */
    @Override
    public int scorePoints(GlassWindow glassWindow) {
        int ret = GlassWindow.CELLS;
        for(Integer n: numbers){
            int newRet = 0;
            for (Cell cell : glassWindow.getCellList()) {
                if (cell.isOccupied() && cell.getDie().getNumber() == n)
                    newRet++;
            }
            ret = min(ret, newRet);
        }
        for(DieColor dc: colors){
            int newRet = 0;
            for (Cell cell : glassWindow.getCellList()) {
                if (cell.isOccupied() && cell.getDie().getColor() == dc)
                    newRet++;
            }
            ret = min(ret, newRet);
        }
        return ret*points;
    }

    private int min(int x, int y){
        if(x<y){
            return x;
        }else
            return y;
    }
}
