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

    private HashMap<String, Die> dieHashMap;
    private List<ActionCommand> actionCommandList;
    private Player turnPlayer;
    private Table table;

    public Game(List<Player> players){
        dieHashMap = new HashMap<>();
        actionCommandList = new ArrayList<>();
        table = new Table(players);
        turnPlayer = players.get(0);
    }

    public Table getTable() {
        return table;
    }

    public Player getTurnPlayer() {
        return turnPlayer;
    }

    public void setTurnPlayer(Player turnPlayer) {
        this.turnPlayer = turnPlayer;
    }

    public void addActionCommand(int index, ActionCommand actionCommand) {
        this.actionCommandList.add(index, actionCommand);
    }
}
