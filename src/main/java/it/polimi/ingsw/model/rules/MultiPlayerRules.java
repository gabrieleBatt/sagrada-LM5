package it.polimi.ingsw.model.rules;

import it.polimi.ingsw.controller.ActionCommand;

import java.util.List;

public class MultiPlayerRules implements Rules {

    private MultiPlayerRules multiPlayerRules = new MultiPlayerRules();


    private MultiPlayerRules(){

    }
    private static ActionCommand getDealPrivateObjectiveCommand(int cardsToEach){
        return (actionReceiver) -> {
            final int toEach = cardsToEach;
            //TODO--dealing cards
        };
    }

    @Override
    public List<ActionCommand> getGameActions() {
        return null;
    }

    public MultiPlayerRules getMultiplayerRules() {
        return multiPlayerRules;
    }
}
