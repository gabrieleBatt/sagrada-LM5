package it.polimi.ingsw.server.model.table.dice;


/**
 *Die color is a concrete class containing an enum of the colors which a die can be characterized with.
 */
public enum DieColor{

    CYAN("C"), GREEN("G"), MAGENTA("M"), RED("R"),  YELLOW("Y");

    private String toString;

    DieColor(String toString){
        this.toString = toString;
    }

    @Override
    public String toString() {
        return toString;
    }
}
