package it.polimi.ingsw.server.model.table.glasswindow;

import it.polimi.ingsw.shared.LogMaker;
import it.polimi.ingsw.shared.identifiables.Identifiable;
import it.polimi.ingsw.server.exception.EmptyCellException;
import it.polimi.ingsw.server.exception.DieNotAllowedException;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.dice.DieColor;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cell is a concrete class representing a single cell of a glasswindow. It has information about restrictions and the
 * eventual presence of a die on.
 */
public class Cell implements Identifiable {

    private static final Logger logger = LogMaker.getLogger(Cell.class.getName(), Level.ALL);
    private Optional<DieColor> colorRestriction;
    private Optional<Integer> numberRestriction;
    private Optional<Die> die;
    private final String id;

    /**
     *Creates a cell with no restrictions
     */
    public Cell(String id){
        this.id = id;
        colorRestriction = Optional.empty();
        numberRestriction = Optional.empty();
        die = Optional.empty();
    }

    /**
     * Creates of a cell with color restriction
     * @param restriction: color restriction of the cell
     */
    public Cell(String id, DieColor restriction){
        this.id = id;
        colorRestriction = Optional.of(restriction);
        numberRestriction = Optional.empty();
        die = Optional.empty();
    }

    /**
     * Creates of a cell with numeric value restriction
     * @param restriction numeric value restriction of the cell
     */
    public Cell(String id, int restriction){
        this.id = id;
        numberRestriction = Optional.of(restriction);
        colorRestriction = Optional.empty();
        die = Optional.empty();
    }

    /**
     * Aim: disable the restriction of a specific cell
     * @param die: die that has to be positioned
     * @param ignoreRestriction:boolean which indicates whether the restriction has to be ignored or not
     * @throws DieNotAllowedException :Exception thrown if die can't be placed due to restrictions
     */
    public void placeDie(Die die, boolean ignoreRestriction) {
        if(ignoreRestriction || (isAllowed(die.getNumber()) && isAllowed(die.getColor()))){
            this.die = Optional.of(die);
            logger.log(Level.FINEST, "This die "+ die.getId()+ " has been placed in this cell:" + this.getId(), this);
        }
        else
            throw new DieNotAllowedException(this.toString()+ "\n" + die.toString() + " is not allowed");
    }

    /**
     * Indicates whether the cell is occupied or not
     * @return: boolean, true if the cell is occupied
     */
    public boolean isOccupied(){
        return die.isPresent();
    }

    /**
     * Gets the die which occupies a cell, if it's present
     * @return: object die in the cell
     * @throws EmptyCellException thrown if the cell is empty
     */
    public Die getDie() {
        if (die.isPresent()){
            return die.get();
        }else
            throw new EmptyCellException(die.toString() +"\nis not present");
    }

    /**
     * Removes a die from a cell.
     */
    public void removeDie(){
        die = Optional.empty();
    }

    /**
     * Checks if a die cen be placed in the cell despite eventual restrictions
     * @param number numeric value of die to place
     * @return: true if the value can be placed in the cell
     */
    public boolean isAllowed(Integer number){
        return !(numberRestriction.isPresent() && !number.equals(numberRestriction.get()));
    }

    /**
     * Checks if a die cen be placed in the cell despite eventual restrictions
     * @param color color of die to place
     * @return: true if the value can be placed in the cell
     */
    public boolean isAllowed(DieColor color){
        return !(colorRestriction.isPresent() && !color.equals(colorRestriction.get()));
    }

    /**
     * gets the cell id
     * @return  the string id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Method used for testing
     * @return String representing a cell
     */
    @Override
    public String toString(){
        String ret = "This cell:";
        if(this.colorRestriction.isPresent())
            ret = ret + "has a color restriction:" +this.colorRestriction.get()+"," ;
        if(this.numberRestriction.isPresent())
            ret = ret + "has a number restriction:"+this.numberRestriction.get()+"," ;
        if(this.isOccupied()) {
            ret = ret + "contains a die:" + this.getDie().toString();
        }else
            ret = ret + "is empty";
        return ret;
    }

    public void placeOptionalDie(Optional<Die> die){
        this.die = die;
    }
}
