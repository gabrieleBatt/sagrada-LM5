package it.polimi.ingsw.model.objective;

import it.polimi.ingsw.model.table.dashboard.DashBoard;

/**
 * Class representing the scoring rules for the players
 */
abstract class Objective {

    private String name;
    private char points;

    /**
     * @param name name of the objective to create
     */
    Objective(String name){
        this.name = name;
    }

    /**
     * gets the name of the objective
     * @return the string with the name
     */
    String getName() {
        return name;
    }


    /**
     * scores the points of the objective only
     * @param dashboard on witch score the point of the objective
     * @return points scored on the dashboard
     */
    public abstract int scorePoints(DashBoard dashboard);

}
