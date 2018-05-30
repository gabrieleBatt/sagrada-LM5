package it.polimi.ingsw.server.model.objective;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.exception.EmptyCellException;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;

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
     * @throws IllegalArgumentException thrown if the parameters are not correct
     */
    public AreaPublicObjective(String name, int points, List<Coordinate> area, List<List<Integer>> multiplicity) {
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
                    if (checkMultiplicityInArea(glassWindow, i, j, a.cells, a.multiplicity)) {
                        ret += points;
                        break;
                    }
                }
            }
        }
        return ret;
    }

    public void addArea(List<Coordinate> area, List<List<Integer>> multiplicity) {
        areaList.add(new Area(area, multiplicity));
        logger.log(Level.FINEST, "This area: " + area + " has been added to areaList", this);

    }

    private static boolean checkMultiplicityInArea(GlassWindow glassWindow, int row, int column, List<Coordinate> area, List<List<Integer>> multiplicity){
        Integer[] actualMultiplicity = new Integer[11];
        for (int i = 0; i < 11; i++) {
            actualMultiplicity[i] = 0;
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
                    default: break;
                }
            }
        }catch (EmptyCellException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }

        for (int i = 0; i < actualMultiplicity.length; i++) {
            if (!multiplicity.get(i).contains(actualMultiplicity[i])) return false;
        }
        return true;
    }

    class Area{
        List<Coordinate> cells;
        List<List<Integer>> multiplicity;

        public Area(List<Coordinate> area, List<List<Integer>> multiplicity) {
            if( multiplicity.size() != 11){
                throw new IllegalArgumentException("Can't make objective\nmultiplicity expected 11, found:"+multiplicity.size());
            }
            this.multiplicity = new ArrayList<>();
            for(List<Integer> il: multiplicity){
                this.multiplicity.add(new ArrayList<>(il));
            }

            this.cells = new ArrayList<>(area);
        }
    }
}



