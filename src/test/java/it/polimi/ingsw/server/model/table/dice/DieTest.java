package it.polimi.ingsw.server.model.table.dice;

import it.polimi.ingsw.server.model.exception.NotValidNumberException;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.dice.DieColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class DieTest {

    @DisplayName("Set die numeric value")
    @Test
    void setNumber() throws NotValidNumberException {
        Die die = new Die(DieColor.CYAN,9);
        for (int i=1; i<=6; i++){
        Assertions.assertTrue(die.setNumber(i).getNumber() == i);
        }
        Assertions.assertThrows(NotValidNumberException.class, () -> die.setNumber(0));
    }


    @DisplayName("Get die id")
    @Test
    void getId() {
        Die die = new Die(DieColor.CYAN, 8);
        Assertions.assertEquals(die.getId(),die.getNumber()+die.getColor().toString()+8);
    }

    @DisplayName("Roll die")
    @Test
    void roll() {
        Die die = new Die(DieColor.CYAN, 8);
        for(int i=0; i<100; i++){
            die.roll();
            Assertions.assertTrue(die.getNumber()<=6 && die.getNumber()>=1);
        }
    }

    @DisplayName("Increase die value")
    @Test
    void increase() throws NotValidNumberException {
        Die die = new Die(DieColor.CYAN, 12);
        for (int i = 1; i < 6; i++) {
            Assertions.assertEquals(die.setNumber(i).getNumber() + 1, die.increase().getNumber());
        }
        Assertions.assertThrows(NotValidNumberException.class, () -> die.setNumber(6).increase());
    }

    @DisplayName("Decrease die value")
    @Test
    void decrease() throws NotValidNumberException {
        Die die = new Die(DieColor.CYAN, 12);
        for (int i = 6; i > 1; i--) {
            Assertions.assertEquals(die.setNumber(i).getNumber() - 1, die.decrease().getNumber());
        }
        Assertions.assertThrows(NotValidNumberException.class, () -> die.setNumber(1).decrease());
    }

    @DisplayName("Confront dice")
    @Test
    void equals() {
        Die die = new Die(DieColor.RED, 6 );
        Die die2 = new Die(DieColor.RED, 9);
        Assertions.assertTrue(die.equals(die));
        Assertions.assertFalse(die.equals(die2));
        Assertions.assertFalse(die.getClass().equals(Object.class));

    }

    @DisplayName("Die to string")
    @Test
    void toStringTest() throws NotValidNumberException {
         Die die = new Die(DieColor.CYAN, 4,12);
         Assertions.assertEquals(die.toString(),"Die color:" + die.getColor() + "Die value" + die.getNumber() + "Die id" + 12);
         //die.dump();
     }
}