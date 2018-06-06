package it.polimi.ingsw.server.model.table.dice;
import it.polimi.ingsw.shared.LogMaker;
import it.polimi.ingsw.shared.identifiables.Identifiable;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Die is the concrete class representing a die in the game. It is characterized by a specific color, a number (randomly
 * extracted whether it's not specified) and an Id which can uniquely represent a die. It contains also all the methods
 * regarding the die and the tools which can have an effect on it.
 */
public class Die implements Identifiable {

    private static final Logger logger = LogMaker.getLogger(Die.class.getName(), Level.ALL);
    private final DieColor color;
    private int number;
    private final int id;

    /**
     * Creates a die, setting both color and numeric value
     * @param color die color
     * @param number die numeric value
     * @param id die id code
     * @throws IllegalArgumentException exception thrown whether parameters are not legal
     */
    public Die(DieColor color, int number, int id) {
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
    @Override
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
     * @throws IllegalArgumentException exception thrown whether the number is not  between 1 and 6
     */
    public Die setNumber(int number) {
        if (number <1 || number > 6 ){
            throw new IllegalArgumentException(number + " is not a valid number");
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
     * Method used for testing
     * @return String representing a die
     */
    @Override
    public String toString(){
        return getId();
    }

}
