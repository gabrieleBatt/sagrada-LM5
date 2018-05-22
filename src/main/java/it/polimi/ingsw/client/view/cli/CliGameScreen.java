package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.factory.GameScreen;
import javafx.util.Pair;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

public class CliGameScreen implements GameScreen {

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

    public CliGameScreen(InputStream inputStream, PrintStream printStream){
        scanner = new Scanner(inputStream);
        this.printStream = printStream;
        printStream.println((char)27+ "[37m");
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
    public void setTools(Collection<Pair <String, Boolean>> tools){
        toolsList = new ArrayList<>();
        for(Pair<String, Boolean> t: tools){
            ToolClass newTool = new ToolClass();
            newTool.toolName = t.getKey();
            newTool.used = t.getValue();
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
        while (!(options.contains(choice) || (choice.equalsIgnoreCase("skip"))&&skip) || (choice.equalsIgnoreCase("undo"))&&undo) {
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
        if(ret.isPresent())
            return ret.get();
        else
            throw new NoSuchElementException();

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
        if(ret.isPresent())
            return ret.get();
        else
            throw new NoSuchElementException();
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
        printStream.println(message + (char)27 + "[31m"+" " + strings +(char)27 + "[37m");
        choice = scanner.nextLine();
        while(!strings.contains(choice)) {
            printStream.println("Scelta non valida ");
            choice = scanner.nextLine();
        }
        return choice;
    }

    @Override
    public void showAll(){
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
        printStream.println();
        if(skip)
            printStream.print("skip");
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
        printStream.print("Obbiettivi pubblici: ");
        for (String publicObjective : publicObjectives) {
            printStream.print(publicObjective+ "\t");
        }
        printStream.print("\n");
    }

    private void showPool() {
        printStream.print("Riserva: ");
        for (Die die : poolDice) {
            printActive(die.active,die.id.substring(0, 2));
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
            printStream.print("round" + (roundTrack.indexOf(dieList)+1)+": ");
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

















}



