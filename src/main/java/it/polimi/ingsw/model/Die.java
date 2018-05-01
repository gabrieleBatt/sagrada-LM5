package it.polimi.ingsw.model;
import java.util.concurrent.ThreadLocalRandom;

import static it.polimi.ingsw.model.DieColor.*;

public class Die {
    private final DieColor color;
    private int number;
    private final int id;

    /**
     * Creates a die, setting both color and numeric value
     * @param color die color
     * @param number die numeric value
     * @param id die id code
     * @throws NotValidNumberException exception thrown whether parameters are not legal
     */
    public Die(DieColor color, int number, int id) throws NotValidNumberException {
        this.color = color;
        this.setNumber(number);
        this.id = id;
    }

    /**
     * Creates a die, setting just color (useful with tools)
     * @param color die color
     * @param id die id code
     */
    public Die(DieColor color, int id){
        this.color = color;
        this.id = id;
        roll();

    }

    /**
     * Gets the die ID
     * @return: int, die ID code
     */
    public int getId (){
        return id;
    }

    /**
     * Gets the die color
     * @return: color of the die
     */
    public DieColor getColor(){
        return color;
    }

    /**
     * Gets die numeric value
     * @return: int, numeric value of the die
     */
    public int getNumber(){
        return number;
    }

    /**
     * Sets the numeric value of a die
     * @param number numeric value that has to be set
     * @throws NotValidNumberException exception thrown whether
     */
    public void setNumber(int number) throws NotValidNumberException {
        if (number <1 || number > 6 ){
            throw new NotValidNumberException();
        }
        else this.number = number;

    }

    /**
     * Rolls the dice setting its numeric value to a random number form 1 to 6
     */
    public void roll(){
        this.number = ThreadLocalRandom.current().nextInt(1, 6 + 1);

    }

    /**
     * Increases the number given unless its value is 6
     * @throws NotValidNumberException exception thrown whether the number is 6
     */
    public void increase() throws NotValidNumberException{
        if (number == 6)
            throw new NotValidNumberException();
        else
            number++;
    }

    /**
     * Decreases the number given unless its value is 1
     * @throws NotValidNumberException exception thrown whether the number is 1
     */
    public void decrease() throws NotValidNumberException{
        if (number == 1)
            throw new NotValidNumberException();
        else
            number--;
    }

    /**
     * Compares dices id to check if they're the same
     * @param die: object to match with this the
     * @return true if dice match
     */
    @Override
    public boolean equals(Object die){
        if (die instanceof Die){
            return ((Die)die).getId() == this.getId();
        }else
            return false;
    }
}
