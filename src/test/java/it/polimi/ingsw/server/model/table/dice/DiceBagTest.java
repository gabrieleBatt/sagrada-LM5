package it.polimi.ingsw.server.model.table.dice;

import it.polimi.ingsw.server.model.exception.BagEmptyException;
import it.polimi.ingsw.server.model.table.dice.DiceBag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

class DiceBagTest {

    @DisplayName("Create and empty the bag")
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

    @DisplayName("Draw multiple times multiple dice")
    @Test
    void drawDice() throws BagEmptyException {
        DiceBag db = new DiceBag();
        Collection<Die> diceDrown = new ArrayList<Die>();
        int n = 90;
        for (int i = 5; db.bagSize() > 0;) {
            diceDrown = db.drawDice(i);
            for (Die d : diceDrown){Assertions.assertFalse(db.bagContains(d));}
            n -= i;
            Assertions.assertTrue(db.bagSize() == n);
        }
        Assertions.assertThrows(BagEmptyException.class, () -> db.drawDice(5));

    }

    @DisplayName("Place one extra die in the bag")
    @Test
    void placeDie() {
        DiceBag db = new DiceBag();
        Die die = new Die(DieColor.CYAN, 9);
        Assertions.assertEquals(db.bagSize(), 90 );
        db.placeDie(die);
        Assertions.assertEquals(db.bagSize(), 91);
        Assertions.assertTrue(db.bagContains(die));
    }

}