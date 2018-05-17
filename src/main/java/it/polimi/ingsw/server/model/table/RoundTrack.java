package it.polimi.ingsw.server.model.table;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.exception.EndGameException;
import it.polimi.ingsw.server.model.table.dice.Die;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Round track is a concrete class representing the round track's game. it has some dice on every cell of the ended turn;
 */
public class RoundTrack implements Memento {

    private static final Logger logger = LogMaker.getLogger(RoundTrack.class.getName(), Level.ALL);
    private List<ArrayList<Die>> dice;
    private Stack<List<ArrayList<Die>>> roundTrackMemento;

    /**
     * Creates a roundTrack
     */
    public RoundTrack() {
        dice = new ArrayList<>();
        roundTrackMemento = new Stack<>();
    }

    /**
     * Gets actual round
     * @return actual round
     */
    public int getRound(){
        return dice.size()+1;
    }

    /**
     * Skips to the next round, ending game in it's over
     * @param d a list of dice
     * @throws EndGameException exception thrown if the game is over
     */
    public void endRound (Collection<Die> d) {
        if (dice.size() < 10) {
            dice.add(new ArrayList<>(d));
            logger.log(Level.FINEST, "This round: " + (getRound()+(-1)) + " just ended, these dice remained on the table: "+ d, this);
        }else
            throw new EndGameException("Game over, can't add\n"+d.toString());

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
                logger.log(Level.FINEST, "This die: " + toRemove + " has been changed with this other die: "+ toPlace, this);
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

    /**
     * Adds the current round track state at the stack.
     */
    @Override
    public void addMemento() {
        List<ArrayList<Die>> newMemento = new ArrayList<>();
        for(ArrayList<Die> ar : dice) {
            newMemento.add(new ArrayList<>(ar));
        }
        roundTrackMemento.push(newMemento);
    }

    /**
     * Gets the last round track state saved from the stack.
     */
    @Override
    public void getMemento() {
        List<ArrayList<Die>> old = new ArrayList<>(roundTrackMemento.peek());
        for(ArrayList<Die> ar : roundTrackMemento.peek()) {
            old.add(new ArrayList<>(ar));
        }
        this.dice = old;
    }
}
