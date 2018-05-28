package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.Message;
import it.polimi.ingsw.client.view.factory.GameScreen;
import it.polimi.ingsw.server.model.table.Player;
import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

public class CliGameScreen extends GameScreen {

    private Scanner scanner;
    private final PrintStream printStream;
    private Collection<String> privateObjectives;
    private Collection<String> publicObjectives;
    private Collection<ToolClass> toolsList;
    private List<PlayerClass> playersList;
    private Collection<Die> poolDice;
    private List<List<Die>> roundTrack;
    private boolean skip;
    private boolean undo;
    private List<String> messageRecord;
    private Optional<String> tempMessage;

    public CliGameScreen(InputStream inputStream, PrintStream printStream){
        scanner = new Scanner(inputStream);
        this.printStream = printStream;
        this.messageRecord = new ArrayList<>();
        this.tempMessage = Optional.empty();
        printStream.println((char)27+ "[37m");
        privateObjectives = new ArrayList<>();
        publicObjectives = new ArrayList<>();
        toolsList = new ArrayList<>();
        playersList = new ArrayList<>();
        poolDice = new ArrayList<>();
        roundTrack = new ArrayList<>();
    }

    @Override
    public void addMessage(String message, boolean toKeep) {
        if(toKeep)
            messageRecord.add(message);
        else
            tempMessage = Optional.ofNullable(message);
    }

    public void setPlayers(List<String> nicknames){
        playersList = new ArrayList<>();
        for(String s: nicknames){
            PlayerClass newPlayer = new PlayerClass();
            newPlayer.nickname = s;
            newPlayer.glassWindow = new WindowClass();
            playersList.add(newPlayer);
        }
    }

    @Override
    public void setPrivateObjectives(Collection<String> privateObjectives){
        this.privateObjectives = privateObjectives;
    }

    @Override
    public void setPublicObjective(Collection<String> publicObjectives){
        this.publicObjectives = publicObjectives;
    }

    @Override
    public void setTools(Collection<String> tools){
        toolsList = new ArrayList<>();
        for(String t: tools){
            ToolClass newTool = new ToolClass();
            newTool.toolName = t;
            toolsList.add(newTool);
        }
    }

    @Override
    public void setToolUsed(String tool, boolean used){
        for(ToolClass t: toolsList)
            if (t.toolName.equals(tool)) {
                t.used = used;
            }
    }

    @Override
    public void setPlayerConnection(String nickname, boolean isConnected){
        for(PlayerClass p: playersList ){
            if(p.nickname.equals(nickname))
                p.connected = isConnected;
        }
    }

    @Override
    public void setPlayerToken(String nickname, int tokens){
        for(PlayerClass p: playersList ){
            if(p.nickname.equals(nickname))
                p.tokens = tokens;
        }
    }

