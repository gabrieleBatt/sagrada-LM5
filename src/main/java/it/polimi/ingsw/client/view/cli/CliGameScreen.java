package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.factory.GameScreen;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class CliGameScreen implements GameScreen {
    List<Pair<String, Boolean>> nicknames;
    List<String> privateObjectives;
    List<String> publicObjectives;
    List<ToolClass > toolsList;
    List<PlayerClass> playersList;
    Collection<Pair<String, Boolean>> dice;


    class ToolClass {
        String toolName;
        boolean used;
        Boolean selectable;
    }
    class PlayerClass{
        String nickname;
        int tokens;
        boolean connected;
        WindowClass glassWindow;
    }
    class WindowClass{
        String windowName;
        Cell[] cells = new Cell[20];
        public WindowClass() {
            windowName = "";
            for (int i = 0; i < 20; i++) {
                cells[i].content = "  ";
            }
        }
    }
    class Cell{
        String content;
        boolean active = false;

    }

    public void setPlayers(List<Pair<String, Boolean>> nicknames){
        playersList = new ArrayList<>();
        for(Pair<String, Boolean> n: nicknames){
            PlayerClass newPlayer = new PlayerClass();
            newPlayer.nickname = n.getKey();
            newPlayer.connected = n.getValue();
        }

    }
    public void setPrivateObjectives(List<String> privateObjectives){
        this.privateObjectives = privateObjectives;
    }

    public void setPublicObjective(List<String> publicObjectives){
        this.publicObjectives = publicObjectives;
    }

    public void setTools(List<Pair <String, Boolean>> tools){
        toolsList = new ArrayList<>();
        for(Pair<String, Boolean> t: tools){
            ToolClass newTool = new ToolClass();
            newTool.toolName = t.getKey();
            newTool.selectable = t.getValue();
            toolsList.add(newTool);
        }
    }

    public void setToolUsed(String tool, boolean used){
        for(ToolClass t: toolsList)
            if (t.toolName.equals(tool)){
            t.used = used;
            }
    }

    public void setPlayerToken(String nickname, int tokens){
        for(PlayerClass p: playersList ){
            if(p.nickname.equals(nickname))
                p.tokens = tokens;

        }
    }

    public void setPlayerWindow(String nickname, String windowName){
        for(PlayerClass p: playersList ){
            if(p.nickname.equals(nickname))
                p.glassWindow.windowName = windowName;

        }
    }

    public void setCellContent(String nickname, int x, int y, String die){
        for(PlayerClass p: playersList)
            if (p.nickname.equals(nickname)){
                p.glassWindow.cells[x*5 + y].content = die;
            }
    }

    public void setCellActive(String nickname, int x, int y){
        for (PlayerClass p: playersList){
            if(p.nickname.equals(nickname))
                p.glassWindow.cells[x*5 + y].active=true;
        }
    }

    public void setPool(Collection<Pair<String, Boolean>> dice){
        this.dice = new HashSet<>(dice);
        this.dice = dice;
    }


    public void setRoundTrack(List<Pair<String, Boolean>> dice){
        throw new UnsupportedOperationException();
    }




    void showAll(){
        showNicknameAndTokensAndRound();
    }

    private void showNicknameAndTokensAndRound() {
        throw new UnsupportedOperationException();
    }


}



