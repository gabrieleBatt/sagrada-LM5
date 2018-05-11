
package it.polimi.ingsw.model.table.glassWindow;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.model.exception.CellNotFoundException;
import it.polimi.ingsw.model.exception.EmptyCellException;
import it.polimi.ingsw.model.exception.IllegalGlassWindowException;
import it.polimi.ingsw.model.objective.SetPublicObjective;
import it.polimi.ingsw.model.table.dice.Die;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * GlassWindow is a concrete class representing a player's glassWindow. It's implemented as a collection of cells.
 */

public class GlassWindow {

    private static final Logger logger = LogMaker.getLogger(GlassWindow.class.getName(), Level.ALL);
    private String name;
    private int difficulty;
    private List<Cell> cellList;

    public GlassWindow(String name, int difficulty, List<Cell> cells) throws IllegalGlassWindowException {
        if (cells.size() != 20){
            throw new IllegalGlassWindowException("GlassWindow size can't be"+cells.size());
        }
        this.name = name;
        this.difficulty = difficulty;
        this.cellList = new ArrayList<>(cells);
    }

    /**
     * Gets the glassWindow difficulty
     * @return int, glassWindow difficulty
     */
    public int getDifficulty(){
        return this.difficulty;
    }

    /**
     * Gets the glassWindow name
     * @return String, glassWindow name
     */
    public String getName(){
        return this.name;
    }

    /**
     * Gets the cell at the x,y coordinates, the top left is the (0, 0)
     * @param x column
     * @param y row
     * @return Cell in x,y on the glassWindow
     * @throws CellNotFoundException Exception thrown whether column or row are not valid inputs
     */
    public Cell getCell(int x, int y) throws CellNotFoundException{
        if (x>=0 && x<4 && y>=0 && y<5)
            return cellList.get(x*5+y);
        else throw new CellNotFoundException("Can't find cell at (" +x+", "+y+")");
    }

    /**
     * Gets the cell in which there's a die having dieId Id
     * @param dieId String, value to look for
     * @return cell containing the die having dieId Id
     * @throws CellNotFoundException Exception thrown if dieId is not found
     */
    public Cell getCellByDie(String dieId) throws CellNotFoundException{
        for(Cell c: cellList){
            if (c.isOccupied()){
                try {
                    if (c.getDie().getId().equals(dieId)){
                        return c;
                    }
                } catch (EmptyCellException e) {
                    logger.log(Level.WARNING, e.getMessage(), e);
                }
            }
        }
        throw new CellNotFoundException("There's no die whit the id "+ dieId +"in your glassWindow");
    }

    /**
     * gets the row of the cell
     * @param cell
     * @return the row
     * @throws CellNotFoundException if the cell is not in this glassWindow
     */
    public int getRow(Cell cell) throws CellNotFoundException {
        for (int i = 0; i < cellList.size(); i++) {
            if(cellList.get(i).equals(cell)){
                return i/5;
            }
        }
        throw new CellNotFoundException("The cell"+ cell.toString() +"is not in your glassWindow");
    }

    /**
     * gets the column of the cell
     * @param cell
     * @return the column
     * @throws CellNotFoundException if the cell is not in this glassWindow
     */
    public int getColumn(Cell cell) throws CellNotFoundException {
        for (int i = 0; i < cellList.size(); i++) {
            if(cellList.get(i).equals(cell)){
                return i%5;
            }
        }
        throw new CellNotFoundException("The cell"+ cell.toString() +"is not in your glassWindow");
    }

    /**
     * Checks if there are surrounding dice
     * @param x row
     * @param y column
     * @return Boolean, true if there are surrounding dice
     */
    public boolean hasSurroundingDice (int x, int y) {
        for(Cell c: this.getSurrounding(x, y)){
            if(c.isOccupied()){
                return true;
            }
        }
        return false;
    }

