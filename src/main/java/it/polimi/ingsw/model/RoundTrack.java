package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;


public class RoundTrack {
    private int round;
    private List<ArrayList<Die>> dice;

    public RoundTrack() {
        round = 1;
        dice = new ArrayList<>();
    }

    public int getRound(){
        return round;
    }
    public void endRound (List<Die> d) throws EndGameException {
        if (round <10) {
            round++;
            dice.add(new ArrayList<>(d));
        }else
            throw new EndGameException();

    }
    public List<Die> getDice(int round){
        return (List<Die>)dice.get(round).clone();
    }

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
