package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.exception.CellNotFoundException;
import it.polimi.ingsw.model.exception.DashBoardNotFoundException;
import it.polimi.ingsw.model.objective.PrivateObjective;

import java.util.*;

/**
 * Player is a concrete class representing a game's player. Its' main attributes are nickname, tokens, dashBoard,
 * privateObjective. It has also a boolean indicating whether it's connected or not.
 */
public class Player {
    private final String nickname;
    private int tokens;
    private Optional<DashBoard> dashBoard;
    private HashSet<PrivateObjective> privateObjective;
    private boolean connected;

    /**
     * Creates a player, setting the nickname
     * @param nickname player's nickname
     */
    public Player(String nickname)
    {
        this.privateObjective = new HashSet<>();
        dashBoard = Optional.empty();
        this.nickname = nickname;
    }

    /**
     * Gets the player's nickname
     * @return: player's nickname
     */
    public String getNickname(){
        return nickname;
    }

    /**
     * Gets the dashboard
     * @return: dashboard
     * @throws CellNotFoundException exception thrown if there's no dashBoard
     */
    public DashBoard getDashBoard() throws DashBoardNotFoundException {
        if(dashBoard.isPresent())
            return dashBoard.get();
        else throw new DashBoardNotFoundException("The player"+ this.nickname +"hasn't a dashboard");
    }

    /**
     * Sets the dashboard
     * @param dashBoard
     */
    public void setDashBoard(DashBoard dashBoard){
        this.dashBoard = Optional.of(dashBoard);
    }

    /**
     * Gets tokens
     * @return tokens
     */
    public int getTokens(){
        return tokens;
    }

    /**
     * Sets tokens
     * @param tokens to be set
     */
    public void setTokens(int tokens){
        this.tokens = tokens;
    }

    /**
     * Gets private objective
     * @return list of private objective
     */
    public Collection<PrivateObjective> getPrivateObjective(){
        return (Collection<PrivateObjective>)privateObjective.clone();
    }

    /**
     * Adds private objective
     * @param newPrivateObjective private objective to be added
     */
    public void addPrivateObjective (PrivateObjective newPrivateObjective){
        this.privateObjective.add(newPrivateObjective);
    }

    /**
     * Adds private objective
     * @param newPrivateObjective list of private objective to be added
     */
    public void addPrivateObjective(Collection<PrivateObjective> newPrivateObjective){
        this.privateObjective.addAll(newPrivateObjective);
    }

    /**
     * Sets connection status
     * @param connected boolean parameter, true for connection up
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     * Gets connection status
     * @return boolean parameter, true if is connected
     */
    public boolean isConnected() {
        return connected;
    }
    /**
     * Method used for testing
     * @return String representing a player
     */
    @Override
    public String toString(){
        String ret = "Player ";
        ret = ret + this.nickname + "has:\n" + this.tokens + "number of tokens\n"+ this.dashBoard.toString() + "\n" +
        "private objective:" + this.privateObjective.toString() + "\n";
        if(this.isConnected())
            ret = ret + "is connected\n";
        else
            ret = ret + "is not connected\n";
        return ret;
    }

    /**
     * Prints the override toString of an object Player
     */
    public void dump(){
        System.out.println(this);
    }
}
