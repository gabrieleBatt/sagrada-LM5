package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.factory.GameScreen;
import javafx.util.Pair;

import java.io.InputStream;
import java.util.*;

public class CliGameScreen implements GameScreen {

    private Scanner scanner;

    public CliGameScreen(InputStream inputStream){
        scanner = new Scanner(inputStream);
    }



    List<Pair<String, Boolean>> nicknames;
    List<String> privateObjectives;
    List<String> publicObjectives;
    List<ToolClass > toolsList;
    List<PlayerClass> playersList;
    Collection<String> poolDice;

    List<List<String>> roundtrack;


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
        showAll(nickname);
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

    public void setPool(Collection<String> dice){
        poolDice = new HashSet<>(dice);
        poolDice = dice;
    }



    public void setRoundTrack(List<String> dice){
        //throw new UnsupportedOperationException();
        roundtrack.add(dice);

    }

    @Override
    public String getWindow(Collection<String> o) {
        return null;
    }

    @Override
    public String getInput(Collection<String> options, String container) {
        if (container.equals("tool")){

        }

        return null;
    }

    @Override
    public String getInputFrom(Collection<String> strings, String message) {
        String choice;
        System.out.println(message + " " + strings);
        choice = scanner.nextLine();
        while(!strings.contains(choice)) {
            System.out.println("Scelta non valida ");
            choice = scanner.nextLine();
        }
        return choice;
    }


    void showAll(String nickname){
        String player = nickname;
        showNicknameAndTokensAndRound(player);
        showPrivateObjective(player);
        showWindow(player);
        showPublicObjectives();
        showPool();
        showTools();
        showOthersNameAndTokens();
        showOthersWindows(player);
        showRoundTrack();
    }
     


    private void showRoundTrack() {
        System.out.print("Round Track: " + "\t");
        for(int i= 0; i<roundtrack.size();i++){
            System.out.print("[" + roundtrack.get(i) +"]" + "\t");
        }
        System.out.print("\n");

    }

    private void showOthersWindows(String player) {
        for (PlayerClass p: playersList){
            if (!p.equals(player)){
                for (int y = 0; y <4 ; y++){
                    for (int x = 0; x < 5 ; x++){
                        System.out.print("[" + p.glassWindow.cells[x*5+y].content + "]");;
                    }
                    System.out.print("\n");
                }
            }
        }
        System.out.print("\n");
    }

    private void showOthersNameAndTokens() {
        for (PlayerClass p: playersList){
            System.out.print(p.nickname + "\t" + p.tokens +"\t");
        }
        System.out.print("\n");
    }

    private void showTools() {
        for(int i= 0; i< toolsList.size(); i++) {
            if (toolsList.get(i).used)
                System.out.print(toolsList.get(i) + "[2]" + "\t");
            else
                System.out.print(toolsList.get(i) + "[1]" + "\t");
        }
        System.out.print("\n");
    }

    private void showPool() {
        do {
            System.out.print(poolDice.iterator().next() + "\t");
        }
        while(poolDice.iterator().hasNext());
        System.out.print("\n");
    }

    private void showPublicObjectives() {
        for (int i = 0; i < publicObjectives.size(); i++){
            System.out.print(privateObjectives.get(i)+ "\t");
        }
        System.out.print("\n");
    }


    private void showWindow(String player) {
        for (PlayerClass p: playersList)
            if(p.nickname.equals(player)){
                for (int y = 0; y <4 ; y++){
                    for (int x = 0; x < 5 ; x++){
                        System.out.print("[" + p.glassWindow.cells[x*5+y].content + "]");;
                    }
                    System.out.print("\n");
                }
            }
        System.out.print("\n");

    }


    private void showPrivateObjective(String player) {
        System.out.println(privateObjectives);

    }

    private void showNicknameAndTokensAndRound(String nickname) {
        for (PlayerClass p: playersList)
            if (p.nickname.equals(nickname)){
                System.out.println(nickname + "\t" + "\t" + p.tokens + "\t" + (roundtrack.size()+1));
            }

    }


}



