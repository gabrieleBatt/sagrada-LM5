package it.polimi.ingsw.model.objective;

import it.polimi.ingsw.model.exception.CellNotFoundException;
import it.polimi.ingsw.model.exception.EmptyCellException;
import it.polimi.ingsw.model.table.glassWindow.GlassWindow;
import it.polimi.ingsw.model.table.dice.DieColor;

import java.util.Collection;
import java.util.HashSet;

public class ColorSetPublicObjective extends PublicObjective {

    private int points;
    private Collection<DieColor> colors;

    public ColorSetPublicObjective(String name, int points, Collection<DieColor> colors) {
        super(name);
        this.points = points;
        this.colors = new HashSet<>(colors);
    }

    /**
     * Gets the number of occurrences of the less frequent color on the glassWindow multiplied
     * by the points given for every complete set at the end of the game.
     * @param glassWindow the glassWindow to check
     * @return int, points given for every complete set.
     */
    @Override
    public int scorePoints(GlassWindow glassWindow) {
        int ret = 20;
        for(DieColor dc: colors){
            int newRet = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 5; j++) {
                    try {
                        if (glassWindow.getCell(i, j).isOccupied()){
                            if(glassWindow.getCell(i, j).getDie().getColor().equals(dc)){
                                newRet++;
                            }
                        }
                    } catch (CellNotFoundException | EmptyCellException e) {
                        System.out.println(e.getMessage());;
                    }
                }
            }
            if(ret > newRet){
                ret = newRet;
            }
        }
        return ret*points;
    }
}
