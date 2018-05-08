package it.polimi.ingsw.model.objective;

import it.polimi.ingsw.model.exception.EmptyCellException;
import it.polimi.ingsw.model.exception.NotValidNumberException;
import it.polimi.ingsw.model.table.DashBoard;
import it.polimi.ingsw.model.table.dice.DieColor;

public class ColorPrivateObjective extends PrivateObjective {

    private DieColor color;

    public ColorPrivateObjective(String name, DieColor color) {
        super(name);
    }

    /**
     * Returns the score the player gets due to the private objective
     * @param dashboard
     * @return
     */
    @Override
    public int scorePoints(DashBoard dashboard) {
        int ret = 0;
        for (int i = 0; i < 4; i++) {
            for (int j= 0; j < 5; j++) {
                try {
                    if (dashboard.getCell(i, j).isOccupied()) {
                        if (dashboard.getCell(i, j).getDie().getColor().equals(color)) {
                            ret += dashboard.getCell(i, j).getDie().getNumber();
                        }
                    }
                } catch (NotValidNumberException | EmptyCellException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }
}
