package it.polimi.ingsw.model;

import java.util.Optional;


public class Cell {
    private Optional<DieColor> colorRestriction;
    private Optional<Integer> numberRestriction;
    private Optional<Die> die;

    /**
     *Creates a cell with no restrictions
     */
    public Cell(){
        colorRestriction = Optional.empty();
        numberRestriction = Optional.empty();
        die = Optional.empty();
    }

    /**
     * Creates of a cell with color restriction
     * @param restriction: color restriction of the cell
     */
    public Cell(DieColor restriction){
        colorRestriction = Optional.of(restriction);
        numberRestriction = Optional.empty();
        die = Optional.empty();
    }

    /**
     * Creates of a cell with numeric value restriction
     * @param restriction numeric value restriction of the cell
     */
    public Cell(int restriction){
        numberRestriction = Optional.of(restriction);
        colorRestriction = Optional.empty();
        die = Optional.empty();
    }

    /**
     * Aim: disable the restriction of a specific cell
     * @param die: die that has to be positioned
     * @param ignoreRestriction:boolean which indicates whether the restriction
     *                           has to be ignored or not
     * @throws dieNotAllowedException:Exception thrown if die can't be placed despite
     *                                restriction has been ignored
     */
    public void placeDie(Die die, boolean ignoreRestriction) throws dieNotAllowedException {
        if(ignoreRestriction || isAllowed(die)){
            this.die = Optional.of(die);
        }
        else
            throw new dieNotAllowedException();
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
    public Die getDie() throws EmptyCellException {
        if (die.isPresent()){
            return die.get();
        }else
            throw new EmptyCellException();
    }

    /**
     * Checks if a die cen be placed in the cell despite eventual restrictions
     * @param die object die to be placed
     * @return: true if the value can be placed in the cell
     */
    public boolean isAllowed(Die die){
        if (colorRestriction.isPresent() && die.getColor() != colorRestriction.get())
            return false;
        if (numberRestriction.isPresent() && die.getNumber() != numberRestriction.get())
            return false;
        return true;
    }

}