    @Override
    public void setPlayerWindow(String nickname, String windowName){
        for(PlayerClass p: playersList ){
            if(p.nickname.equals(nickname))
                p.glassWindow.windowName = windowName;
            try {
                JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(GLASS_WINDOW_PATH+windowName+".json"));
                List<String> restrictions = new ArrayList<>((JSONArray)jsonObject.get("cells"));
                System.out.println(restrictions);
                for (int i = 0; i < 20; i++) {
                    p.glassWindow.cells[i].restriction = restrictions.get(i);
                }
            } catch (IOException | ParseException e) {
                printStream.println("Window not found");
            }
        }
    }

    @Override
    public void setCellContent(String nickname, int x, int y, String die){
        for(PlayerClass p: playersList)
            if (p.nickname.equals(nickname)){
                p.glassWindow.cells[x*5 + y].content = die;
            }
    }

    public void setPool(Collection<String> dice){
        poolDice = new HashSet<>();
        for (String die : dice) {
            poolDice.add(new Die(die));
        }
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
    }

    @Override
    public String getWindow(Collection<String> o) {
        List<String> convertedNames = o.stream().map(Message::convertWindowName).collect(Collectors.toList());
        printStream.println(Message.CHOOSE_WINDOW+": " + convertedNames);
        String choice = scanner.nextLine();
        while(!convertedNames.contains(choice)){
            printStream.println(Message.INVALID_CHOICE);
            choice = scanner.nextLine();
        }
        return Message.decodeWindowName(choice);
    }

    @Override
    public String getInput(Collection<String> options, String container) {
        skip = false;
        undo = false;
        if(options.contains("skip")){
            skip = true;
            options.remove("skip");
        }
        if(options.contains("undo")){
            undo = true;
            options.remove("undo");
        }
        String ret;
        if(container.equalsIgnoreCase("pool")) {
            ret = poolGetInput(options);
        }else if (container.equalsIgnoreCase("roundTrack")) {
            ret = roundTrackGetInput(options);
        }else if (container.equalsIgnoreCase("glassWindow")) {
            ret = windowGetInput(options);
        }else if (container.equalsIgnoreCase("table")) {
            ret = tableGetInput(options);
        }else{
            throw new IllegalArgumentException();
        }
        skip = false;
        undo = false;
        return ret;
    }

    private String getChoice (Collection<String> options){
        showAll();
        String choice = scanner.nextLine();
        while (!(options.contains(choice) || (choice.equalsIgnoreCase("skip")&&skip) || (choice.equalsIgnoreCase("undo")&&undo))) {
            printStream.println(Message.INVALID_CHOICE);
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
            playersList.get(0).glassWindow
                    .cells[(Character.getNumericValue(option.charAt(0)))*5 + Character.getNumericValue(option.charAt(1))]
                    .active = true;
        }
        String choice = getChoice(options
                .stream()
                .map(s -> s.substring(0, 2))
                .collect(Collectors.toList()));

        for (Cell cell : playersList.get(0).glassWindow.cells) {
            cell.active = false;
        }
        Optional<String> ret = options
                .stream()
                .filter(s -> s.substring(0, 2)
                        .equals(choice)).findAny();
        return ret.orElse(choice);

    }

    private String poolGetInput (Collection<String> options){
        for (Die die : poolDice) {
            if(options.contains(die.id)){
                die.active = true;
            }
        }
        String choice = getChoice(options.stream()
                .map(s -> s.substring(0,2))
                .collect(Collectors.toList()));

        for (Die die : poolDice) {
            die.active = false;
        }
        Optional<String> ret = options.stream().filter(s -> s.substring(0, 2).equals(choice)).findAny();
        return ret.orElse(choice);
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
        String convertMessage = Message.convertMessage(message);
        List<String> convertStrings = strings
                .stream()
                .map(Message::convertMessage)
                .collect(Collectors.toList());
        printStream.println(convertMessage + (char)27 + "[31m"+" " + convertStrings +(char)27 + "[37m");
        choice = scanner.nextLine();
        while(!convertStrings.contains(choice)) {
            printStream.println(Message.INVALID_CHOICE);
            choice = scanner.nextLine();
        }
        return Message.decodeMessage(choice);
    }

    @Override
    public void endGame(List<Pair<Player, Integer>> scores) {
        //TODO
    }

    @Override
    public void showAll(){
        clear();
        showNicknameAndTokensAndRound();
        showPrivateObjective();
        showWindow();
        showPublicObjectives();
        showTools();
        showPool();
        showOthersNameAndTokens();
        showOthersWindows();
        showRoundTrack();
        printStream.println();
        if (skip) {
            printActive(true, Message.SKIP.toString());
            printStream.println("\t");
        }if (undo) {
            printActive(true, Message.UNDO.toString());
            printStream.println("\n\n");
        }
    }

    private void showNicknameAndTokensAndRound() {
        printStream.println(playersList.get(0).nickname + "\t"+ Message.TOKENS+": " + playersList.get(0).tokens + " \t \t \t \t \t \t"+Message.ROUND +": "+ (roundTrack.size()+1));
    }

    private void showPrivateObjective() {
        printStream.println(Message.PRIVATE_OBJ + ": ");
        for (String privateObjective : privateObjectives) {
            showObjective(privateObjective);
        }
        printStream.println();

    }

    private void showWindow() {
        for (int x = 0; x <4 ; x++){
            for (int y = 0; y < 5 ; y++){
                Cell c = playersList.get(0).glassWindow.cells[x*5+y];
                if(c.content.equals(" ")) {
                    printActive(c.active, "[" + c.restriction + "]");
                }else{
                    printActive(c.active, c.content.charAt(0) + " " + c.content.charAt(1));
                }
            }
            printStream.print("\n");
        }
        printStream.print("\n\n");

    }

    private void printActive(boolean active, String s){
        if(active)
            printStream.print((char)27 + "[31m"+"{");
        else
            printStream.print(" ");
        printStream.print(s);
        if(active)
            printStream.print((char)27 + "[31m"+"} " + (char)27+ "[37m");
        else
            printStream.print("  ");
    }

    private void showPublicObjectives() {
        printStream.print(Message.PUBLIC_OBJ+": ");
        printStream.print("\n");
        for (String publicObjective : publicObjectives) {
            showObjective(publicObjective);
        }
    }

    private void showObjective(String name){
        try {
            JSONObject jsonObject = ((JSONObject) new JSONParser().parse(new FileReader(OBJECTIVE_PATH+name+".json")));
            printStream.print(jsonObject.get("name").toString() + ",\t"+Message.POINTS+":" + jsonObject.get("points"));
            printStream.println(",\t" + jsonObject.get("description"));
        } catch (IOException | ParseException e) {
            printStream.println("Objective not found");
        }
    }

    private void showPool() {
        printStream.print(Message.DRAFT_POOL+": ");
        for (Die die : poolDice) {
            printActive(die.active,die.id.substring(0, 2));
        }
        printStream.print("\n");
    }

    private void showTools() {
        printStream.println(Message.TOOL + ": ");
         for(ToolClass tool: toolsList) {
             try {
                 if (tool.used) {
                     printStream.print("[2]  ");
                 }else
                     printStream.print("[1]  ");
                 JSONObject jsonObject = ((JSONObject) new JSONParser().parse(new FileReader(TOOL_PATH+tool.toolName+".json")));
                 printActive(tool.active, jsonObject.get("name").toString());
                 printStream.print("\t" + jsonObject.get("description"));
             } catch (IOException | ParseException e) {
                 printStream.print("Tool not found");
             }
             printStream.println();
         }
        printStream.println();
    }

    private void showOthersNameAndTokens() {
        printStream.println("\n");
        for (PlayerClass p: playersList.subList(1,playersList.size())){
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < 38; i++) {
                s.append(" ");
            }
            String ins = p.nickname + " "+Message.TOKENS+":" + p.tokens;
            if(p.connected)
                ins = ins + " "+Message.ONLINE;
            else
                ins = ins + " "+Message.OFFLINE;
            s.replace(0, ins.length(), ins);
            printStream.print(s);
        }
        printStream.print("\n");
    }

    private void showOthersWindows() {
        for (int x = 0; x < 4; x++) {
            for (PlayerClass p : playersList.subList(1, playersList.size())) {
                for (int y = 0; y < 5; y++) {
                    Cell c = p.glassWindow.cells[x * 5 + y];
                    if(c.content.equals(" ")) {
                        printActive(c.active, "[" + c.restriction + "]");
                    }else{
                        printActive(c.active, c.content.charAt(0) + " " + c.content.charAt(1));
                    }}
                printStream.print("\t" + "\t" + " ");
            }
            printStream.println();
        }
        printStream.println();
    }

    private void showRoundTrack() {
        printStream.print(Message.ROUND_TRACK + ": " + "\t");
        for (List<Die> dieList : roundTrack) {
            printStream.print(Message.ROUND.toString() + (roundTrack.indexOf(dieList)+1)+": ");
            for (Die die: dieList) {
                printActive(die.active, die.id);
            }

        }
        printStream.print("\n");
    }


    private void clear(){
        for (int i = 0; i < 10; i++) {
            printStream.println();
        }
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
            windowName = "NotYetChosen";
            cells = new Cell[20];
            for (int i = 0; i < 20; i++) {
                cells[i] = new Cell();
                cells[i].content = " ";
                cells[i].restriction = " ";
            }
        }
    }
    private class Cell{
        String content;
        String restriction;
        boolean active;
    }

    private class Die{
        final String id;
        boolean active;

        public Die(String die) {
            id = die;
        }
    }
}



