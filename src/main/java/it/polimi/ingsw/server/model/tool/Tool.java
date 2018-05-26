package it.polimi.ingsw.server.model.tool;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.net.identifiables.Identifiable;
import it.polimi.ingsw.server.model.rules.ActionCommand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tool implements Identifiable {

    private static final Logger logger = LogMaker.getLogger(Tool.class.getName(), Level.ALL);
    private final List<ActionCommand> actionCommandList;
    private final String name;
    private boolean used;
    private final Collection<ToolConditions> toolConditions;

    /**
     * * Creates a tool
     * @param actionCommandList action that has to be performed by the tool
     * @param name String, tool name.
     * @param toolConditions Collection of using conditions for the tool.
     */
    public Tool(List<ActionCommand> actionCommandList, String name, Collection<ToolConditions> toolConditions){
        this.name = name;
        this.actionCommandList = actionCommandList;
        this.toolConditions = toolConditions;
        used = false;
    }

    /**
     * Gets a copy of the conditions list.
     * @return a copy of the conditions list.
     */
    public Collection<ToolConditions> getToolConditions() {
        return new ArrayList<>(toolConditions);
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
