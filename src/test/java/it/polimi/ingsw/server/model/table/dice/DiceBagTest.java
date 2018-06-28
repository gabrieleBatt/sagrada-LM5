package it.polimi.ingsw.server.model.table.dice;

import it.polimi.ingsw.server.exception.BagEmptyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

class DiceBagTest {

    @DisplayName("Create and empty the bag")
    @Test
    void drawDie() {
        DiceBag db = new DiceBag();
        Set<String> sd = new HashSet<>();
        Assertions.assertEquals(90, db.bagSize());
        for (int i = 0; i < 90; i++) {
            sd.add(new ArrayList<>(db.drawDice(1)).get(0).getId().substring(1));
        }
        Assertions.assertEquals(0, db.bagSize());
        Assertions.assertEquals(90, sd.size());
    }

    @DisplayName("Draw multiple times multiple dice")
    @Test
    void drawDice() {
        DiceBag db = new DiceBag();
        Collection<Die> diceDrown = new ArrayList<Die>();
        int n = 90;
        for (int i = 5; db.bagSize() > 0;) {
            diceDrown = db.drawDice(i);
            for (Die d : diceDrown){Assertions.assertFalse(db.bagContains(d));}
            n -= i;
            Assertions.assertEquals(db.bagSize(), n);
        }
        Assertions.assertThrows(BagEmptyException.class, () -> db.drawDice(5));
    }

    @DisplayName("Place one extra die in the bag")
    @Test
    void placeDie() {
        DiceBag db = new DiceBag();
        Die die = new Die(DieColor.CYAN, 9);
        Assertions.assertEquals(90, db.bagSize() );
        db.placeDie(die);
        Assertions.assertEquals(91, db.bagSize());
        Assertions.assertTrue(db.bagContains(die));
        Assertions.assertThrows(BagEmptyException.class, () -> db.drawDice(100));
    }

    @DisplayName("Testing memento")
    @Test
    void memento() {
        DiceBag db = new DiceBag();
        Die die = new Die(DieColor.CYAN, 9);
        db.addMemento();
        db.placeDie(die);
        db.getMemento();
        Assertions.assertEquals(90,db.bagSize());
        db.drawDice(10);
        db.addMemento();
        db.drawDice(20);
        Assertions.assertEquals(60,db.bagSize());
        db.getMemento();
        Assertions.assertEquals(80,db.bagSize());
    }
}