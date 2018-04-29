package it.polimi.ingsw.model;

import java.util.Optional;

public class Cell {
    private Optional<DieColor> colorRestriction;
    private Optional<Integer> numberRestriction;
    private Optional<Die> die;

    public Cell(){
        colorRestriction = Optional.empty();
        numberRestriction = Optional.empty();
        die = Optional.empty();
    }

    public Cell(DieColor color){
        colorRestriction = Optional.of(color);
        numberRestriction = Optional.empty();
        die = Optional.empty();
    }

    public Cell(int num){
        numberRestriction = Optional.of(num);
        colorRestriction = Optional.empty();
        die = Optional.empty();
    }

    public Cell(int num, DieColor color){
        numberRestriction = Optional.of(num);
        colorRestriction = Optional.of(color);
        die = Optional.empty();
    }

    public void placeDie(Die die, boolean ignoreRestriction) throws dieNotAllowedException {
        if(ignoreRestriction || isAllowed(die)){
            this.die = Optional.of(die);
        }
        else
            throw new dieNotAllowedException();
    }
    public boolean isOccupied(){
        return die.isPresent();
    }
    public Die getDie() throws EmptyCellException {
        if (die.isPresent()){
            return die.get();
        }else
            throw new EmptyCellException();
    }
    public boolean isAllowed(Die die){
        if (colorRestriction.isPresent() && die.getColor() != colorRestriction.get())
            return false;
        if (numberRestriction.isPresent() && die.getNumber() != numberRestriction.get())
            return false;
        return true;
    }

}
