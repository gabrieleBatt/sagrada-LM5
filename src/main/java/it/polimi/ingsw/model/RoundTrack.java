package it.polimi.ingsw.model;

import java.util.List;

public class RoundTrack {
    private int conteggio = 0;

    public int getRound(){
        return conteggio;
    }
    public void endRound (List<Die> d) throws EndGameException {
        if (conteggio <10)
            conteggio++;
        else
            throw new EndGameException();

    }

}
