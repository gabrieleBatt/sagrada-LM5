package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.table.dice.Die;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Pool {
    private Set<Die> diceOnTable;

    /**
     * Creates the list of dice in the pool
     * @return a list of dice
     */
    public Collection<Die> getDice(){
        return new HashSet<>(diceOnTable);
    }

    /**
     * Adds a list of die to the table
     * @param diceOnTable parameter that has to be set
     */
    public void addDice(Collection<Die> diceOnTable){
        this.diceOnTable = new HashSet<>(diceOnTable);
    }

    /**
     * Removes a die from the table
     * @param die object die which has to be removed
     */
    public void takeDie(Die die){
        diceOnTable.remove(die);
    }

    /**
     * Rolls dice on table
     */
    public void roll(){
        for(Die d: diceOnTable) {
            d.roll();
        }
    }
}
