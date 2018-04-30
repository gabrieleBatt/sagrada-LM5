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
     * Creating a player, setting the nickname
     * @param nickname player's nickname
     */
    public Player(String nickname)
    {
        this.nickname = nickname;
    }

    /**
     * Getting nickname
     * @return: player's nickname
     */
    public String getNickname(){
        return nickname;
    }

    /**
     * Getting the dashboard
     * @return: dashboard
     */
    public DashBoard getDashBoard() {
        return dashBoard;
    }

    /**
     * Setting the dashboard
     * @param dashBoard
     */
    public void setDashBoard(DashBoard dashBoard){
        this.dashBoard = dashBoard;
    }

    /**
     * Getting tokens
     * @return tokens
     */
    public int getTokens(){
        return tokens;
    }

    /**
     * Setting tokens
     * @param tokens to be set
     */
    public void setTokens(int tokens){
        this.tokens = tokens;
    }

    /**
     * Getting private objectives
     * @return list of private objective
     */
    public List<PrivateObjective> getPrivateObjective(){
        return (List<PrivateObjective>)privateObjective.clone();
    }

    /**
     * Adding private objective
     * @param newPrivateObjective private objective to be added
     */
    public void addPrivateObjective (PrivateObjective newPrivateObjective){
        this.privateObjective.add(newPrivateObjective);
    }

    /**
     * Adding private objectives
     * @param newPrivateObjective list of private objectives to be added
     */
    public void addPrivateObjective(List<PrivateObjective> newPrivateObjective){
        this.privateObjective.addAll(newPrivateObjective);
    }

    /**
     * Settting connection status
     * @param connected boolean parameter, true for connection up
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     * Getting connection status
     * @return boolean parameter, true if is connected
     */
    public boolean isConnected() {
        return connected;
    }
}
