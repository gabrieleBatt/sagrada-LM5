package it.polimi.ingsw.model.objective;

import it.polimi.ingsw.model.exception.CellNotFoundException;
import it.polimi.ingsw.model.exception.EmptyCellException;
import it.polimi.ingsw.model.exception.IllegalObjectiveException;
import it.polimi.ingsw.model.exception.NotValidNumberException;
import it.polimi.ingsw.model.table.DashBoard;

import java.util.ArrayList;
import java.util.List;

public class AreaPublicObjective extends PublicObjective {

    private List<Coordinate> area;
    private List<List<Integer>> multiplicity;
    private int points;

    //TODO improve class
    public AreaPublicObjective(String name, int points, List<Integer> area, List<List<Integer>> multiplicity) throws IllegalObjectiveException {
        super(name);
        if(area.size()%2 != 0 || multiplicity.size() != 11){
            throw new IllegalObjectiveException("Can't make objective\narea:"+ area.size()+"\nmult:"+multiplicity.size());
        }
        this.area = new ArrayList<>();
        for (int i = 0; i < area.size(); i+=2) {
            this.area.add(new Coordinate(area.get(i), area.get(i+1)));
        }
        this.points = points;
        this.multiplicity = new ArrayList<>(multiplicity);
    }

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
        }catch (CellNotFoundException | EmptyCellException e) {
            System.out.println(e.getMessage());
        }

        for (int i = 0; i < actualMultiplicity.length; i++) {
            if (!multiplicity.get(i).contains(actualMultiplicity[i])) return false;
        }
        return true;
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
