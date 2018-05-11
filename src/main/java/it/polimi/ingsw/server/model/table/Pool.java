package it.polimi.ingsw.server.model.table;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.model.table.dice.Die;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Pool is a concrete class representing the dice "on the table" in a round.
 */
public class Pool {

    private static final Logger logger = LogMaker.getLogger(Pool.class.getName(), Level.ALL);

    private Set<Die> diceOnTable = new HashSet<>();

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
    /**
     * Method used for testing
     * @return String representing a pool
     */
    @Override
    public String toString(){
        String ret = "On the table there are:\n";
        for (Die die: diceOnTable){
            ret = ret + die.toString();
        }
        return ret;
    }

    /**
     * Prints the override toString of an object Pool
     */
    public void dump(){
        System.out.println(this);
    }
}
