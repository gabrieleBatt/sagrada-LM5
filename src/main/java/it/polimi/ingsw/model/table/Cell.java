package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.exception.EmptyCellException;
import it.polimi.ingsw.model.exception.DieNotAllowedException;
import it.polimi.ingsw.model.table.dice.Die;
import it.polimi.ingsw.model.table.dice.DieColor;

import java.util.Optional;

public class Cell {
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
    public void placeDie(Die die, boolean ignoreRestriction) throws DieNotAllowedException {
        if(ignoreRestriction || isAllowed(die)){
            this.die = Optional.of(die);
        }
        else
            throw new DieNotAllowedException();
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
        return !((colorRestriction.isPresent() && die.getColor() != colorRestriction.get()) ||
        (numberRestriction.isPresent() && die.getNumber() != numberRestriction.get()));
    }

    /**
     * gets the cell id
     * @return  the string id
     */
    public String getId() {
        return id;
    }

    /**
     * checks if two cells are the same by id
     * @param cell
     * @return true if cells has the same id
     */
    @Override
    public boolean equals(Object cell) {
        if (cell instanceof Cell){
            return ((Cell) cell).getId().equals(this.getId());
        }else
            return false;
    }
}
