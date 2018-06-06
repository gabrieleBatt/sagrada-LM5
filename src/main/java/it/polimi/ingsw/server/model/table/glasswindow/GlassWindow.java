package it.polimi.ingsw.server.model.table.glasswindow;

import it.polimi.ingsw.shared.identifiables.Identifiable;
import it.polimi.ingsw.server.model.table.dice.Die;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GlassWindow is a concrete class representing a player's glassWindow. It's implemented as a collection of cells.
 */

public class GlassWindow implements Identifiable {

    public static final int ROWS = 4;
    public static final int COLUMNS = 5;
    public static final int CELLS = 20;
    private String name;
    private int difficulty;
    private List<Cell> cellList;

    public GlassWindow(String name, int difficulty, List<Cell> cells) {
        if (cells.size() != CELLS){
            throw new IllegalArgumentException("GlassWindow size can't be"+cells.size());
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
     * @throws IndexOutOfBoundsException Exception thrown whether column or row are not valid inputs
     */
    public Cell getCell(int x, int y){
        if (x>=0 && x<ROWS && y>=0 && y<COLUMNS)
            return cellList.get(x*COLUMNS+y);
        else throw new IndexOutOfBoundsException("Can't find cell at (" +x+", "+y+")");
    }

    /**
     * Gets the cell in which there's a die having dieId Id
     * @param dieId String, value to look for
     * @return cell containing the die having dieId Id
     * @throws NoSuchElementException Exception thrown if dieId is not found
     */
    Cell getCellByDie(String dieId){
        for(Cell c: cellList){
            if (c.isOccupied() && c.getDie().getId().equals(dieId))
                return c;
        }
        throw new NoSuchElementException("There's no die whit the id "+ dieId +"in your glassWindow");
    }

    /**
     * gets the row of the cell
     * @param cell to find the row of
     * @return the row
     */
    int getRow(Cell cell) {
        return cellList.indexOf(cell)/COLUMNS;
    }

    /**
     * gets the column of the cell
     * @param cell to find the column of
     * @return the column
     */
    int getColumn(Cell cell){
        return cellList.indexOf(cell)%COLUMNS;
    }

    /**
     * Checks if there are surrounding dice
     * @param x row
     * @param y column
     * @return Boolean, true if there are surrounding dice
     */
    boolean hasSurroundingDice(int x, int y) {
        for(Cell c: this.getSurrounding(x, y)){
            if(c.isOccupied()){
                return true;
            }
        }
        return false;
    }

    private Collection<Cell> getSurrounding(int x, int y){
        Set<Cell> ret = new HashSet<>(this.getAdjacent(x, y));
        if(x<ROWS-1 && y<COLUMNS-1) ret.add(this.getCell(x+1,y+1));
        if(x<ROWS-1 && y>0) ret.add(this.getCell(x+1,y-1));
        if(x>0 && y<COLUMNS-1) ret.add(this.getCell(x-1,y+1));
        if(x>0 && y>0) ret.add(this.getCell(x-1,y-1));
        return ret;
    }

    private Collection<Cell> getAdjacent(int x, int y){
        Set<Cell> ret = new HashSet<>();
        if(x>0)ret.add(this.getCell(x-1,y));
        if(x<ROWS-1)ret.add(this.getCell(x+1,y));
        if(y>0)ret.add(this.getCell(x,y-1));
        if(y<COLUMNS-1)ret.add(this.getCell(x,y+1));
        return ret;
    }

    /**
     * Checks if there's a cell with the same numeric value or color of the placing die nearby
     * @param x column
     * @param y row
     * @param die Die considered for placing
     * @return boolean, true if there's conflict
     */
    boolean hasAdjacentSimilar(int x, int y, Die die){
        for(Cell c: this.getAdjacent(x, y)){
            if(c.isOccupied() &&
                    (c.getDie().getColor().equals(die.getColor()) || c.getDie().getNumber() == die.getNumber())){
                    return true;
            }
        }
        return false;
    }

    /**
     * Finds the collection of cells on the border
     * @return Collection of available cells
     */
    private Collection<Cell> borderCells(){
        return (new ArrayList<>(cellList)).stream()
                .filter(c -> ((this.getColumn(c) == 0) || (this.getColumn(c) == COLUMNS-1)
                || (this.getRow(c) == 0) || (this.getRow(c) == 3)))
                .collect(Collectors.toList());
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
                !hasAdjacentSimilar(getRow(c), getColumn(c), die)
                        && (hasSurroundingDice(getRow(c), getColumn(c)) || ignoredSurroundingRestriction)
                        && !c.isOccupied()
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
        return true;
    }

    /**
     * Method used for testing
     * @return String representing a glassWindow
     */
    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder("This glassWindow contains\n");
            for(int i = 0; i<ROWS;i++) {
                for (int j = 0; j < COLUMNS; j++) {
                    if (this.getCell(i, j).isOccupied())
                        ret.append(this.getCell(i, j).getDie().toString()).append("  ");
                    else
                        ret.append("[ ]  ");
                }
                ret.append("\n");
            }
            return ret.toString();
    }

    /**
     * Gets the list of the cells of the glass window.
     * @return list of the cells of te glass window.
     */
    public List<Cell> getCellList() {
        return new ArrayList<>(cellList);
    }

    /**
     * returns the identifiable unique id
     * @return the id
     */
    @Override
    public String getId() {
        return name;
    }
}


