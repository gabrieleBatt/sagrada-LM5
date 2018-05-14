package it.polimi.ingsw.server.controller;

import com.sun.javafx.css.Rule;
import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.controller.commChannel.CommunicationChannel;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.rules.ActionCommand;
import it.polimi.ingsw.server.model.rules.DefaultRules;
import it.polimi.ingsw.server.model.rules.TurnActionCommand;
import it.polimi.ingsw.server.model.table.Memento;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.Table;
import it.polimi.ingsw.server.model.table.dice.Die;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Game implements Runnable {

    private static final Logger logger = LogMaker.getLogger(Game.class.getName(), Level.ALL);
    private Table table;
    private List<CommunicationChannel> commChannels;
    private HashMap<String, Die> dice;
    private List<ActionCommand> actionCommandList;

    //TODO

    public Game(Collection<CommunicationChannel> commChannels){
        this.commChannels = new ArrayList<>(commChannels);
        this.table = new Table(commChannels.stream()
                .map(cc -> new Player(cc.getNickname()))
                .collect(Collectors.toList()));
        this.dice = new HashMap<>();
        this.actionCommandList = new ArrayList<>();

        actionCommandList.addAll(DefaultRules.getDefaultRules().getSetupGameActions());

        Iterator<Player> players = getTable()
                .getPlayersIterator(getTable().getPlayers().get(0), true);
        for (int i = 0; i < 10; i++) {
            actionCommandList.add(DefaultRules.getDefaultRules().getSetupRoundAction());
            Iterator<Player> roundIterator = getTable()
                    .getPlayersIterator(players.next(), false);
            int x = 1;
            while (roundIterator.hasNext()){
                actionCommandList.add(actionCommandList.size() - x, DefaultRules.getDefaultRules().getTurnAction(roundIterator.next()));
                actionCommandList.add(actionCommandList.size() - x, DefaultRules.getDefaultRules().getTurnAction(roundIterator.next()));
                x++;
            }
            actionCommandList.add(DefaultRules.getDefaultRules().getEndRoundAction());
        }
        actionCommandList.addAll(DefaultRules.getDefaultRules().getEndGameActions());
    }

    @Override
    public void run() {
        while(!actionCommandList.isEmpty()){
            try {
                actionCommandList.get(0).execute(this);
                actionCommandList.remove(0);
            } catch (EndGameException | BagEmptyException | DeckTooSmallException | DieNotAllowedException | CellNotFoundException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
        }
    }

    /**
     * Reset the turn when called if the current action is a TurnActionCommand
     */
    public void resetTurn(){
        if(actionCommandList.get(0) instanceof TurnActionCommand){
            ((TurnActionCommand) actionCommandList.get(0)).reset(this);
        }
    }

    /**
     * Gets the HashMap of chosen die.
     * @return HashMap of chosen die.
     */
    public HashMap<String,Die> getMap(){
        return this.dice;
    }

    /**
     * Gets the logger.
     * @return logger.
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * Gets table.
     * @return Object, table.
     */
    public Table getTable(){
        return this.table;
    }

    /**
     * Gets the collection of communication channel.
     * @return Collection og communication channel.
     */
    public Collection<CommunicationChannel> getCommChannels() {
        return new ArrayList<>(commChannels);
    }


}
