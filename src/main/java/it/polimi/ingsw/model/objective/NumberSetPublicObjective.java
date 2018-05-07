package it.polimi.ingsw.model.objective;

import it.polimi.ingsw.model.exception.EmptyCellException;
import it.polimi.ingsw.model.exception.NotValidNumberException;
import it.polimi.ingsw.model.table.DashBoard;

import java.util.Collection;
import java.util.HashSet;

public class NumberSetPublicObjective extends PublicObjective {

    private int points;
    private Collection<Integer> numbers;

    public NumberSetPublicObjective(String name, int points, Collection<Integer> numbers) {
        super(name);
        this.points = points;
        this.numbers = new HashSet<>(numbers);
    }

    @Override
    public int scorePoints(DashBoard dashboard) {
        int ret = 20;
        for(Integer n: numbers){
            int newRet = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 5; j++) {
                    try {
                        if (dashboard.getCell(i, j).isOccupied()){
                            if(dashboard.getCell(i, j).getDie().getNumber() == n){
                                newRet++;
                            }
                        }
                    } catch (NotValidNumberException | EmptyCellException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(ret > newRet){
                ret = newRet;
            }
        }
        return ret*points;
    }
}
