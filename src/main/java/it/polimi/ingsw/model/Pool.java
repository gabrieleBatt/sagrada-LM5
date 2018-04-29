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
    public void takeDie(int dieId){
        for(Die d: diceOnTable){
            if (d.getId() == dieId) {
                diceOnTable.remove(d);
                return;
            }
        }
    }
    public void roll(){
        for(Die d: diceOnTable) {
            d.roll();
        }
    }
}
