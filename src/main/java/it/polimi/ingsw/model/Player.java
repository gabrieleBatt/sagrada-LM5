package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String nickname;
    private int tokens;
    private DashBoard dashBoard;
    private ArrayList<PrivateObjective> privateObjective;

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
}
