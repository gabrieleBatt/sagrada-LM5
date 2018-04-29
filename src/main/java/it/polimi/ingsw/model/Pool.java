package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Pool {
    private List<Die> diceOnTable;

    public List<Die> getDice(){
        return new ArrayList<>(diceOnTable);
    }
    public void addDice(List<Die> diceOnTable){
        this.diceOnTable = diceOnTable;
    }
    public void takeDie(Die die){
        diceOnTable.remove(die);
    }
    public void roll(){
        for(Die d: diceOnTable) {
            d.roll();
        }
    }
}
