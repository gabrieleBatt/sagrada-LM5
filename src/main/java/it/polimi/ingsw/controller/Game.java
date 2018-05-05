package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.table.Player;
import it.polimi.ingsw.model.table.Table;

import java.util.List;


public class Game {


    private List<ActionCommand> actionCommandList;
    private Player turnPlayer;
    private Table table;

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
