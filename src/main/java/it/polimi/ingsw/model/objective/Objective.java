package it.polimi.ingsw.model.objective;

import it.polimi.ingsw.model.table.DashBoard;

/**
 * Class representing the scoring rules for the players
 */
abstract class Objective {

    private String name;
    private char points;

    /**
     * @param name name of the objective to create
     * @param points points scored for each time the objective is completed
     *               or '#' if the scoring involves a special scoring
     */
    Objective(String name, char points){
        this.name = name;
        this.points = points;
    }

    /**
     * scores the points of the objective only
     * @param dashboard on witch score the point of the objective
     * @return points scored on the dashboard
     */
    abstract int scorePoints(DashBoard dashboard);

    /**
     * gets the points scored for each time the objective is completed
     * or '#' if the scoring involves a special scoring
     * @return the char value representing the points
     */
    char getPoints() {
        return points;
    }

    /**
     * gets the name of the objective
     * @return the string with the name
     */
    String getName() {
        return name;
    }
}
