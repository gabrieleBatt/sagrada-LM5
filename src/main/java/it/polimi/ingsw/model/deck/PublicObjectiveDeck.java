package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.exception.EmptyCellException;
import it.polimi.ingsw.model.exception.NotValidNumberException;
import it.polimi.ingsw.model.objective.PublicObjective;
import it.polimi.ingsw.model.table.DashBoard;
import it.polimi.ingsw.model.table.dice.DieColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PublicObjectiveDeck {


    public static Collection<PublicObjective> draw(int numOfCards) {
        return new ArrayList<>();
    }

    private static PublicObjective makeAreaObjective(String name, int points, List<Coordinate> area, List<List<Integer>> multiplicity) {
        return new PublicObjective(name) {
            @Override
            public int scorePoints(DashBoard dashboard) {
                int ret = 0;
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (checkMultiplicityInArea(dashboard, i, j, area, multiplicity)){
                            ret += points;
                        }
                    }
                }
                return ret;
            }
        };
    }

    private static boolean checkMultiplicityInArea(DashBoard dashboard, int row, int column, List<Coordinate> area, List<List<Integer>> multiplicity){
        Integer[] actualMultiplicity = new Integer[11];
        for (int i = 0; i < 11; i++) {
            actualMultiplicity[i] = new Integer(0);
        }
        try {
            for (Coordinate c : area) {
                if(row + c.x >= 4 || row + c.x < 0 || column + c.y >= 5 || column + c.y < 0) return false;
                if(!dashboard.getCell(row+c.x, column+c.y).isOccupied()) return false;
                actualMultiplicity[dashboard.getCell(row+c.x, column+c.y).getDie().getNumber()-1]++;
                switch (dashboard.getCell(row+c.x, column+c.y).getDie().getColor()){
                    case CYAN: actualMultiplicity[6]++; break;
                    case GREEN: actualMultiplicity[7]++; break;
                    case MAGENTA: actualMultiplicity[8]++; break;
                    case RED: actualMultiplicity[9]++; break;
                    case YELLOW: actualMultiplicity[10]++; break;
                }
            }
        }catch (NotValidNumberException | EmptyCellException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < actualMultiplicity.length; i++) {
            if (!multiplicity.get(i).contains(actualMultiplicity[i])) return false;
        }
        return true;
    }



    private static PublicObjective makeNumberSetObjective(String name, int points, List<Integer> numbers, List<DieColor> colors) {
        return new PublicObjective(name) {
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
        };
    }

    private static PublicObjective makeColorSetObjective(String name, int points, List<DieColor> colors) {
        return new PublicObjective(name) {
            @Override
            public int scorePoints(DashBoard dashboard) {
                int ret = 20;
                for(DieColor dc: colors){
                    int newRet = 0;
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 5; j++) {
                            try {
                                if (dashboard.getCell(i, j).isOccupied()){
                                    if(dashboard.getCell(i, j).getDie().getColor().equals(dc)){
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
        };
    }
}

class Coordinate{
    int x;
    int y;

    Coordinate(int x , int y){
        this.x = x;
        this.y = y;
    }
}