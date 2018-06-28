package it.polimi.ingsw.server.model.objective;

import it.polimi.ingsw.server.model.table.dice.DieColor;
import it.polimi.ingsw.shared.LogMaker;
import it.polimi.ingsw.server.exception.EmptyCellException;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * A public objective that awards points for each time the glass window
 * meets the target, specific to the objective.
 * The target is an area in which the possible die values must meet a
 * specific multiplicity
 *
 * For example for Column variety the area is a column:[(0,0),(1,0),(2,0),(3,0)]
 * and the multiplicity is any for the numbers and zero or one for each color.
 */
public final class AreaPublicObjective extends PublicObjective {

    private static final Logger logger = LogMaker.getLogger(AreaPublicObjective.class.getName(), Level.ALL);
    private static final int DIE_MAX = 6;
    private List<Target> targetList;
    private int points;

    /**
     * creates an objective from the data found in json already parsed
     * @param name objective name
     * @param points points the target
     * @param area list of coordinates representing an area in which the multiplicity condition must be met
     * @param multiplicity multiplicity conditions for scoring(listed the numbers form one to six and then the colors in alphabetical order)
     * @throws IllegalArgumentException thrown if the parameters are not correct
     */
    public AreaPublicObjective(String name, int points, List<Coordinate> area, List<List<Integer>> multiplicity) {
        super(name);

        targetList = new ArrayList<>();
        targetList.add(new Target(area, multiplicity));

        this.points = points;
    }

    /**
     * scores the points of the objective only
     * @param glassWindow on witch score the point of the objective
     * @return points scored on the glassWindow
     */
    @Override
    public int scorePoints(GlassWindow glassWindow) {
        int ret = 0;
        for (int i = 0; i < GlassWindow.ROWS; i++) {
            for (int j = 0; j < GlassWindow.COLUMNS; j++) {
                for(Target a: targetList) {
                    if (checkMultiplicityInArea(glassWindow, i, j, a.cells, a.multiplicity)) {
                        ret += points;
                        break;
                    }
                }
            }
        }
        return ret;
    }

    /**
     * Adds a target to score, if any target conditions are met, the points are awarded
     * @param area list of coordinates representing an area in which the multiplicity condition must be met
     * @param multiplicity multiplicity conditions for scoring
     */
    public void addArea(List<Coordinate> area, List<List<Integer>> multiplicity) {
        targetList.add(new Target(area, multiplicity));
        logger.log(Level.FINEST, "This area: " + area + " has been added to targetList", this);
    }


    private static boolean checkMultiplicityInArea(GlassWindow glassWindow, int row, int column, List<Coordinate> area, List<List<Integer>> multiplicity){
        Integer[] actualMultiplicity = new Integer[DIE_MAX + DieColor.values().length];
        for (int i = 0; i < DIE_MAX + DieColor.values().length; i++) {
            actualMultiplicity[i] = 0;
        }
        try {
            for (Coordinate c : area) {
                if(row + c.x >= GlassWindow.ROWS || row + c.x < 0 || column + c.y >= GlassWindow.COLUMNS || column + c.y < 0) return false;
                if(!glassWindow.getCell(row+c.x, column+c.y).isOccupied()) return false;
                //Number multiplicity
                actualMultiplicity[glassWindow.getCell(row+c.x, column+c.y).getDie().getNumber()-1]++;
                //Color multiplicity
                DieColor color = glassWindow.getCell(row+c.x, column+c.y).getDie().getColor();
                actualMultiplicity[Arrays.stream(DieColor.values()).collect(Collectors.toList()).indexOf(color) + DIE_MAX]++;
            }
        }catch (EmptyCellException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }

        for (int i = 0; i < actualMultiplicity.length; i++) {
            if (!multiplicity.get(i).contains(actualMultiplicity[i])) return false;
        }
        return true;
    }

    /**
     * List of cell representing the area and the multiplicity condition to met
     */
    class Target {
        List<Coordinate> cells;
        List<List<Integer>> multiplicity;

        Target(List<Coordinate> area, List<List<Integer>> multiplicity) {
            if( multiplicity.size() != DIE_MAX + DieColor.values().length){
                throw new IllegalArgumentException("Can't make objective\nmultiplicity expected "+DIE_MAX + DieColor.values().length+", found:"+multiplicity.size());
            }
            this.multiplicity = new ArrayList<>();
            for(List<Integer> il: multiplicity){
                this.multiplicity.add(new ArrayList<>(il));
            }

            this.cells = new ArrayList<>(area);
        }
    }
}



