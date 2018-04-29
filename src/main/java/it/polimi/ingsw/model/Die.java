package it.polimi.ingsw.model;
import java.util.concurrent.ThreadLocalRandom;

import static it.polimi.ingsw.model.DieColor.*;

public class Die {
    private final DieColor color;
    private int number;
    private final int id;

    public Die(DieColor color, int number, int id) throws NotValidNumberException {
        this.color = color;
        this.setNumber(number);
        this.id = id;
    }

    public Die(DieColor color, int id){
        this.color = color;
        this.id = id;
        roll();

    }

    public int getId (){
        return id;
    }

    public DieColor getColor(){
        return color;
    }

    public int getNumber(){
        return number;
    }

    public void setNumber(int number) throws NotValidNumberException {
        if (number <1 || number > 6 ){
            throw new NotValidNumberException();
        }
        else this.number = number;

    }
    public void roll(){
        this.number = ThreadLocalRandom.current().nextInt(1, 6 + 1);

    }

    public void increase() throws NotValidNumberException{
        if (number == 6)
            throw new NotValidNumberException();
        else
            number++;
    }

    public void decrease() throws NotValidNumberException{
        if (number == 1)
            throw new NotValidNumberException();
        else
            number--;
    }

}
