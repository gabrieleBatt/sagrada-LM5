package it.polimi.ingsw.server.model.table;

import it.polimi.ingsw.server.model.exception.EndGameException;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.dice.DieColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoundTrackTest {

    @DisplayName("End round adding dice")
    @Test
    void endRound() throws EndGameException {
        RoundTrack roundTrack = new RoundTrack();

        Collection<Die> dice = new ArrayList<>();
        Die d1 = new Die(DieColor.YELLOW, 4);

        dice.add(d1);
        for (int i=1; i<15; i++){

            if(i< 10) {
                roundTrack.endRound(dice);
                Assertions.assertEquals( dice,roundTrack.getDice(roundTrack.getRound()-1) );

            }else
                Assertions.assertThrows(EndGameException.class, ()-> roundTrack.endRound(dice));

        }
    }

    @DisplayName("Remove a die from roundTrack placing another in its place")
    @Test
    void switchDie() throws EndGameException {
        RoundTrack roundTrack = new RoundTrack();
        Collection<Die> dice = new ArrayList<>();
        Die d1 = new Die(DieColor.YELLOW, 4);
        Die d2 = new Die(DieColor.YELLOW, 7);
        Die d3 = new Die(DieColor.GREEN, 9);
        dice.add(d1);
        dice.add(d2);
        roundTrack.endRound(dice);

        Collection<Die> dice2 = new ArrayList<>();
        dice2.add(d3);
        roundTrack.endRound(dice2);

        Die d4 = new Die(DieColor.CYAN, 11);

        roundTrack.switchDie(d4,d2);


        Assertions.assertTrue(roundTrack.getDice(1).contains(d4));
        Assertions.assertFalse(roundTrack.getDice(1).contains(d2));
        Assertions.assertTrue(roundTrack.getDice(1).contains(d1));
        Assertions.assertTrue(roundTrack.getDice(2).contains(d3));

    }
}