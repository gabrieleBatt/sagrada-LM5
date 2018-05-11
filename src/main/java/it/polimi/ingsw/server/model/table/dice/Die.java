package it.polimi.ingsw.server.model.table.dice;
import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.model.exception.NotValidNumberException;
import it.polimi.ingsw.server.model.table.Player;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Die is the concrete class representing a die in the game. It is characterized by a specific color, a number (randomly
 * extracted whether it's not specified) and an Id which can uniquely represent a die. It contains also all the methods
 * regarding the die and the tools which can have an effect on it.
 */
public class Die {

    private static final Logger logger = LogMaker.getLogger(Die.class.getName(), Level.ALL);
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
     * Creates a die, setting just color
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
    public String getId (){
        return number + color.toString() + id;
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
     * * Sets the numeric value of a die
     * @param number numeric value that has to be set
     * @return this
     * @throws NotValidNumberException exception thrown whether
     */
    public Die setNumber(int number) throws NotValidNumberException {
        if (number <1 || number > 6 ){
            throw new NotValidNumberException(number + " is not a valid number");
        }
        else this.number = number;
        logger.log(Level.FINEST, "Die set to "+getId());
        return this;
    }

    /**
     * Rolls the dice setting its numeric value to a random number form 1 to 6
     */
    public void roll(){
        this.number = ThreadLocalRandom.current().nextInt(1, 6 + 1);
        logger.log(Level.FINEST, "Die rolled to "+getId());
    }

    /**
     * Increases the number given unless its value is 6
     * @return this
     * @throws NotValidNumberException exception thrown whether the number is 6
     */
    public Die increase() throws NotValidNumberException{
        if (this.number == 6)
            throw new NotValidNumberException("Die is already at the maximum value");
        else
            this.number++;
        logger.log(Level.FINEST, "Die increased to "+getId());
        return this;
    }

    /**
     * Decreases the number given unless its value is 1
     * @throws NotValidNumberException exception thrown whether the number is 1
     * @return this
     */
    public Die decrease() throws NotValidNumberException{
        if (number == 1)
            throw new NotValidNumberException("Die is already at the minimum value");
        else
            number--;
        logger.log(Level.FINEST, "Die decreased to "+getId());
        return this;
    }

    /**
     * Compares dices id to check if they're the same
     * @return true if dice match
     * @param die: object to match with this die
     */
    @Override
    public boolean equals(Object die){
        if (die instanceof Die){
            return ((Die)die).getId().equals(this.getId());
        }else
            return false;
    }

    /**
     * Method used for testing
     * @return String representing a die
     */
    @Override
    public String toString(){
        return ("Die color:" + this.color + "Die value" + this.number + "Die id" + this.id);
    }

    /**
     * Prints the override toString of an object Die
     */
    public void dump(){
        System.out.println(this);
    }
}
