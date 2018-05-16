package it.polimi.ingsw.server.model.objective;

import it.polimi.ingsw.server.model.table.glassWindow.GlassWindow;

/**
 * Class representing the scoring rules for the players
 */
public abstract class Objective {

    private String name;
    private char points;

    /**
     * @param name name of the objective to create
     */
    public Objective(String name){
        this.name = name;
    }

    /**
     * gets the name of the objective
     * @return the string with the name
     */
    public String getName() {
        return name;
    }


    /**
     * scores the points of the objective only
     * @param glassWindow on witch score the point of the objective
     * @return points scored on the glassWindow
     */
    public abstract int scorePoints(GlassWindow glassWindow);

}
