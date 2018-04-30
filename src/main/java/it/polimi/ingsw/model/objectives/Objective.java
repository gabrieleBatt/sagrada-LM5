package it.polimi.ingsw.model.objectives;

import it.polimi.ingsw.model.DashBoard;

public abstract class Objective {

    private String name;
    private char points;

    public Objective(String name, char points){
        this.name = name;
        this.points = points;
    }

    public abstract int scorePoints(DashBoard dashboard);

    public char getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }
}
