package it.polimi.ingsw.model.rules;

import it.polimi.ingsw.controller.Game;
import it.polimi.ingsw.model.deck.PublicObjectiveDeck;
import it.polimi.ingsw.model.deck.ToolDeck;
import it.polimi.ingsw.model.objective.PrivateObjective;
import it.polimi.ingsw.model.deck.PrivateObjectiveDeck;
import it.polimi.ingsw.model.table.Player;
import it.polimi.ingsw.model.table.dice.Die;

import java.util.*;

/**
 * The default rules generate all the actions needed in a default game
 */
public class DefaultRules implements Rules {

    private DefaultRules defaultRules = new DefaultRules();


    private DefaultRules(){

    }

    /**
     * returns an action that deals to every player in game their private objective
     * @param cardsToEach private objective to give to each
     * @return the ActionCommand
     */
    private static ActionCommand getDealPrivateObjectivesCommand(int cardsToEach){
        return (actionReceiver) -> {
            List<PrivateObjective> privateObjectives;
            privateObjectives  = new ArrayList<>(PrivateObjectiveDeck.draw(actionReceiver.getTable().getPlayers().size()* cardsToEach));
            for(Player p: actionReceiver.getTable().getPlayers()){
                for (int i = 0; i < cardsToEach; i++) {
                    p.addPrivateObjective(privateObjectives.get(0));
                    privateObjectives.remove(0);
                }
            }
        };
    }

    private static ActionCommand getDealDashBoardsCommand(int dashBoardToEach){
        return (actionReceiver) -> {
            final int toEach = dashBoardToEach;
            //TODO
        };
    }

    private static ActionCommand getGiveFavorsCommand(){
        return (actionReceiver) -> {
            //TODO
        };
    }

    /**
     * returns an action that picks the public objective for the game
     * @param numOfCards public objective to pick
     * @return the ActionCommand
     */
    private static ActionCommand getPickPublicObjectivesCommand(int numOfCards){
        return (actionReceiver) -> {
            actionReceiver.getTable().setPublicObjective(PublicObjectiveDeck.draw(numOfCards));
        };
    }

    /**
     * returns an action that picks the tools for the game
     * @param numOfTools tools to pick
     * @return the ActionCommand
     */
    private static ActionCommand getPickToolsCommand(int numOfTools){
        return (actionReceiver) -> {
            actionReceiver.getTable().setTools(ToolDeck.draw(numOfTools));
        };
    }

    /**
     * returns an action that makes the setup of the round (makes the pool) and
     * places the round actions(two turns for player) and the end turn action(filling the round track)
     * in the list of the game
     * @param firstOfRound the player that begins the round
     * @return the ActionCommand
     */
    private static ActionCommand getRoundCommand(Player firstOfRound){
        return (actionReceiver) -> {
            Collection<Die> dice = actionReceiver.getTable().getDiceBag().drawDice((actionReceiver.getTable().getPlayers().size()*2)+1);
            actionReceiver.getTable().getPool().addDice(dice);

            actionReceiver.addActionCommand(1, actionReceiver1 -> {
                Collection<Die> dice1 = actionReceiver1.getTable().getPool().getDice();
                for(Die d : dice1){
                    actionReceiver1.getTable().getPool().takeDie(d);
                }
                actionReceiver1.getTable().getRoundTrack().endRound(dice1);
            });

            Iterator<Player> iterator = actionReceiver.getTable().getPlayersIterator(firstOfRound);
            for (int i = 1; iterator.hasNext(); i++) {
                Player next = iterator.next();
                actionReceiver.addActionCommand(i, getTurnCommand(next));
                actionReceiver.addActionCommand(i, getTurnCommand(next));
            }
        };
    }

    private static ActionCommand getTurnCommand(Player turnPlayer){
        return (actionReceiver) -> {
            actionReceiver.setTurnPlayer(turnPlayer);
        };
    }

    private static ActionCommand getUseToolCommand(){
        return (actionReceiver) -> {
            //TODO
        };
    }

    private static ActionCommand getScoreCommand(){
        return (actionReceiver) -> {
            //TODO
        };
    }

    /**
     * sets up the action to do in the game and returns them
     * @param game the game getting the actions
     * @return the List of actions to execute during the game
     */
    @Override
    public List<ActionCommand> getGameActions(Game game) {
        List<ActionCommand> ret = new ArrayList<>();
        ret.add(getDealPrivateObjectivesCommand(1));
        ret.add(getDealDashBoardsCommand(2));
        ret.add(getGiveFavorsCommand());
        ret.add(getPickPublicObjectivesCommand(3));
        ret.add(getPickToolsCommand(3));
        Iterator<Player> iterator = game.getTable().getPlayersIterator(game.getTable().getPlayers().get(0));
        for (int i = 0; i < 10; i++) {
            if(!iterator.hasNext()){
                iterator = game.getTable().getPlayersIterator(game.getTable().getPlayers().get(0));
            }
            if(iterator.hasNext()){
                ret.add(getRoundCommand(iterator.next()));
            }
        }
        ret.add(getScoreCommand());
        return null;
    }

    public DefaultRules getDefaultRules() {
        return defaultRules;
    }
}
