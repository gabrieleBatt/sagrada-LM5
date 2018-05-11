package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.rules.ActionCommand;
import it.polimi.ingsw.model.table.Player;
import it.polimi.ingsw.model.table.Table;
import it.polimi.ingsw.model.table.dice.Die;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Game {
    private Table table;
    private List<CommunicationChannel> commChannels;
    private List<Player> players;

    //TODO
    public Game(Collection<CommunicationChannel> commChannels){
        this.commChannels = new ArrayList<>(commChannels);
        players = new ArrayList<>();
        for(CommunicationChannel cc: commChannels){
            players.add(new Player(cc.getNickname()));
        }
        this.table = new Table(players);
    }
    public Table getTable(){
        return this.table;
    }

    public Collection<CommunicationChannel> getCommChannels() {
        return new ArrayList<>(commChannels);
    }

    public void updateAll(){
        for (CommunicationChannel channel : this.getCommChannels()) {
            channel.updateView();
        }
    }
}
