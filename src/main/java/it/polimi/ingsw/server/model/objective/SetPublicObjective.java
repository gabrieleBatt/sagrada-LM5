package it.polimi.ingsw.server.model.objective;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.model.exception.CellNotFoundException;
import it.polimi.ingsw.server.model.exception.EmptyCellException;
import it.polimi.ingsw.server.model.table.dice.DieColor;
import it.polimi.ingsw.server.model.table.glassWindow.GlassWindow;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SetPublicObjective extends PublicObjective {

    private static final Logger logger = LogMaker.getLogger(SetPublicObjective.class.getName(), Level.ALL);
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
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 5; j++) {
                    try {
                        if (glassWindow.getCell(i, j).isOccupied()){
                            if(glassWindow.getCell(i, j).getDie().getNumber() == n){
                                newRet++;
                            }
                        }
                    } catch (CellNotFoundException e) {
                        logger.log(Level.WARNING, e.getMessage(), e);

                    }
                }
            }
            if(ret > newRet){
                ret = newRet;
            }
        }
        for(DieColor dc: colors){
            int newRet = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 5; j++) {
                    try {
                        if (glassWindow.getCell(i, j).isOccupied()){
                            if(glassWindow.getCell(i, j).getDie().getColor().equals(dc)){
                                newRet++;
                            }
                        }
                    } catch (CellNotFoundException e) {
                        logger.log(Level.WARNING, e.getMessage(), e);
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