    private Collection<Cell> getSurrounding(int x, int y){
        Set<Cell> ret = new HashSet<>(this.getAdjacent(x, y));
        try {
            if(x<3 && y<4)ret.add(this.getCell(x+1,y+1));
            if(x<3 && y>0)ret.add(this.getCell(x+1,y-1));
            if(x>0 && y<4)ret.add(this.getCell(x-1,y+1));
            if(x>0 && y>0)ret.add(this.getCell(x-1,y-1));
        } catch (CellNotFoundException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return ret;
    }

    private Collection<Cell> getAdjacent(int x, int y){
        Set<Cell> ret = new HashSet<>();
        try {
            if(x>0)ret.add(this.getCell(x-1,y));
            if(x<3)ret.add(this.getCell(x+1,y));
            if(y>0)ret.add(this.getCell(x,y-1));
            if(y<4)ret.add(this.getCell(x,y+1));
        } catch (CellNotFoundException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return ret;
    }

    /**
     * Checks if there's a cell with the same numeric value or color of the placing die nearby
     * @param x column
     * @param y row
     * @param die Die considered for placing
     * @return boolean, true if there's conflict
     */
    public boolean hasAdjacentSimilar(int x, int y, Die die){
        for(Cell c: this.getAdjacent(x, y)){
            if(c.isOccupied()){
                try {
                    if (c.getDie().getColor().equals(die.getColor()) || c.getDie().getNumber() == die.getNumber()){
                        return true;
                    }
                } catch (EmptyCellException e) {
                    logger.log(Level.WARNING, e.getMessage(), e);
                }
            }
        }
        return false;
    }

    /**
     * Finds the collection of cells on the border
     * @return Collection of available cells
     */
    private Collection<Cell> borderCells(){
        return (new ArrayList<Cell>(cellList)).stream().filter(c -> {
            try {
                return ((this.getColumn(c) == 0) || (this.getColumn(c) == 4)
                                || (this.getRow(c) == 0) || (this.getRow(c) == 3));
            } catch (CellNotFoundException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            return false;
        }).collect(Collectors.toList());
    }


    /**
     * Finds the list of cells where to place the die according to adjacency restriction
     * @param die die considered for placing
     * @param ignoredSurroundingRestriction true to not consider surrounding restriction
     * @return Collection of available cells
     */
    public Collection<Cell> availableCells(Die die, boolean ignoredSurroundingRestriction){
        if(this.isEmpty())
            return borderCells();
        return (new ArrayList<>(cellList)).stream().filter(c ->
                {
                    try {
                        return !hasAdjacentSimilar(getRow(c), getColumn(c), die)
                                && (hasSurroundingDice(getRow(c), getColumn(c)) || ignoredSurroundingRestriction)
                                && !c.isOccupied();
                    } catch (CellNotFoundException e) {
                        logger.log(Level.WARNING, e.getMessage(), e);
                    }
                    return false;
                }
        ).collect(Collectors.toList());

    }

    /**
     * Checks if glassWindow is empty
     * @return true if there's no die placed
     */
    private boolean isEmpty(){
        for(Cell c: this.cellList){
            if(c.isOccupied())
                return false;
        }
        return  true;
    }

    /**
     * Method used for testing
     * @return String representing a glassWindow
     */
    @Override
    public String toString(){
        String ret = "This glassWindow contains\n";
            for(int i = 0; i<4;i++) {
                for (int j = 0; j < 5; j++) {
                    try {
                        if (this.getCell(i, j).isOccupied())
                            ret = ret + this.getCell(i, j).getDie().toString() + "  ";
                        else
                            ret = ret + "empty  ";
                    } catch (EmptyCellException | CellNotFoundException e) {
                        logger.log(Level.WARNING, e.getMessage(), e);

                    }
                }
                ret = ret + "\n";
            }
            return ret;
    }

    /**
     * Prints the override toString of an object GlassWindow
     */
    public void dump(){
        System.out.println(this);
    }
}


