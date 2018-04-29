package it.polimi.ingsw.model;

public class Tool {
    private ActionCommand actionCommand;
    private String name;
    private boolean used;
    private int tokens;


    public Tool(ActionCommand actionCommand){
        this.actionCommand = actionCommand;
    }
    public String getName() {
        return name;
    }

    public ActionCommand getActionCommand() {
        return actionCommand;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isUsed() {
        return used;
    }

    public int getTokens() {
        return tokens;
    }

    public void addTokens(){
        if (isUsed())
            tokens = tokens +2;
        else
            tokens++;
    }
}
