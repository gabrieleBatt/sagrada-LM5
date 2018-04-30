package it.polimi.ingsw.model;

public class Tool {
    private ActionCommand actionCommand;
    private String name;
    private boolean used;
    private int tokens;

    /**
     * Creating a tool
     * @param actionCommand action that has to be performed by the tool
     */
    public Tool(ActionCommand actionCommand){
        this.actionCommand = actionCommand;
    }

    /**
     * Getting tool name
     * @return tool name
     */
    public String getName() {
        return name;
    }

    /**
     * Getting action command
     * @return tool action command
     */
    public ActionCommand getActionCommand() {
        return actionCommand;
    }

    /**
     * Setting tool use
     * @param used boolean, set true if the tool is used
     */
    public void setUsed(boolean used) {
        this.used = used;
    }

    /**
     * Getting tool use
     * @return, boolean, true if is used
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * Getting number of tokens on the tool
     * @return number of tokens
     */
    public int getTokens() {
        return tokens;
    }

    /**
     * Adding tokens to the tool
     */
    public void addTokens(){
        if (isUsed())
            tokens = tokens +2;
        else
            tokens++;
    }
}
