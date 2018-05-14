package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.controller.commChannel.CommunicationChannel;
import it.polimi.ingsw.server.model.rules.ActionCommand;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.Table;
import it.polimi.ingsw.server.model.table.dice.Die;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game {

    private static final Logger logger = LogMaker.getLogger(Game.class.getName(), Level.ALL);
    private Table table;
    private List<CommunicationChannel> commChannels;
    private List<Player> players;
    /*private HashMap<String, Die> dice;

    //TODO

    restart hashmap da chiamare nel turn
    public static Logger getLogger()
    {
        dice.put("d1", dado)

        return logger;
    }
    */
    /**
     * Creates a game, setting players and table.
     * @param commChannels Collection of communication channel, one per player.
     */
    public Game(Collection<CommunicationChannel> commChannels){
        this.commChannels = new ArrayList<>(commChannels);
        players = new ArrayList<>();
        for(CommunicationChannel cc: commChannels){
            players.add(new Player(cc.getNickname()));
        }
        this.table = new Table(players);
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

    /**
     * Updates to every client the changes occurred in game.
     */
    public void updateAll(){
        for (CommunicationChannel channel : this.getCommChannels()) {
            channel.updateView();
        }
    }
}
