package it.polimi.ingsw.model.objective;

import it.polimi.ingsw.model.exception.EmptyCellException;
import it.polimi.ingsw.model.exception.NotValidNumberException;
import it.polimi.ingsw.model.table.DashBoard;
import it.polimi.ingsw.model.table.dice.Die;
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
     * Gets the number of occurrences of the less frequent color on the dashboard multiplied
     * by the points given for every complete set at the end of the game.
     * @param dashboard the dashboard to check
     * @return int, points given for every complete set.
     */
    @Override
    public int scorePoints(DashBoard dashboard) {
        int ret = 20;
        for(DieColor dc: colors){
            int newRet = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 5; j++) {
                    try {
                        if (dashboard.getCell(i, j).isOccupied()){
                            if(dashboard.getCell(i, j).getDie().getColor().equals(dc)){
                                newRet++;
                            }
                        }
                    } catch (NotValidNumberException | EmptyCellException e) {
                        e.printStackTrace();
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
