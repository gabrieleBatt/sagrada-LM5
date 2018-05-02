
package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.exception.DashBoardException;
import it.polimi.ingsw.model.exception.EmptyCellException;
import it.polimi.ingsw.model.exception.NotValidNumberException;
import it.polimi.ingsw.model.table.dice.Die;

import java.util.ArrayList;
import java.util.List;

public class DashBoard {
    private String name;
    private int difficulty;
    private Cell[][] dashBoard;

    public DashBoard(String name, int difficulty){
        this.name = name;
        this.difficulty = difficulty;
    }

    /**
     * Sets the dashBoard cells array (used for testing)
     * @param board two-dimensional array, list of cells
     */
    public void setDashBoard( Cell[][] board){
        this.dashBoard=board;
    }

    /**
     * Gets the dashBoard difficulty
     * @return int, dashBoard difficulty
     */
    public int getDifficulty(){
        return this.difficulty;
    }

    /**
     * Gets the dashBoard name
     * @return String, dashBoard name
     */
    public String getName(){
        return this.name;
    }

    /**
     * Gets the cell at the x,y coordinates
     * @param x column
     * @param y row
     * @return Cell in x,y on the dashBoard
     * @throws NotValidNumberException Exception thrown whether column or row are not valid inputs
     */
    public Cell getCell(int x, int y) throws NotValidNumberException{
        if (x>=0 && x<5 && y>=0 && y<4)
            return dashBoard[x][y];
        else throw new NotValidNumberException();
    }

    /**
     * Gets the cell in which there's a die having dieId Id
     * @param dieId String, value to look for
     * @return cell containing the die having dieId Id
     * @throws DashBoardException Exception thrown whether dieId is not found
     * @throws EmptyCellException Exception thrown whether
     */
    public Cell getCell(String dieId) throws DashBoardException, EmptyCellException {
        boolean found = false;
        Cell cellFound = null;
        for(int j=0; j<4; j++){
            for(int i=0; i<5; i++) {
                if(found == false && dashBoard[i][j].isOccupied() &&
                        dashBoard[i][j].getDie().getId().equals(dieId)) {
                    cellFound = dashBoard[i][j];
                    found = true;
                }
            }
        }
        if(found == true)
            return cellFound;
        else
            throw new DashBoardException();
    }

    /**
     * Checks condition on cells surrounding the one given
     * @param x column
     * @param y row
     * @param die Object, checking conditions on it
     * @return Boolean, true if its possible to place the die on that cell
     * @throws EmptyCellException exception thrown whether an empty cell is accessed
     */
    public boolean areDiceNearby (int x, int y, Die die) throws EmptyCellException {
        boolean diceNearby = false;
        //checking sides
        if(x>0) {
            diceNearby = dashBoard[x-1][y].isOccupied();
            if(!checkNearby(x-1,y,die))
                return false;
            //TODO
        }if(x<4) {
            diceNearby = dashBoard[x+1][y].isOccupied();
            if(!checkNearby(x+1,y,die))
                return false;
        }
        if(y>0) {
            diceNearby = dashBoard[x][y-1].isOccupied();
            if(!checkNearby(x,y-1,die))
                return false;
        }
        if(y<3) {
            diceNearby = dashBoard[x][y+1].isOccupied();
            if(!checkNearby(x,y+1,die))
                return false;
        }
        //checking diagonals
        if(x>0 && y>0 && !diceNearby)
            diceNearby = dashBoard[x-1][y-1].isOccupied();
        if(x>0 && y<4 && !diceNearby)
            diceNearby = dashBoard[x-1][y+1].isOccupied();
        if(x<5 && y>0 && !diceNearby)
            diceNearby = dashBoard[x+1][y-1].isOccupied();
        if(x<5 && y<4 && !diceNearby)
            diceNearby = dashBoard[x+1][y+1].isOccupied();

        return diceNearby;
    }

    /**
     * Checks if there's a cell with the same numeric value or color of the placing die nearby
     * @param sx column
     * @param sy row
     * @param die Object, die considered
     * @return boolean, true if there's no conflict
     * @throws EmptyCellException exception thrown whether an empty cell is accessed
     */
    public boolean checkNearby(int sx, int sy, Die die) throws EmptyCellException {
        boolean AllowedDiceNearby = true;
            if(dashBoard[sx][sy].getDie().getNumber()== die.getNumber()
                    || dashBoard[sx][sy].getDie().getColor().toString().equals(die.getColor().toString()))
                AllowedDiceNearby=false;
        return AllowedDiceNearby;
    }

    /**
     * Finds the list of available cells (on the border) in case there's no die placed yet
     * @param die Object, die considered
     * @return List of available cells
     */
    public List<Cell> emptyDashBoard(Die die){
        List<Cell> availableCells= new ArrayList<Cell>();
        int i;
        int j;
        for(i=0,j=0; i<5; i++){
            if(dashBoard[i][j].isAllowed(die))
                availableCells.add(dashBoard[i][j]);
        }
        for(i=0,j=3; i<5; i++){
            if(dashBoard[i][j].isAllowed(die))
                availableCells.add(dashBoard[i][j]);
        }
        for(i=0,j=1; j<3; j++){
            if(dashBoard[i][j].isAllowed(die))
                availableCells.add(dashBoard[i][j]);
        }
        for(i=4,j=1; j<2; j++){
            if(dashBoard[i][j].isAllowed(die))
                availableCells.add(dashBoard[i][j]);
        }
        return availableCells;
    }

    /**
     * Finds the list of cells where to place the die (parameter) according to rules
     * @param die Object, die considered
     * @return List of available cells
     * @throws EmptyCellException exception thrown whether an empty cell is accessed
     */
    public List<Cell> availableCells(Die die) throws EmptyCellException {
        List<Cell> availableCells= new ArrayList<Cell>();
        Boolean isEmpty = true;
        int i;
        int j;
        for(j=0; j<4; j++)
            for(i=0; i<5; i++)
                if(dashBoard[i][j].isOccupied() && isEmpty)
                    isEmpty = false;

        if(!isEmpty) {
            for(j=0; j<4; j++)
                for(i=0; i<5; i++)
                    if(dashBoard[i][j].isAllowed(die) && areDiceNearby(i,j,die))
                        availableCells.add(dashBoard[i][j]);
        }else{
            availableCells = emptyDashBoard(die);
        }
        return availableCells;

    }
}


