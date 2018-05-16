package it.polimi.ingsw.server.model.tool;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.controller.Identifiable;
import it.polimi.ingsw.server.model.rules.ActionCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tool implements Identifiable {

    private static final Logger logger = LogMaker.getLogger(Tool.class.getName(), Level.ALL);
    private List<ActionCommand> actionCommandList;
    private String name;
    private boolean used;

    /**
     * Creates a tool
     * @param actionCommandList action that has to be performed by the tool
     */
    public Tool(List<ActionCommand> actionCommandList, String name){
        this.name = name;
        this.actionCommandList = actionCommandList;
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
    public List<ActionCommand> getActionCommandList() {
        return new ArrayList<>(actionCommandList);
    }

    /**
     * Sets tool use
     * @param used boolean, set true if the tool is used
     */
    public void setUsed(boolean used) {
        this.used = used;
        logger.log(Level.FINEST, "This tool: " + name+ " has been set used", this);

    }

    /**
     * Gets tool use
     * @return, boolean, true if is used
     */
    public boolean isUsed() {
        return used;
    }

    @Override
    public String getId() {
        return getName();
    }
}
