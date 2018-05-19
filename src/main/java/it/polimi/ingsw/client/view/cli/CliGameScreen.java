package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.factory.GameScreen;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;
import it.polimi.ingsw.server.model.tool.Tool;
import javafx.util.Pair;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

public class CliGameScreen implements GameScreen {

    private Scanner scanner;
    private final PrintStream printStream;
    private List<String> privateObjectives;
    private List<String> publicObjectives;
    private List<ToolClass> toolsList;
    private List<PlayerClass> playersList;
    private Collection<Die> poolDice;
    private List<List<Die>> roundTrack;
    private boolean skip;
    private boolean undo;

    public CliGameScreen(InputStream inputStream, PrintStream printStream){
        scanner = new Scanner(inputStream);
        this.printStream = printStream;
        privateObjectives = new ArrayList<>();
        publicObjectives = new ArrayList<>();
        toolsList = new ArrayList<>();
        playersList = new ArrayList<>();
        poolDice = new ArrayList<>();
        roundTrack = new ArrayList<>();
    }

    private class ToolClass {
        String toolName;
        boolean used;
        boolean active;
    }
    private class PlayerClass{
        String nickname;
        int tokens;
        boolean connected;
        WindowClass glassWindow;
    }
    private class WindowClass{
        String windowName;
        Cell[] cells;

        public WindowClass() {
            windowName = "Not yet choosen";
            cells = new Cell[20];
            for (int i = 0; i < 20; i++) {
                cells[i] = new Cell();
                cells[i].content = "  ";
            }
        }
    }
    private class Cell{
        String content;
        boolean active;
    }

    private class Die{
        final String id;
        boolean active;

        public Die(String die) {
            id = die;
        }
    }

    public void setPlayers(List<Pair<String, Boolean>> nicknames){
        playersList = new ArrayList<>();
        for(Pair<String, Boolean> n: nicknames){
            PlayerClass newPlayer = new PlayerClass();
            newPlayer.nickname = n.getKey();
            newPlayer.connected = n.getValue();
            newPlayer.glassWindow = new WindowClass();
            playersList.add(newPlayer);
        }
        showAll();
    }

    @Override
    public void setPrivateObjectives(List<String> privateObjectives){
        this.privateObjectives = privateObjectives;
        showAll();
    }

    @Override
    public void setPublicObjective(List<String> publicObjectives){
        this.publicObjectives = publicObjectives;
        showAll();
    }

    @Override
    public void setTools(List<Pair <String, Boolean>> tools){
        toolsList = new ArrayList<>();
        for(Pair<String, Boolean> t: tools){
            ToolClass newTool = new ToolClass();
            newTool.toolName = t.getKey();
            newTool.used = t.getValue();
            toolsList.add(newTool);
        }
        showAll();
    }

    @Override
    public void setToolUsed(String tool, boolean used){
        for(ToolClass t: toolsList)
            if (t.toolName.equals(tool)) {
                t.used = used;
            }
        showAll();
    }

    @Override
    public void setPlayerToken(String nickname, int tokens){
        for(PlayerClass p: playersList ){
            if(p.nickname.equals(nickname))
                p.tokens = tokens;

        }
        showAll();
    }

    @Override
    public void setPlayerWindow(String nickname, String windowName){
        for(PlayerClass p: playersList ){
            if(p.nickname.equals(nickname))
                p.glassWindow.windowName = windowName;

        }
        showAll();
    }

    @Override
    public void setCellContent(String nickname, int x, int y, String die){
        for(PlayerClass p: playersList)
            if (p.nickname.equals(nickname)){
                p.glassWindow.cells[x*5 + y].content = die;
            }
        showAll();
    }

    public void setPool(Collection<String> dice){
        poolDice = new HashSet<>();
        for (String die : dice) {
            poolDice.add(new Die(die));
        }
        showAll();
    }

    @Override
    public void setRoundTrack(List<List<String>> dice){
        roundTrack = new ArrayList<>();
        for (List<String> list : dice) {
            List<Die> ld = new ArrayList<>();
            for (String s : list) {
                ld.add(new Die(s));
            }
            roundTrack.add(ld);
        }
        showAll();
    }

    @Override
    public String getWindow(Collection<String> o) {
        printStream.println("Scegli la finestra: " + o);
        String choice = scanner.nextLine();
        while(!o.contains(choice)){
            printStream.println("Scelta non valida ");
            choice = scanner.nextLine();
        }
        return choice;
    }

    @Override
    public String getInput(Collection<String> options, String container) {
        if(options.contains("skip")){
            skip = true;
            options.remove("skip");
        }
        if(options.contains("undo")){
            undo = true;
            options.remove("undo");
        }
        String ret;
        switch (container){
            case "pool":
                ret = poolGetInput(options);
                break;
            case "roundTrack":
                ret = roundTrackGetInput(options);
                break;
            case "glassWindow":
                ret = windowGetInput(options);
                break;
            case "table":           //tool
                ret = tableGetInput(options);
                break;
            default: throw new IllegalArgumentException();
        }
        showAll();
        skip = false;
        undo = false;
        return ret;
    }

    private String getChoice (Collection<String> options){
        showAll();
        List<String> lowerCaseOptions = options.stream().map(String::toLowerCase).collect(Collectors.toList());
        String choice = scanner.nextLine().toLowerCase();
        while (!(lowerCaseOptions.contains(choice) || (choice.equalsIgnoreCase("skip"))&&skip) || (choice.equalsIgnoreCase("undo"))&&undo) {
            printStream.println("Scelta non valida ");
            choice = scanner.nextLine();
        }
        return choice;
    }

