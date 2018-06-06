package it.polimi.ingsw.server.model.table;

import it.polimi.ingsw.shared.LogMaker;
import it.polimi.ingsw.server.model.table.dice.Die;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Pool is a concrete class representing the dice "on the table" in a round.
 */
public class Pool implements Memento {

    private static final Logger logger = LogMaker.getLogger(Pool.class.getName(), Level.ALL);
    private Deque<List<Die>> poolMemento = new ArrayDeque<>();
    private Set<Die> diceOnTable = new HashSet<>();

    /**
     * Gets the list of dice in the pool
     * @return a list of dice
     */
    public Collection<Die> getDice(){
        return new HashSet<>(diceOnTable);
    }

    /**
     * Swap pair of dice from the pool.
     * @param dieToPlace Object Die, die to place on the pool.
     * @param dieToTake Object Die, die to take from the pool.
     */
    public void swapDice(Die dieToPlace, Die dieToTake){
        this.diceOnTable.remove(dieToTake);
        this.diceOnTable.add(dieToPlace);
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
        StringBuilder ret = new StringBuilder("On the table there are:\n");
        for (Die die: diceOnTable){
            ret.append(die.toString());
        }
        return ret.toString();
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

    /**
     * Adds a die in Pool.
     * @param die object Die, die to add.
     */
    public void addDie(Die die) {
        this.diceOnTable.add(die);
    }
}
