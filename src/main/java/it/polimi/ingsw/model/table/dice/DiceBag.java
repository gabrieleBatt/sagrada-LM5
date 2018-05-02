package it.polimi.ingsw.model.table.dice;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class DiceBag {
    private List<Die> bag;

    /**
     * Creates the dice bag and all the dice in it, 18 per each color
     */
    public DiceBag(){

       bag = new ArrayList<>();
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
    public List<Die> drawDice(int num){
        List<Die> ret = new ArrayList<>();
        for(int i=0; i < num ; i++){
            ret.add(drawDie());
        }
        return ret;
    }

    /**
     * Draws a die from the dice bag; removing die from the dice bag
     * @return the die drown
     */
    private Die drawDie(){
        int randomNum = ThreadLocalRandom.current().nextInt(0, bag.size() );
        Die ret = bag.get(randomNum);
        bag.remove(randomNum);
        return ret;
    }

    /**
     * Adds a die to the dice beg (tool effect)
     * @param die object die placed in the dice bag
     */
    public void placeDie (Die die){
        bag.add(die);
    }

    /**
     * method used for testing;
     * @return the amount of remaining dice
     */
    int bagSize(){
        return bag.size();
    }

}
