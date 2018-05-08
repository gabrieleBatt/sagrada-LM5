package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.exception.EndGameException;
import it.polimi.ingsw.model.table.dice.Die;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Round track is a concrete class representing the round track's game. it has some dice on every cell of the ended turn;
 */
public class RoundTrack {
    private int round;
    private List<ArrayList<Die>> dice;

    /**
     * Creates a roundTrack
     */
    public RoundTrack() {
        round = 1;
        dice = new ArrayList<>();
    }

    /**
     * Gets actual round
     * @return actual round
     */
    public int getRound(){
        return round;
    }

    /**
     * Skips to the next round, ending game in it's over
     * @param d a list of dice
     * @throws EndGameException exception thrown if the game is over
     */
    public void endRound (Collection<Die> d) throws EndGameException {
        if (round <10) {
            round++;
            dice.add(new ArrayList<>(d));
        }else
            throw new EndGameException();

    }

    /**
     * Gets a dice from the roundTrack
     * @param round corresponding to the die or dice to be got
     * @return a clone of the corresponding dice list
     */
    public Collection<Die> getDice(int round){
        return (Collection<Die>)dice.get(round-1).clone();
    }

    /**
     * Switches a die on the roundTrack
     * @param toPlace object die to be placed
     * @param toRemove object die to be removed
     */
    public void switchDie(Die toPlace, Die toRemove){
        for(ArrayList<Die> ar: dice){
            if(ar.contains(toRemove)){
                ar.remove(toRemove);
                ar.add(toPlace);
                return;
            }
        }
    }
    /**
     * Method used for testing
     * @return String representing a round track
     */
    @Override
    public String toString(){
        String ret = "The round track contains:";
        for (int i=1; i<10; i++)
            ret = ret + "Round " + i + ": " + this.getDice(i).toString();
        return ret;
    }

    /**
     * Prints the override toString of an object RoundTrack
     */
    public void dump(){
        System.out.println(this);
    }

}
