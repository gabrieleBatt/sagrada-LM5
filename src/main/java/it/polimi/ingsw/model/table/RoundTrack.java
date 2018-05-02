package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.exception.EndGameException;
import it.polimi.ingsw.model.table.dice.Die;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


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

}
