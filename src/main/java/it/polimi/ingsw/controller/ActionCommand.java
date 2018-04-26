package it.polimi.ingsw.controller;

public abstract class ActionCommand {

    private String description;
    private Game actionReceiver;

    public ActionCommand(String description, Game actionReceiver) {
        this.description = description;
        this.actionReceiver = actionReceiver;
    }

    public String getDescription() {
        return description;
    }

    public abstract void execute();
}
