package it.polimi.ingsw.model.tool;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.model.rules.ActionCommand;
import it.polimi.ingsw.model.table.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Tool {

    private static final Logger logger = LogMaker.getLogger(Tool.class.getName(), Level.ALL);
    private ActionCommand actionCommand;
    private String name;
    private boolean used;

    /**
     * Creates a tool
     * @param actionCommand action that has to be performed by the tool
     */
    public Tool(ActionCommand actionCommand, String name){
        this.name = name;
        this.actionCommand = actionCommand;
        used = false;
    }

    /**
     * Gets tool name
     * @return tool name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets action command
     * @return tool action command
     */
    public ActionCommand getActionCommand() {
        return actionCommand;
    }

    /**
     * Sets tool use
     * @param used boolean, set true if the tool is used
     */
    public void setUsed(boolean used) {
        this.used = used;
    }

    /**
     * Gets tool use
     * @return, boolean, true if is used
     */
    public boolean isUsed() {
        return used;
    }
}
