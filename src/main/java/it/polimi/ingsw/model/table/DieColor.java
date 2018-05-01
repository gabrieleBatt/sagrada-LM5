package it.polimi.ingsw.model.table;

import java.awt.*;

public enum DieColor{

    RED(Color.RED, "R"), GREEN(Color.GREEN, "G"), YELLOW(Color.YELLOW, "Y"), CYAN(Color.CYAN, "C"), MAGENTA(Color.MAGENTA, "M");

    private String toString;
    private Color color;

    /**
     * Color assignment
     * @param color
     */
    private DieColor(Color color, String toString){
        this.toString = toString;
        this.color = color;
    }

    /**
     * Getting a die color
     * @return: die color
     */

    public Color getColor(){
        return color;
    }

    @Override
    public String toString() {
        return toString;
    }
}