    private String tableGetInput(Collection<String> options) {
        for(ToolClass tool: toolsList){
            if(options.contains(tool.toolName))
                tool.active = true;
        }

        String ret = getChoice(options);

        for(ToolClass tool: toolsList)
            tool.active = false;


        return ret;
    }


    private String windowGetInput(Collection<String> options) {
        for (String option : options) {
            playersList.get(0).glassWindow.cells[(Character.getNumericValue(option.charAt(0)))*5 + Character.getNumericValue(option.charAt(1))].active = true;
        }
        List<String> op = options.stream().map(s -> s.substring(0, 2)).collect(Collectors.toList());

        String ret = getChoice(op);

        for (Cell cell : playersList.get(0).glassWindow.cells) {
            cell.active = false;
        }
        return ret;
    }

    private String poolGetInput (Collection<String> options){
        for (Die die : poolDice) {
            if(options.contains(die.id)){
                die.active = true;
            }
        }

        String ret = getChoice(options);

        for (Die die : poolDice) {
            die.active = false;
        }
        return ret;
    }

    private String roundTrackGetInput (Collection<String> options){
        for(List<Die> dieList: roundTrack){
            for(Die d: dieList){
                if(options.contains(d.id))
                    d.active = true;
            }
        }

        String ret = getChoice(options);

        for(List<Die> dieList: roundTrack){
            for(Die d: dieList){
                d.active = false;
            }
        }
        return ret;
    }


    @Override
    public String getInputFrom(Collection<String> strings, String message) {
        String choice;
        printStream.println(message + " " + strings);
        choice = scanner.nextLine();
        while(!strings.contains(choice)) {
            printStream.println("Scelta non valida ");
            choice = scanner.nextLine();
        }
        showAll();
        return choice;
    }

    private void showAll(){
        clear();
        showNicknameAndTokensAndRound();
        showPrivateObjective();
        showWindow();
        showPublicObjectives();
        showPool();
        showTools();
        showOthersNameAndTokens();
        showOthersWindows();
        showRoundTrack();
        if(skip)
            printStream.println("Skip");
        if(undo)
            printStream.println("undo");
    }

    private void showNicknameAndTokensAndRound() {
        printStream.println(playersList.get(0).nickname + "\t" + "\t" + playersList.get(0).tokens + " \t"+ " \t"+" \t"+ " \t"+ " \t"+" \t" + "Round: " + (roundTrack.size()+1));
    }

    private void showPrivateObjective() {
        printStream.print("Obbiettivo privato: ");
        for (String privateObjective : privateObjectives) {
            printStream.print(privateObjective+"\t");
        }
        printStream.println();

    }

    private void showWindow() {
        for (int x = 0; x <4 ; x++){
            for (int y = 0; y < 5 ; y++){
                Cell c = playersList.get(0).glassWindow.cells[x*5+y];
                printActive(c.active, "[" + c.content.substring(0,2) + "]");
            }
            printStream.print("\n");
        }
        printStream.print("\n");

    }

    private void printActive(boolean active, String s){
        if(active)
            printStream.print("{");
        else
            printStream.print(" ");
        printStream.print(s);
        if(active)
            printStream.print("} ");
        else
            printStream.print("  ");
    }

    private void showPublicObjectives() {
        printStream.print("Obbiettivi pubblici: ");
        for (int i = 0; i < publicObjectives.size(); i++){
            printStream.print(privateObjectives.get(i)+ "\t");
        }
        printStream.print("\n");
    }

    private void showPool() {
        printStream.print("Riserva: ");
        for (Die die : poolDice) {
            printActive(die.active,die.id.substring(0, 1));
        }
        printStream.print("\n");
    }

    private void showTools() {
        printStream.print("Strumenti: " + "\t");
         for(ToolClass tool: toolsList) {
             printActive(tool.active, tool.toolName);
             if (tool.used) {
                printStream.print("[2]  ");
             }else
                 printStream.print("[1]  ");
         }
        printStream.println();
    }

    private void showOthersNameAndTokens() {
        for (PlayerClass p: playersList.subList(1,playersList.size())){
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < 35; i++) {
                s.append(" ");
            }
            s.insert(0, p.nickname + " " + p.tokens);
            printStream.print(s);
        }
        printStream.print("\n");
    }

    private void showOthersWindows() {
        for (int x = 0; x < 4; x++) {
            for (PlayerClass p : playersList.subList(1, playersList.size())) {
                for (int y = 0; y < 5; y++) {
                    Cell c = p.glassWindow.cells[x * 5 + y];
                    printActive(c.active, "[" + c.content.substring(0, 2) + "]");
                }
                printStream.print("\t" + "\t" + " ");
            }
            printStream.println();
        }
        printStream.println();
    }

    private void showRoundTrack() {
        printStream.print("Round track: " + "\t");
        for (List<Die> dieList : roundTrack) {
            printStream.print("round" + roundTrack.indexOf(dieList)+1+": ");
            for (Die die: dieList) {
                printActive(die.active, die.id.substring(0, 1));
            }

        }
        printStream.print("\n");
    }


    private void clear(){
        for (int i = 0; i < 10; i++) {
            printStream.println();
        }
    }

















}



