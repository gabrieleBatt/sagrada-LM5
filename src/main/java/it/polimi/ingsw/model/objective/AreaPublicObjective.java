package it.polimi.ingsw.model.objective;

import it.polimi.ingsw.model.exception.CellNotFoundException;
import it.polimi.ingsw.model.exception.EmptyCellException;
import it.polimi.ingsw.model.exception.IllegalObjectiveException;
import it.polimi.ingsw.model.table.glassWindow.GlassWindow;

import java.util.ArrayList;
import java.util.List;


public class AreaPublicObjective extends PublicObjective {

    private List<Area> areaList;
    private int points;

    public AreaPublicObjective(String name, int points, List<Integer> area, List<List<Integer>> multiplicity) throws IllegalObjectiveException {
        super(name);

        areaList = new ArrayList<>();
        areaList.add(new Area(area, multiplicity));

        this.points = points;
    }

    @Override
    public int scorePoints(GlassWindow glassWindow) {
        int ret = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                for(Area a: areaList) {
                    if (checkMultiplicityInArea(glassWindow, i, j, a.area, a.multiplicity)) {
                        ret += points;
                        break;
                    }
                }
            }
        }
        return ret;
    }

    public void addArea(List<Integer> area, List<List<Integer>> multiplicity) throws IllegalObjectiveException {
        areaList.add(new Area(area, multiplicity));
    }

    private static boolean checkMultiplicityInArea(GlassWindow glassWindow, int row, int column, List<Coordinate> area, List<List<Integer>> multiplicity){
        Integer[] actualMultiplicity = new Integer[11];
        for (int i = 0; i < 11; i++) {
            actualMultiplicity[i] = new Integer(0);
        }
        try {
            for (Coordinate c : area) {
                if(row + c.x >= 4 || row + c.x < 0 || column + c.y >= 5 || column + c.y < 0) return false;
                if(!glassWindow.getCell(row+c.x, column+c.y).isOccupied()) return false;
                actualMultiplicity[glassWindow.getCell(row+c.x, column+c.y).getDie().getNumber()-1]++;
                switch (glassWindow.getCell(row+c.x, column+c.y).getDie().getColor()){
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



    class Coordinate{
        int x;
        int y;

        Coordinate(int x , int y){
            this.x = x;
            this.y = y;
        }
    }

    class Area{
        List<Coordinate> area;
        List<List<Integer>> multiplicity;

        public Area(List<Integer> area, List<List<Integer>> multiplicity) throws IllegalObjectiveException {
            if( multiplicity.size() != 11 || area.size()%2 != 0){
                throw new IllegalObjectiveException("Can't make objective\nmultiplicity expected 11, found:"+multiplicity.size()+"\narea expected even, found:"+ area.size());
            }
            this.multiplicity = new ArrayList<>();
            for(List<Integer> il: multiplicity){
                this.multiplicity.add(new ArrayList<>(il));
            }

            this.area = new ArrayList<>();
            for (int i = 0; i < area.size(); i+=2) {
                this.area.add(new Coordinate(area.get(i), area.get(i+1)));
            }
        }
    }
}


