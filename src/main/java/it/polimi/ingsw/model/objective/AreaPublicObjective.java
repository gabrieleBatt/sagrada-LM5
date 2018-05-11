package it.polimi.ingsw.model.objective;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.controller.Lobby;
import it.polimi.ingsw.model.exception.CellNotFoundException;
import it.polimi.ingsw.model.exception.EmptyCellException;
import it.polimi.ingsw.model.exception.IllegalObjectiveException;
import it.polimi.ingsw.model.table.glassWindow.GlassWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AreaPublicObjective extends PublicObjective {

    private static final Logger logger = LogMaker.getLogger(AreaPublicObjective.class.getName(), Level.ALL);
    private List<Area> areaList;
    private int points;

    /**
     * creates an objective from the data found in json already parsed
     * @param name objective name
     * @param points points for area respecting multiplicity
     * @param area list of coordinates representing an area
     * @param multiplicity multiplicity of die values(listed the numbers form one to six and then the colors in alphabetical order)
     * @throws IllegalObjectiveException thrown if the parameters are not correct
     */
    public AreaPublicObjective(String name, int points, List<Coordinate> area, List<List<Integer>> multiplicity) throws IllegalObjectiveException {
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

    public void addArea(List<Coordinate> area, List<List<Integer>> multiplicity) throws IllegalObjectiveException {
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
            logger.log(Level.WARNING, e.getMessage(), e);
        }

        for (int i = 0; i < actualMultiplicity.length; i++) {
            if (!multiplicity.get(i).contains(actualMultiplicity[i])) return false;
        }
        return true;
    }

    class Area{
        List<Coordinate> area;
        List<List<Integer>> multiplicity;

        public Area(List<Coordinate> area, List<List<Integer>> multiplicity) throws IllegalObjectiveException {
            if( multiplicity.size() != 11){
                throw new IllegalObjectiveException("Can't make objective\nmultiplicity expected 11, found:"+multiplicity.size());
            }
            this.multiplicity = new ArrayList<>();
            for(List<Integer> il: multiplicity){
                this.multiplicity.add(new ArrayList<>(il));
            }

            this.area = new ArrayList<>(area);
        }
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


