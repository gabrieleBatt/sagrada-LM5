package it.polimi.ingsw.server.model.table;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.model.table.dice.Die;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Pool is a concrete class representing the dice "on the table" in a round.
 */
public class Pool implements Memento {

    private static final Logger logger = LogMaker.getLogger(Pool.class.getName(), Level.ALL);
    private Stack<List<Die>> poolMemento = new Stack<>();
    private Set<Die> diceOnTable = new HashSet<>();

    /**
     * Gets the list of dice in the pool
     * @return a list of dice
     */
    public Collection<Die> getDice(){
        return new HashSet<>(diceOnTable);
    }

    /**
     * Sets dice in the pool
     * @param diceOnTable parameter to be set
     */
    public void setDice(Collection<Die> diceOnTable){
        this.diceOnTable = new HashSet<>(diceOnTable);
        logger.log(Level.FINEST, "These dice :" + diceOnTable + " have been added to the pool", this);

    }

    /**
     * Removes a die from the table
     * @param die object die has to be removed
     */
    public void takeDie(Die die){ diceOnTable.remove(die); }

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

    /**
     * Adds the current pool state to the stack.
     */
    @Override
    public void addMemento() {
        poolMemento.push(new ArrayList<>(diceOnTable));
    }

    /**
     * Gets the last pool state saved in the stack.
     */
    @Override
    public void getMemento() {
        this.setDice(new ArrayList<>(poolMemento.peek()));
    }
}
