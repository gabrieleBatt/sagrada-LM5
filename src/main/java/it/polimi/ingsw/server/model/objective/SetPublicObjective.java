package it.polimi.ingsw.server.model.objective;

import it.polimi.ingsw.server.model.table.dice.DieColor;
import it.polimi.ingsw.server.model.table.glasswindow.Cell;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;

import java.util.Collection;
import java.util.HashSet;

public class SetPublicObjective extends PublicObjective {

    private int points;
    private Collection<Integer> numbers;
    private Collection<DieColor> colors;

    public SetPublicObjective(String name, int points, Collection<Integer> numbers, Collection<DieColor> colors) {
        super(name);
        this.points = points;
        this.numbers = new HashSet<>(numbers);
        this.colors = new HashSet<>(colors);
    }

    @Override
    public int scorePoints(GlassWindow glassWindow) {
        int ret = 20;
        for(Integer n: numbers){
            int newRet = 0;
            for (Cell cell : glassWindow.getCellList()) {
                if (cell.isOccupied() && cell.getDie().getNumber() == n)
                    newRet++;
            }
            ret = min(ret, newRet);
        }
        for(DieColor dc: colors){
            int newRet = 0;
            for (Cell cell : glassWindow.getCellList()) {
                if (cell.isOccupied() && cell.getDie().getColor() == dc)
                    newRet++;
            }
            ret = min(ret, newRet);
        }
        return ret*points;
    }

    private int min(int x, int y){
        if(x<y){
            return x;
        }else
            return y;
    }
}
