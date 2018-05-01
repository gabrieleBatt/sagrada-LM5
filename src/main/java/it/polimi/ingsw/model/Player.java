package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String nickname;
    private int tokens;
    private DashBoard dashBoard;
    private ArrayList<PrivateObjective> privateObjective;
    private boolean connected;

    /**
     * Creates a player, setting the nickname
     * @param nickname player's nickname
     */
    public Player(String nickname)
    {
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
     */
    public DashBoard getDashBoard() {
        return dashBoard;
    }

    /**
     * Sets the dashboard
     * @param dashBoard
     */
    public void setDashBoard(DashBoard dashBoard){
        this.dashBoard = dashBoard;
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
     * Gets private objectives
     * @return list of private objective
     */
    public List<PrivateObjective> getPrivateObjective(){
        return (List<PrivateObjective>)privateObjective.clone();
    }

    /**
     * Adds private objective
     * @param newPrivateObjective private objective to be added
     */
    public void addPrivateObjective (PrivateObjective newPrivateObjective){
        this.privateObjective.add(newPrivateObjective);
    }

    /**
     * Adds private objectives
     * @param newPrivateObjective list of private objectives to be added
     */
    public void addPrivateObjective(List<PrivateObjective> newPrivateObjective){
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
}
