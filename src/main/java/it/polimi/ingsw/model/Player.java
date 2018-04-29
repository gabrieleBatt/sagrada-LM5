package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String nickname;
    private int tokens;
    private DashBoard dashBoard;
    private ArrayList<PrivateObjective> privateObjective;
    private boolean connected;

    public Player(String nickname)
    {
        this.nickname = nickname;
    }

    public String getNickname(){
        return nickname;
    }

    public DashBoard getDashBoard() {
        return dashBoard;
    }
    public void setDashBoard(DashBoard dashBoard){
        this.dashBoard = dashBoard;
    }
    public int getTokens(){
        return tokens;
    }

    public void setTokens(int tokens){
        this.tokens = tokens;
    }
    public List<PrivateObjective> getPrivateObjective(){
        return (List<PrivateObjective>)privateObjective.clone();
    }
    public void addPrivateObjective (PrivateObjective newPrivateObjective){
        this.privateObjective.add(newPrivateObjective);
    }
    public void addPrivateObjective(List<PrivateObjective> newPrivateObjective){
        this.privateObjective.addAll(newPrivateObjective);
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }
}
