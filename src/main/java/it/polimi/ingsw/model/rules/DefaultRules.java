package it.polimi.ingsw.model.rules;

import it.polimi.ingsw.controller.ActionCommand;

import java.util.List;
import java.util.Set;

/**
 * The default rules generate all the actions needed in a default game
 */
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

    private static ActionCommand getRoundCommand(int numOfPlayers){
        return (actionReceiver) -> {
            //TODO
        };
    }

    private static ActionCommand getTurnCommand(int numOfPlayers){
        return (actionReceiver) -> {
            //TODO
        };
    }

    private static ActionCommand getScoreCommand(){
        return (actionReceiver) -> {
            //TODO
        };
    }

    private static ActionCommand getUseToolCommand(){
        return (actionReceiver) -> {
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
    public Set<ActionCommand> getTurnActions() {
        return null;
    }

    public DefaultRules getMultiplayerRules() {

        return defaultRules;
    }
}
