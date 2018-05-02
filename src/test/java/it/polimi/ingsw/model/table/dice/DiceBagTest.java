package it.polimi.ingsw.model.table.dice;

import it.polimi.ingsw.model.exception.BagEmptyException;
import it.polimi.ingsw.model.table.dice.DiceBag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class DiceBagTest {


    @Test
    void drawDie() throws BagEmptyException {
        DiceBag db = new DiceBag();
        Set<String> sd = new HashSet<>();
        Assertions.assertTrue(db.bagSize() == 90);
        for (int i = 0; i < 90; i++) {
            sd.add(new ArrayList<>(db.drawDice(1)).get(0).getId().substring(1));
        }
        Assertions.assertTrue(db.bagSize() == 0);
        Assertions.assertTrue(sd.size() == 90);
    }

    @Test
    void drawDice() throws BagEmptyException {
        DiceBag db = new DiceBag();
        int n = 90;
        for (int i = 0; i < 10; i++) {
            db.drawDice(i);
            n -= i;
            Assertions.assertTrue(db.bagSize() == n);
        }
    }

    @Test
    void placeDie() {
    }
}