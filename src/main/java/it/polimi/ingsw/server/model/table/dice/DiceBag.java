package it.polimi.ingsw.server.model.table.dice;

import it.polimi.ingsw.shared.LogMaker;
import it.polimi.ingsw.server.exception.BagEmptyException;
import it.polimi.ingsw.server.model.table.Memento;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DiceBag is a concrete class representing the actual dice bag in the game: it contains all the left die, which are
 * randomly extracted from it.
 */
public class DiceBag implements Memento{
    private static final int BAG_SIZE = 90;
    private Set<Die> bag;
    private static final Logger logger = LogMaker.getLogger(DiceBag.class.getName(), Level.ALL);
    private Deque<List<Die>> diceBagMemento;


    /**
     * Creates the dice bag and all the dice in it, 18 per each color
     */
    public DiceBag(){
       bag = new HashSet<>();
       diceBagMemento = new ArrayDeque<>();
       for(int i=0; i<BAG_SIZE/DieColor.values().length; i++){
           int id = i;
           Arrays.stream(DieColor.values()).forEach(c -> bag.add(new Die(c, id)));
       }
    }

    /**
     * Draws a specific number of dice from the dice bag
     * @param num number of dice to be drawn
     * @return: list of dice drawn
     */
    public Collection<Die> drawDice(int num) {
        if (bag.size() < num){
            throw new BagEmptyException("Dice in bag: " + bag.size()+"\nDice requested "+ num);
        }
        Collection<Die> ret = new ArrayList<>();
        for(int i=0; i < num ; i++){
            ret.add(drawDie());
        }
        return ret;
    }

    /**
     * Draws a die from the dice bag; removing die from the dice bag
     * @return the die drown once rolled.
     */
    private Die drawDie() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, bag.size() );
        Die ret = new ArrayList<>(bag).get(randomNum);
        bag.remove(ret);
        ret.roll();
        logger.log(Level.FINEST, "drawn "+ ret.getId(), this);
        return ret;
    }

    /**
     * Adds a die to the dice bag (tool effect)
     * @param die object die placed in the dice bag
     */
    public void placeDie (Die die){
        bag.add(die);
        logger.log(Level.FINEST, "This die has been placed in the diceBag: "+ die.getId(), this);
    }

    /**
     * method used for testing;
     * @return amount of remaining dice
     */
    int bagSize(){
        return bag.size();
    }

    /**
     * method used for testing;
     * @param die object, true if it's contained in bagDice
     * @return boolean: true if the die is contained
     */
    boolean bagContains(Die die){return bag.contains(die);}

    /**
     * Method used for testing
     * @return String representing a diceBag
     */
    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder("The dice bag contains:");
        for(Die die: this.bag){
            ret.append(die.toString());
        }
        return ret.toString();
    }

    /**
     * Adds the current dice bag state at the stack.
     */
    @Override
    public void addMemento() {
        diceBagMemento.push(new ArrayList<>(bag));
    }

    /**
     * Gets the last round dice bag saved from the stack.
     */
    @Override
    public void getMemento() {
        this.bag = new HashSet<>(diceBagMemento.peek());
    }
}
