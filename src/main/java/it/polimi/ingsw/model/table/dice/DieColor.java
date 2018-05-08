package it.polimi.ingsw.model.table.dice;

import java.awt.*;

/**
 *Die color is a concrete class containing an enum of the colors which a die can be characterized with.
 */
public enum DieColor{

    RED(Color.RED, "R"), GREEN(Color.GREEN, "G"), YELLOW(Color.YELLOW, "Y"), CYAN(Color.CYAN, "C"), MAGENTA(Color.MAGENTA, "M");

    private String toString;
    private Color color;

    /**
     * Color assignment
     * @param color
     */
    DieColor(Color color, String toString){
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
