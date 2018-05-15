package it.polimi.ingsw.server.model.table.dice;

import it.polimi.ingsw.LogMaker;
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
    private List<Die> bag;
    private static final Logger logger = LogMaker.getLogger(DiceBag.class.getName(), Level.ALL);
    private Stack<List<Die>> diceBagMemento;


    /**
     * Creates the dice bag and all the dice in it, 18 per each color
     */
    public DiceBag(){
       bag = new ArrayList<>();
       diceBagMemento = new Stack<>();
       for(int i=0; i<18; i++){
           bag.add(new Die(DieColor.RED, i));
           bag.add(new Die(DieColor.GREEN, 18 + i));
           bag.add(new Die(DieColor.YELLOW, 36+i));
           bag.add(new Die(DieColor.MAGENTA, 54+i));
           bag.add(new Die(DieColor.CYAN, 72+i));
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
     * @return the die drown
     */
    private Die drawDie() {
        if (bag.size() == 0){
            throw new BagEmptyException("Dice in bag: " + bag.size()+"\nDice requested 0");
        }
        int randomNum = ThreadLocalRandom.current().nextInt(0, bag.size() );
        Die ret = bag.get(randomNum);
        bag.remove(randomNum);
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
        String ret = "The dice bag contains:";
        for(Die die: this.bag){
            ret = ret + die.toString();
        }
        return ret;
    }

    /**
     * Prints the override toString of an object DiceBag
     */
    public void dump(){
        System.out.println(this);
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
        this.bag = new ArrayList<>(diceBagMemento.peek());
    }
}
