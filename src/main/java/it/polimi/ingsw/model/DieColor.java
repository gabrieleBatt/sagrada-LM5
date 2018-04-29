package it.polimi.ingsw.model;

import java.awt.*;

public enum DieColor{

    RED(Color.RED), GREEN(Color.GREEN), YELLOW(Color.YELLOW), CYAN(Color.CYAN), MAGENTA(Color.MAGENTA);

    Color color;

    private DieColor(Color color){
        this.color = color;
    }

    public Color getColor(){
        return color;
    }
}
