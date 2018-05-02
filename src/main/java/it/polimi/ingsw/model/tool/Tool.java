package it.polimi.ingsw.model.tool;

import it.polimi.ingsw.controller.ActionCommand;

public class Tool {
    private ActionCommand actionCommand;
    private String name;
    private boolean used;
    private int tokens;

    /**
     * Creates a tool
     * @param actionCommand action that has to be performed by the tool
     */
    public Tool(ActionCommand actionCommand){
        this.actionCommand = actionCommand;
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
