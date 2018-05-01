package it.polimi.ingsw.model.rules;

import it.polimi.ingsw.controller.ActionCommand;

import java.util.List;

public class DefaultRules implements Rules {

    private DefaultRules defaultRules = new DefaultRules();


    private DefaultRules(){

    }

    private static ActionCommand getDealPrivateObjectivesCommand(int cardsToEach){
        return (actionReceiver) -> {
            final int toEach = cardsToEach;
            //TODO
        };
    }

    private static ActionCommand getDealDashBoardsCommand(int dashBoardToEach){
        return (actionReceiver) -> {
            final int toEach = dashBoardToEach;
            //TODO
        };
    }

    private static ActionCommand getDealPublicObjectivesCommand(int cardsToEach){
        return (actionReceiver) -> {
            final int toEach = cardsToEach;
            //TODO
        };
    }

    private static ActionCommand getPickToolsCommand(int numOfTools){
        return (actionReceiver) -> {
            final int num = numOfTools;
            //TODO
        };
    }


    @Override
    public List<ActionCommand> getGameActions() {
        return null;
    }

    @Override
    public List<ActionCommand> getRoundActions() {
        return null;
    }

    @Override
    public List<ActionCommand> getTurnActions() {
        return null;
    }

    public DefaultRules getMultiplayerRules() {

        return defaultRules;
    }
}
