package it.polimi.ingsw.server.model.table;

import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.dice.DieColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class PoolTest {

    @DisplayName("Add dice to pool")
    @Test
    void addDice() {
        Set diceToBe = new HashSet();
        Pool p = new Pool();
        Die d1= new Die(DieColor.CYAN, 8);
        Die d2= new Die(DieColor.CYAN, 9);
        Die d3= new Die(DieColor.RED, 20);
        diceToBe.add(d1);
        diceToBe.add(d2);
        diceToBe.add(d3);
        p.setDice(diceToBe);
        Assertions.assertEquals(p.getDice(),diceToBe);
        Die d4 = new Die(DieColor.RED, 50);
        p.addDie(d4);
        Assertions.assertTrue(p.getDice().contains(d4));
    }

    @DisplayName("Remove die from pool")
    @Test
    void takeDie() {
        Pool p = new Pool();
        Set dice = new HashSet();
        Die d1 = new Die(DieColor.CYAN, 4);
        Die d2 = new Die(DieColor.CYAN, 6);
        Die d3 = new Die(DieColor.GREEN, 9);
        dice.add(d1);
        dice.add(d2);
        dice.add(d3);

        p.setDice(dice);
        Assertions.assertTrue(p.getDice().contains(d2));

        p.takeDie(d2);
        Assertions.assertFalse(p.getDice().contains(d2));

        dice.remove(d2);
        Assertions.assertEquals(dice,p.getDice());

    }

    @DisplayName("Roll all dice in pool")
    @Test
    void roll() {
        Pool p = new Pool();
        Set dice = new HashSet();
        Die d1 = new Die(DieColor.CYAN, 4);
        Die d2 = new Die(DieColor.CYAN, 6);
        Die d3 = new Die(DieColor.GREEN, 9);
        dice.add(d1);
        dice.add(d2);
        dice.add(d3);

        p.setDice(dice);
        p.roll();
        Assertions.assertEquals(d1.getId().substring(1), "C4");
        Assertions.assertEquals(d2.getId().substring(1), "C6");
        Assertions.assertEquals(d3.getId().substring(1), "G9");
    }

    @DisplayName("Testing memento")
    @Test
    void memento() {
        Pool p = new Pool();
        Set dice = new HashSet();
        Die d1 = new Die(DieColor.CYAN, 4);
        Die d2 = new Die(DieColor.CYAN, 6);
        Die d3 = new Die(DieColor.GREEN, 9);
        dice.add(d1);
        dice.add(d2);
        p.addMemento();
        p.getMemento();
        Assertions.assertEquals(0,p.getDice().size());
        p.setDice(dice);
        Assertions.assertEquals(2,p.getDice().size());
        p.getMemento();
        Assertions.assertEquals(0,p.getDice().size());
        p.setDice(dice);
        p.addMemento();
        dice.add(d3);
        p.getMemento();
        Assertions.assertEquals(2,p.getDice().size());
        dice.remove(d3);
        Assertions.assertTrue(p.getDice().containsAll(dice));
    }
}