package it.polimi.ingsw.server.model.objective;

import it.polimi.ingsw.shared.identifiables.Identifiable;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;

/**
 * A generic objective, contains its scoring rules on a glass window
 */
public abstract class Objective implements Identifiable {

    private String name;

    /**
     * @param name name of the objective to create
     */
    public Objective(String name){
        this.name = name;
    }

    /**
     * gets the name of the objective
     * @return the string with the name
     */
    public String getName() {
        return name;
    }


    /**
     * scores the points of the objective only
     * @param glassWindow on witch score the point of the objective
     * @return points scored on the glasswindow
     */
    public abstract int scorePoints(GlassWindow glassWindow);

    /**
     * returns the identifiable unique id
     * @return the id
     */
    @Override
    public String getId(){
        return getName();
    }

}
