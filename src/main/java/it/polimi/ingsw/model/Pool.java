package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Pool {
    private List<Die> diceOnTable;

    /**
     * Creating the list of dice in the pool
     * @return a list of dice
     */
    public List<Die> getDice(){
        return new ArrayList<>(diceOnTable);
    }

    /**
     * Adding a list of die to the table
     * @param diceOnTable parameter that has to be set
     */
    public void addDice(List<Die> diceOnTable){
        this.diceOnTable = diceOnTable;
    }

    /**
     * Removing a die from the table
     * @param die object die which has to be removed
     */
    public void takeDie(Die die){
        diceOnTable.remove(die);
    }

    /**
     * Rolling dice on table (useful for tool)
     */
    public void roll(){
        for(Die d: diceOnTable) {
            d.roll();
        }
    }
}
