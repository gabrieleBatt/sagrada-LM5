package it.polimi.ingsw.server.model.table;

import it.polimi.ingsw.shared.LogMaker;
import it.polimi.ingsw.server.exception.EndGameException;
import it.polimi.ingsw.server.model.table.dice.Die;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Round track is a concrete class representing the round track's game. it has some dice on every cell of the ended turn;
 */
public class RoundTrack implements Memento {

    private static final Logger logger = LogMaker.getLogger(RoundTrack.class.getName(), Level.ALL);
    public static final int ROUND_NUM = 10;
    private List<ArrayList<Die>> dice;
    private Deque<List<ArrayList<Die>>> roundTrackMemento;

    /**
     * Creates a roundTrack
     */
    public RoundTrack() {
        dice = new ArrayList<>();
        roundTrackMemento = new ArrayDeque<>();
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
        logger.log(Level.FINE, "This round: " + (getRound()+(-1)) + " just ended, these dice remained on the table: "+ d, this);
        logger.log(Level.FINE, "Round Track: "+dice.toString());
        if (dice.size() < ROUND_NUM) {
            dice.add(new ArrayList<>(d));
        }else
            throw new EndGameException("Game over, can't add\n"+d.toString());

    }

    /**
     * Gets a dice from the roundTrack
     * @param round corresponding to the die or dice to be got
     * @return a clone of the corresponding dice list
     */
    public Collection<Die> getDice(int round){
        return new ArrayList<>(dice.get(round-1));
    }

    /**
     * Switches a die on the roundTrack
     * @param toPlace object die to be placed
     * @param toRemove object die to be removed
     */
    public void switchDie(Die toPlace, Die toRemove){
        Optional<ArrayList<Die>> ar = dice.stream().filter(l -> l.contains(toRemove)).findFirst();
        if (ar.isPresent()){
            ar.get().remove(toRemove);
            ar.get().add(toPlace);
            logger.log(Level.FINEST, "This die: " + toRemove + " has been changed with this other die: "+ toPlace, this);
        }
    }

    /**
     * Gets the list of the list (one per turn) of dice.
     * @return list of the list (one per turn) of dice.
     */
    public List<List<Die>> getCollections (){
        List<List<Die>> dieList = new ArrayList<>();
        for(int i = 1; i<getRound(); i++) {
            dieList.add(new ArrayList<>());
            dieList.get(i - 1).addAll(getDice(i));
        }
        return dieList;
    }

    /**
     * Method used for testing
     * @return String representing a round track
     */
    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder("The round track contains:");
        for (int i=1; i<getRound(); i++)
            ret.append("Round ").append(i).append(": ").append(this.getDice(i).toString());
        return ret.toString();
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
        List<ArrayList<Die>> old = new ArrayList<>();
        for(ArrayList<Die> ar : roundTrackMemento.peek()) {
            old.add(new ArrayList<>(ar));
        }
        this.dice = old;
    }
}
