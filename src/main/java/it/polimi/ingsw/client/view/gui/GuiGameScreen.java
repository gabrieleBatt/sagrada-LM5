package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.factory.GameScreen;
import it.polimi.ingsw.shared.Message;
import it.polimi.ingsw.shared.identifiables.StdId;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image ;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.text.html.HTMLWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static javafx.scene.paint.Color.WHITE;
import static javafx.scene.paint.Color.color;


public class GuiGameScreen extends GameScreen {

    private static final double CELL_OPPONET_WIDTH_MULT = 0.0171875;
    private static final double CELL_OPPONET_HEIGHT_MULT = 0.0323529;
    private Collection<ImageView> privateObjectives;
    private Collection<ImageView> publicObjectives;
    private List<ToolButton> toolsList;
    private List<PlayerClass> playersList;
    private List<DieButton> poolDice;
    private List<List<DieButton>> roundTrack;
    private WindowClass mainPlayerWindow;
    private List<String> messageRecord;
    private Stage gameStage;
    private final String resource = "/clientResources/gui";
    private final String PNG = ".png";
    private static double WIDTH;
    private static double HEIGHT;
    private StackPane tableStackPane;
    private String input;
    private static final Image NO_RESTRICTION = new Image("/clientResources/gui/restrictions/noRestr.png");
    private static final String FONT = "Algerian";
    private static final String NO_MESSAGES = "";
    private static final double DEFAULT_HEIGHT = 680;
    private static final double DEFAULT_WIDTH  = 1280;
    private static final double CELL_WIDTH_MULT  = 0.041322314;
    private static final double CELL_HEIGHT_MULT  = 0.07518797;
    private static final double BUTTON_WIDTH_MULT  = 0.049322314;
    private static final double BUTTON_HEIGHT_MULT  = 0.05518797;
    private static final double CARD_WIDTH_MULT = 0.11111111111;
    private static final double CARD_HEIGHT_MULT = 0.2727142857;
    private static final double WINDOW_BUTTON_WIDTH_MULT = 0.20961157;
    private static final double WINDOW_BUTTON_HEIGHT_MULT = 0.3100751;
    private static final int ROUND_TRACK_SIZE = 10;
    private static final int CELL_NUM = 20;
    private static final Object CARD_NAME = "name";
    private static final Object CARD_POINTS = "points";
    private static final Object CARD_DESCRIPTION = "description";
    private static final String NO_WINDOW = "NoWindow";
    private static final Object WINDOW_CELLS = "cells";
    private static final String JSON_EXTENSION = ".json";
    private static final Object DIFFICULTY = "difficulty";
    private static final int COLUMNS = 5;
    private static final String OK = "OK";
    private static final String SHOW_MESSAGES = "Show messages";
    private static final double BIG_SPACING;
    private static final double SMALL_SPACING;
    private static final double MEDIUM_SPACING;
    private static final double BIGGER_SPACING;
    private static final double BIGGER_MULT = 1.05;
    private static final double SMALLER_MULT = 0.8;
    private static final int MAX_MSGS_LENGHT = 15;
    private static final double MESSAGES_MIN_WIDTH;

    static {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        WIDTH = primaryScreenBounds.getWidth();
        HEIGHT = primaryScreenBounds.getHeight();

        if(DEFAULT_WIDTH/DEFAULT_HEIGHT < WIDTH/HEIGHT)
            WIDTH = DEFAULT_WIDTH*HEIGHT/DEFAULT_HEIGHT;
        else
            HEIGHT = DEFAULT_HEIGHT*WIDTH/DEFAULT_WIDTH;
        MESSAGES_MIN_WIDTH = 350/DEFAULT_WIDTH*WIDTH;
        BIGGER_SPACING = 130/DEFAULT_WIDTH*WIDTH;
        BIG_SPACING = 50/DEFAULT_WIDTH*WIDTH;
        SMALL_SPACING = 5/DEFAULT_WIDTH*WIDTH;
        MEDIUM_SPACING = 15/DEFAULT_WIDTH*WIDTH;
    }

    private Scene selectWindowScene;
    private Scene gameScene;
    private GridPane gameGridPane;
    private StackPane mainPlayerStackPane;
    private GridPane objectiveGridPane;
    private GridPane toolsGridPane;
    private GridPane opponentsWindowPane;
    private VBox diceOnTableVBox;
    private VBox messagesVBox;
    private Button skipButton = new Button();
    private Button undoButton = new Button();
    private Button toolButton = new Button();
    private Button drawButton = new Button();
    private Button messageButton = new Button();
    private Stage messageStage;
    private Text messages;
    private Scene messageScene;
    private Text dialogText;
    private StackPane messageStackPane;

    public GuiGameScreen(){
        this.messageRecord = new ArrayList<>();
        privateObjectives = new ArrayList<>();
        publicObjectives = new ArrayList<>();
        toolsList = new ArrayList<>();
        playersList = new ArrayList<>();
        poolDice = new ArrayList<>();
        roundTrack = new ArrayList<>();
        gameStage = Client.getStage().get();
        mainPlayerWindow = new WindowClass();
        gameGridPane = new GridPane();
        diceOnTableVBox = new VBox();
        messagesVBox = new VBox();
        dialogText = new Text(NO_MESSAGES);
        messagesVBox.setAlignment(Pos.CENTER);
        diceOnTableVBox.setSpacing(MEDIUM_SPACING);

        gameGridPane.setVgap(SMALL_SPACING);
        gameGridPane.setHgap(BIG_SPACING);
        mainPlayerStackPane = new StackPane();
        objectiveGridPane = new GridPane();
        objectiveGridPane.setHgap(MEDIUM_SPACING);
        toolsGridPane = new GridPane();
        toolsGridPane.setHgap(MEDIUM_SPACING);
        opponentsWindowPane = new GridPane();
        setButton(skipButton, StdId.SKIP);
        setButton(undoButton, StdId.UNDO);
        setButton(toolButton, StdId.USE_TOOL);
        setButton(drawButton, StdId.DRAFT);

        messageButton.setText(SHOW_MESSAGES);
        messageButton.setFont(Font.font(FONT,20));
        messageButton.setOnAction(event -> {
            if(!messageStage.isShowing())
                messageStage.show();
        });

        gameGridPane.add(messagesVBox,0,0);
        gameGridPane.add(diceOnTableVBox,1,0);
        gameGridPane.add(opponentsWindowPane,2,0);
        gameGridPane.add(mainPlayerStackPane,1,1);
        gameGridPane.add(objectiveGridPane,0,1);
        gameGridPane.add(toolsGridPane,2,1);

        tableStackPane = new StackPane();
        tableStackPane.getChildren().add(gameGridPane);
        gameScene = new Scene(tableStackPane);
        gameScene.getStylesheets().addAll(this.getClass().getResource("/clientResources/gui/guiGameScreen.css").toExternalForm());
        tableStackPane.setId("table");
    }

    private void setButton(Button button, StdId stdId) {
        button.setMinSize(WIDTH*BUTTON_WIDTH_MULT,HEIGHT*BUTTON_HEIGHT_MULT);
        button.setText(Message.convertMessage(stdId.getId()));
        button.setFont(Font.font(FONT));
        button.setOnAction(event ->
                Platform.runLater(() -> input = stdId.getId()
                ));
        button.setDisable(true);
    }


    @Override
    public void addMessage(String message) throws RemoteException {
        messageRecord.add(0,Message.convertMessage(message)+"\n");
        if(messageRecord.size() > MAX_MSGS_LENGHT)
            messageRecord = messageRecord.subList(0, MAX_MSGS_LENGHT);
        Platform.runLater(()-> {
            messages = new Text();
            messageStackPane = new StackPane();
            messageStackPane.setStyle("-fx-background-color: #e7bb77");
            messageStackPane.setMinWidth(MESSAGES_MIN_WIDTH);
            messageStackPane.getChildren().add(messages);
            messageStackPane.setAlignment(Pos.CENTER_LEFT);
            messageScene = new Scene(messageStackPane);
            messageStage = new Stage();
            messageStage.setScene(messageScene);
            String msgs = new String();
            messagesVBox.getChildren().remove(0, messagesVBox.getChildren().size());
            for (int i = messageRecord.size()-1; i>=0; i--) {
                msgs = msgs + messageRecord.get(i);
            }
            messages.setText(msgs);
            messages.setFont(Font.font(FONT,20));
            dialogText.setFont(Font.font(FONT,20));
            dialogText.setFill(WHITE);
            messagesVBox.setSpacing(MEDIUM_SPACING);
            messagesVBox.getChildren().add(messageButton);
            messagesVBox.getChildren().add(dialogText);
        });
    }

    @Override
    public void setPlayers(List<String> nicknames) throws RemoteException {
        if (!playersList.stream().map(p -> p.nickname).collect(Collectors.toList()).equals(nicknames)) {
            playersList = new ArrayList<>();
            for (String s : nicknames) {
                PlayerClass newPlayer = new PlayerClass();
                newPlayer.nickname = s;
                newPlayer.playerText = new Text();
                playersList.add(newPlayer);
            }

        }

    }

    @Override
    public void setPrivateObjectives(Collection<String> privateObjectivesStrings) throws RemoteException {
        this.privateObjectives = new ArrayList<>();
        for (String privateObjective : privateObjectivesStrings) {
            ImageView privateObView = new ImageView(new Image(resource + "/PrivateOb/" + privateObjective + PNG));
            privateObView.fitHeightProperty().setValue(HEIGHT*CARD_HEIGHT_MULT);
            privateObView.fitWidthProperty().setValue(WIDTH*CARD_WIDTH_MULT);
            privateObjectives.add(privateObView);
        }
        updateObjectives();
        //todo manage more than 1 objective case
    }

    @Override
    public void setPublicObjective(Collection<String> publicObjectivesStrings){
        this.publicObjectives = new ArrayList<>();
        for (String publicObjective : publicObjectivesStrings) {
            ImageView publicObView = new ImageView(new Image(resource + "/PublicOb/" + publicObjective + PNG));
            publicObView.fitHeightProperty().setValue(HEIGHT*CARD_HEIGHT_MULT);
            publicObView.fitWidthProperty().setValue(WIDTH*CARD_WIDTH_MULT);
            publicObjectives.add(publicObView);
        }
        updateObjectives();
        //todo manage more than three objective case
    }

    private void updateObjectives(){
        Platform.runLater( () -> {
            int i = 0;
            objectiveGridPane.getChildren().remove(0, objectiveGridPane.getChildren().size());
            for (ImageView publicObjective : this.publicObjectives) {
                objectiveGridPane.add(publicObjective, i / 2, i % 2);
                i++;
            }
            for (ImageView privateObjective : privateObjectives) {
                objectiveGridPane.add(privateObjective, i / 2, i % 2);
                i++;
            }
        });
    }

    @Override
    public void setTools(Collection<String> tools){
        toolsList = new ArrayList<>();
        for(String t: tools){
            ToolButton newTool = new ToolButton();
            newTool.toolName = t;
            ImageView toolImageView = new ImageView(new Image(resource + "/Tools/" + t + PNG));
            toolImageView.fitHeightProperty().setValue(HEIGHT*CARD_HEIGHT_MULT*SMALLER_MULT);
            toolImageView.fitWidthProperty().setValue(WIDTH*CARD_WIDTH_MULT*SMALLER_MULT);
            newTool.setDisable(true);
            newTool.setMinSize(WIDTH*CARD_WIDTH_MULT*SMALLER_MULT,HEIGHT*CARD_HEIGHT_MULT*SMALLER_MULT);
            newTool.setGraphic(toolImageView);
            toolsList.add(newTool);
        }
        Platform.runLater( () -> {
            //setting buttons
            toolsGridPane.getChildren().removeAll(toolsGridPane.getChildren());
            VBox buttonVBox = new VBox();
            buttonVBox.setSpacing(SMALL_SPACING);
            buttonVBox.getChildren().add(undoButton);
            buttonVBox.getChildren().add(skipButton);
            buttonVBox.getChildren().add(toolButton);
            buttonVBox.getChildren().add(drawButton);
            buttonVBox.setAlignment(Pos.CENTER);
            toolsGridPane.add(buttonVBox, 0, 0);

            //setting tools VBox and effect
            int i = 1;
            for (ToolButton t : toolsList) {
                t.toolDescription.setText(Message.convertName(t.toolName) + "\n" + Message.TOKENS.toString() + ": 1");
                toolsGridPane.add(t.toolVBox, i / 2, i % 2);
                i++;
                t.setOnAction(event ->
                        Platform.runLater(() -> input = t.toolName
                        ));
            }
        });
        //todo manage four or more tools case
    }

    @Override
    public void setToolUsed(String tool, boolean used) throws RemoteException {
        for(ToolButton t: toolsList)
            if (t.toolName.equals(tool)) {
                Platform.runLater(() -> {
                    if (!used) {
                        t.toolDescription.setText(Message.convertName(t.toolName) + "\n" + Message.TOKENS.toString() + ": 1");
                    } else
                        t.toolDescription.setText(Message.convertName(t.toolName) + "\n" + Message.TOKENS.toString() + ": 2");
                });
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
    public void setPlayerWindow(String nickname, String windowName) throws RemoteException {
        for(int i = 0; i<this.playersList.size(); i++ ){
            if(playersList.get(i).nickname.equals(nickname) && !playersList.get(i).glassWindow.windowName.equals(windowName)) {
                playersList.get(i).glassWindow = new WindowClass(windowName);
                playersList.get(i).glassWindow.setPlayerWindow(i!=0);
            }
        }

    }

    @Override
    public void setCellContent(String nickname, int x, int y, String die) throws RemoteException {

        Optional<PlayerClass> playerClassOptional = playersList.stream()
                .filter(p -> p.nickname.equals(nickname))
                .findFirst();
        playerClassOptional.ifPresent(playerClass -> Platform.runLater(() -> {
            ImageView image;
            if (die.equals(" "))
                image = playerClass.glassWindow.cells[x * COLUMNS + y].restriction;
            else
                image = new ImageView(new Image(resource + "/dice/" + die.charAt(0) + die.charAt(1) + PNG));

            playerClass.glassWindow.cells[x * COLUMNS + y].showingImage = image;

            playerClass.glassWindow.setPlayerWindow(!playersList.get(0).equals(playerClass));
        }));
    }

    @Override
    public void setPool(Collection<String> dice) throws RemoteException {
        List<String> sortedDice = new ArrayList<>(dice);
        sortedDice.sort(String::compareTo);
        poolDice = new ArrayList<>();
        for (String die : sortedDice) {
            DieButton dieButton = new DieButton(die,CELL_WIDTH_MULT,CELL_HEIGHT_MULT);
            poolDice.add(dieButton);
        }
        setDiceOnTable();

    }

    @Override
    public void setRoundTrack(List<List<String>> dice) throws RemoteException {
        Platform.runLater(() -> {
            roundTrack = new ArrayList<>();

            for (List<String> list : dice) {
                List<DieButton> ld = new ArrayList<>();
                for (String s : list) {
                    DieButton dieButton = new DieButton(s,CELL_WIDTH_MULT,CELL_HEIGHT_MULT);
                    ld.add(dieButton);
                }
                roundTrack.add(ld);

            }
            setDiceOnTable();
        });
    }

    private void setDiceOnTable(){


        Platform.runLater(() -> {

            diceOnTableVBox.getChildren().remove(0, diceOnTableVBox.getChildren().size());
            diceOnTableVBox.setAlignment(Pos.CENTER);
            HBox roundTrackHBox = new HBox();
            roundTrackHBox.setSpacing(SMALL_SPACING);
            roundTrackHBox.setAlignment(Pos.CENTER);

            for (int i = 0; i<roundTrack.size();i++) {
                    Button roundButton = new Button();
                    roundButton.setMinSize(WIDTH*CELL_OPPONET_WIDTH_MULT*BIGGER_MULT,HEIGHT*CELL_OPPONET_HEIGHT_MULT*BIGGER_MULT);
                    roundButton.setStyle("-fx-background-color: #a5814c");
                    Integer j = i+1;
                    roundButton.setText(j.toString());
                    roundButton.setFont(Font.font(FONT,10));

                    VBox roundTrackVBox = new VBox();
                    Stage roundTrackStage = new Stage();
                    Scene roundTrackCellScene = new Scene(roundTrackVBox);
                    roundTrackStage.setScene(roundTrackCellScene);
                    roundTrackVBox.setStyle("-fx-background-color: #c7a96e");
                    for(DieButton dieButton: roundTrack.get(i)){
                        roundTrackVBox.getChildren().add(dieButton);
                    }

                    roundButton.setOnAction(event->{
                        if(!roundTrackStage.isShowing())
                            roundTrackStage.show();
                        else
                            roundTrackStage.close();
                    });
                    roundTrackHBox.getChildren().add(roundButton);
            }

            for (int i = roundTrack.size(); i < ROUND_TRACK_SIZE; i++) {
                Button roundButton = new Button();

                roundButton.setMinSize(WIDTH*CELL_OPPONET_WIDTH_MULT*BIGGER_MULT,HEIGHT*CELL_OPPONET_HEIGHT_MULT*BIGGER_MULT);

                roundButton.setDisable(true);
                roundButton.setStyle("-fx-background-color: #a5814c");

                Integer j = i+1;
                roundButton.setText(j.toString());
                roundButton.setFont(Font.font(FONT,10));
                roundTrackHBox.getChildren().add(roundButton);
            }

            diceOnTableVBox.getChildren().add(roundTrackHBox);

            int i = 0;
            VBox poolVBox = new VBox();
            poolVBox.setAlignment(Pos.CENTER);
            HBox firstLine = new HBox();
            firstLine.setAlignment(Pos.CENTER);
            HBox secondLine = new HBox();
            secondLine.setAlignment(Pos.CENTER);
            for (DieButton dieButton : poolDice) {
                if (i % 2 == 0)
                    firstLine.getChildren().add(dieButton);
                else
                    secondLine.getChildren().add(dieButton);
                i++;
            }
            poolVBox.getChildren().add(firstLine);
            poolVBox.getChildren().add(secondLine);
            diceOnTableVBox.getChildren().add(poolVBox);
        });
    }

    @Override
    public String getWindow(Collection<String> windows) throws RemoteException {
        Platform.runLater(() -> {
            Text chooseText = new Text(Message.CHOOSE_WINDOW.toString());


            chooseText.setFont(Font.font(FONT,25));
            chooseText.setFill(WHITE);
            chooseText.setWrappingWidth(WIDTH/4);

            GridPane windowsGridPane = new GridPane();
            List<Button> buttons = new ArrayList<>();
            input = null;

            Iterator<String> iterator = windows.iterator();

            int index = 0;
            while (iterator.hasNext()){

                String w = iterator.next();

                VBox windowVBox = new VBox();
                setGWInWindSelection(w, windowVBox);
                windowsGridPane.add(windowVBox, index / 2, index % 2);
                Button windowButton = new Button();
                windowButton.setOpacity(0);
                windowButton.setMinSize(WIDTH*WINDOW_BUTTON_WIDTH_MULT, HEIGHT*WINDOW_BUTTON_HEIGHT_MULT);;
                windowsGridPane.add(windowButton, index / 2, index % 2);
                windowsGridPane.setAlignment(Pos.TOP_CENTER);
                buttons.add(windowButton);

                windowButton.setOnAction(event -> Platform.runLater( () -> {
                    input = w;

                    for (Button b : buttons) {
                        b.disarm();
                    }
                    gameStage.setScene(gameScene);
                    gameStage.show();
                }));
                index++;
            }

            GridPane windowChoiceGridPane = new GridPane();
            windowChoiceGridPane.setPadding(new Insets(HEIGHT/30));
            windowChoiceGridPane.setHgap(HEIGHT/20);
            windowChoiceGridPane.add(chooseText,0,0);
            windowsGridPane.setHgap(HEIGHT/30);
            windowsGridPane.setVgap(HEIGHT/30);
            windowChoiceGridPane.add(windowsGridPane,1,0);

            privateObjectives.forEach(po -> {
                windowChoiceGridPane.add(po,2,0);
            });
            StackPane windowChoiceSP = new StackPane();
            windowChoiceSP.getChildren().add(windowChoiceGridPane);
            selectWindowScene = new Scene(windowChoiceSP, WIDTH, HEIGHT);
            selectWindowScene.getStylesheets().addAll(this.getClass().getResource("/clientResources/gui/guiGameScreen.css").toExternalForm());

            windowChoiceSP.setId("table");
            gameStage.setScene(selectWindowScene);
        });

        waitInput();

        return input;
    }

    private void setGWInWindSelection(String window,VBox windowBox) {


        WindowClass windowPane = new WindowClass(window);
        windowPane.setPlayerWindow(false);

        Label windowDifficulty = new Label();
        Label windowName = new Label();

        windowBox.getChildren().add(windowPane);
        windowBox.getChildren().add(windowName);
        windowBox.getChildren().add(windowDifficulty);

        windowName.setText(Message.convertName(windowPane.windowName));
        windowName.setFont(Font.font(FONT,20));
        windowName.setTextFill(WHITE);

        windowDifficulty.setText(Message.TOKENS + " " + String.valueOf(windowPane.tokens));
        windowDifficulty.setFont(Font.font(FONT,15));
        windowDifficulty.setTextFill(WHITE);

    }

    @Override
    public String getInput(Collection<String> options, String container) throws RemoteException {
        if(options.contains(StdId.SKIP.getId())){
            options.remove(StdId.SKIP.getId());
            skipButton.setDisable(false);
        }
        if(options.contains(StdId.UNDO.getId())){
            options.remove(StdId.UNDO.getId());
            undoButton.setDisable(false);
        }

        List<DieButton> activeDie = new ArrayList<>();
        List<ToolButton> activeTools = new ArrayList<>();
        List<CellButton> activeCells = new ArrayList<>();

        if(container.equalsIgnoreCase(StdId.POOL.getId())) {
            for(DieButton dieButton:poolDice){
                if(options.contains(dieButton.dieId)){
                    dieButton.setDisable(false);
                    activeDie.add(dieButton);
                }
            }
        }else if (container.equalsIgnoreCase(StdId.ROUND_TRACK.getId())) {
            for(List<DieButton> list:roundTrack)
                for(DieButton dieButton:list){
                    if(options.contains(dieButton.dieId)){
                        dieButton.setDisable(false);
                        activeDie.add(dieButton);
                    }
                }
        }else if (container.equalsIgnoreCase(StdId.GLASS_WINDOW.getId())) {
            for(CellButton cellButton: mainPlayerWindow.cells){
                if(options.contains(cellButton.idCell)){
                    cellButton.setDisable(false);
                    activeCells.add(cellButton);
                }
            }

        }else if (container.equalsIgnoreCase(StdId.TABLE.getId())) {
            for(ToolButton tb: toolsList){
                if(options.contains(tb.toolName)){
                    tb.setDisable(false);
                    activeTools.add(tb);
                }
            }

        }else{
            throw new IllegalArgumentException();
        }

        waitInput();


        activeDie.forEach(d->d.setDisable(true));
        activeCells.forEach(c->c.setDisable(true));
        activeTools.forEach(t->t.setDisable(true));
        skipButton.setDisable(true);
        undoButton.setDisable(true);

        return input;
    }

    @Override
    public String getInputFrom(Collection<String> options, String message) throws RemoteException {
        dialogText.setText(Message.convertMessage(message));

        if(options.contains(StdId.SKIP.getId())){
            options.remove(StdId.SKIP.getId());
            skipButton.setDisable(false);
        }
        if(options.contains(StdId.UNDO.getId())){
            options.remove(StdId.UNDO.getId());
            undoButton.setDisable(false);
        }
        if(options.contains(StdId.DRAFT.getId())){
            options.remove(StdId.DRAFT.getId());
            drawButton.setDisable(false);
        }
        if(options.contains(StdId.USE_TOOL.getId())){
            options.remove(StdId.USE_TOOL.getId());
            toolButton.setDisable(false);
        }
        if(!options.isEmpty()) {
            ComboBox<String> comboBox = new ComboBox<>();
            Platform.runLater(() -> {
                comboBox.setItems(FXCollections.observableArrayList(options));
                Button button = new Button();
                button.setText(OK);
                button.setFont(Font.font(FONT));
                messagesVBox.getChildren().add(comboBox);
                messagesVBox.getChildren().add(button);
                button.setOnAction(event -> {
                    if (comboBox.getValue() != null)
                        input = comboBox.getValue();
                });
            });
        }

        waitInput();

        toolButton.setDisable(true);
        drawButton.setDisable(true);
        skipButton.setDisable(true);
        undoButton.setDisable(true);
        dialogText.setText(NO_MESSAGES);

        return input;
    }


    @Override
    public void showAll( ) throws RemoteException {
        showGameStage();
    }

    private void showGameStage(){
        Platform.runLater(() -> {

            showWindows();

            if(playersList.size() > 2)
                gameGridPane.setPadding(new Insets(SMALL_SPACING,BIGGER_SPACING,SMALL_SPACING,BIGGER_SPACING));
            else
                gameGridPane.setPadding(new Insets(BIG_SPACING,BIGGER_SPACING,BIG_SPACING,BIGGER_SPACING));

            gameStage.setX(Screen.getPrimary().getBounds().getMinX());
            gameStage.setY(Screen.getPrimary().getBounds().getMinY());
            gameStage.setWidth(WIDTH);
            gameStage.setHeight(HEIGHT);

            gameStage.setMaximized(true);

            gameStage.show();
        });
    }

    private class WindowClass extends GridPane {
        long tokens;
        String windowName;
        CellButton[] cells;

        WindowClass() {
            this.windowName = NO_WINDOW;
            this.cells = new CellButton[CELL_NUM];

            for (int i = 0; i < CELL_NUM; i++) {
                this.cells[i] = new CellButton();
                this.cells[i].restriction = new ImageView(NO_RESTRICTION);
                this.cells[i].setGraphic(this.cells[i].restriction);
            }
        }

        WindowClass(String windowName) {
            this.windowName = windowName;

            cells = new CellButton[CELL_NUM];

            try {
                JSONObject jsonObject = (JSONObject) new JSONParser().parse(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(GLASS_WINDOW_PATH + windowName + JSON_EXTENSION)));
                List<String> restrictions = new ArrayList<>((JSONArray) jsonObject.get(WINDOW_CELLS));
                for (int i = 0; i < CELL_NUM; i++) {
                    this.cells[i] = new CellButton();
                    if(restrictions.get(i).charAt(0) == ' ') {
                        this.cells[i].restriction = new ImageView(NO_RESTRICTION);
                    }else {
                        this.cells[i].restriction = new ImageView(new Image(resource + "/restrictions/restr" + restrictions.get(i).charAt(0) + PNG));
                    }

                    final int x = i/COLUMNS;
                    final int y = i%COLUMNS;
                    this.cells[i].showingImage = this.cells[i].restriction;
                    this.cells[i].setOnAction(event -> input = x + "" + y + ":" + this.windowName);
                    this.cells[i].setDisable(true);
                    this.cells[i].idCell = x + "" + y + ":" + this.windowName;
                    this.add(this.cells[i],y,x);
                }

                this.tokens = (long)jsonObject.get(DIFFICULTY);
            } catch (IOException | ParseException e) {
                //WINDOW NOT FOUND MESSAGE
            }
        }

        void setPlayerWindow(Boolean isOpponent){
            for (CellButton cellButton : this.cells) {
                    if(isOpponent){
                        cellButton.restriction.fitWidthProperty().setValue(WIDTH*CELL_OPPONET_WIDTH_MULT);
                        cellButton.restriction.fitHeightProperty().setValue(HEIGHT*CELL_OPPONET_HEIGHT_MULT);
                        cellButton.showingImage.fitWidthProperty().setValue(WIDTH*CELL_OPPONET_WIDTH_MULT);
                        cellButton.showingImage.fitHeightProperty().setValue(HEIGHT*CELL_OPPONET_HEIGHT_MULT);
                        cellButton.setMinSize(WIDTH*CELL_OPPONET_WIDTH_MULT,HEIGHT*CELL_OPPONET_HEIGHT_MULT);
                        cellButton.setMaxSize(WIDTH*CELL_OPPONET_WIDTH_MULT,HEIGHT*CELL_OPPONET_HEIGHT_MULT);
                        this.setGridLinesVisible(true);
                    }
                    else{
                        cellButton.restriction.fitWidthProperty().setValue(WIDTH*CELL_WIDTH_MULT);
                        cellButton.restriction.fitHeightProperty().setValue(HEIGHT*CELL_HEIGHT_MULT);
                        cellButton.showingImage.fitWidthProperty().setValue(WIDTH*CELL_WIDTH_MULT);
                        cellButton.showingImage.fitHeightProperty().setValue(HEIGHT*CELL_HEIGHT_MULT);
                        cellButton.setMinSize(WIDTH*CELL_WIDTH_MULT*BIGGER_MULT,HEIGHT*CELL_HEIGHT_MULT*BIGGER_MULT);
                        cellButton.setMaxSize(WIDTH*CELL_WIDTH_MULT*BIGGER_MULT,HEIGHT*CELL_HEIGHT_MULT*BIGGER_MULT);

                        this.setStyle("-fx-background-color: black");
                        this.setVgap(1);
                        this.setHgap(1);
                    }

                    cellButton.setGraphic(cellButton.showingImage);
                    cellButton.setStyle("-fx-background-color: none");
                    cellButton.setOnMouseEntered(event -> {
                        if(!cellButton.isDisable()) cellButton.setStyle("-fx-background-color: lightgray");});
                    cellButton.setOnMouseExited(event -> {
                        if(!cellButton.isDisable()) cellButton.setStyle("-fx-background-color: none");});
                }
            }
    }

    private class CellButton extends Button {
        String idCell;
        ImageView restriction;
        ImageView showingImage;
    }

    private class ToolButton extends Button{
        VBox toolVBox;
        Text toolDescription;
        String toolName;

        ToolButton(){
            toolVBox = new VBox();
            toolDescription = new Text();
            toolDescription.setFont(Font.font(FONT));
            toolDescription.setFill(WHITE);
            toolVBox.getChildren().add(this);
            toolVBox.getChildren().add(toolDescription);
        }
    }

    private class PlayerClass{
        String nickname;
        int tokens;
        boolean connected;
        WindowClass glassWindow = new WindowClass();
        Text playerText;
    }


    private class DieButton extends Button {
        final String dieId;

        DieButton(String die,double cellWithMult, double cellHeightMult) {
            dieId = die;
            ImageView buttonImage = new ImageView(new Image(resource+"/dice/"+die.charAt(0)+die.charAt(1)+PNG));
            buttonImage.fitWidthProperty().setValue(WIDTH*cellWithMult);
            buttonImage.fitHeightProperty().setValue(HEIGHT*cellHeightMult);
            this.setMinSize(WIDTH*cellWithMult*BIGGER_MULT,HEIGHT*cellHeightMult*BIGGER_MULT);
            this.setMaxSize(WIDTH*cellWithMult*BIGGER_MULT,HEIGHT*cellHeightMult*BIGGER_MULT);

            this.setDisable(true);
            this.setGraphic(buttonImage);
            this.setStyle("-fx-background-color: none");
            this.setOnMouseEntered(event -> {if(!this.isDisable())
                this.setStyle("-fx-background-color: lightgray");});
            this.setOnMouseExited(event -> {if(!this.isDisable())
                this.setStyle("-fx-background-color: none");});
            this.setOnAction(event ->
                    Platform.runLater(() -> input = dieId)
            );
        }
    }

    private void showWindows() {
        Platform.runLater(() -> {
                    opponentsWindowPane.getChildren().remove(0,opponentsWindowPane.getChildren().size());
                    mainPlayerStackPane.getChildren().remove(0,mainPlayerStackPane.getChildren().size());
                    mainPlayerStackPane.setAlignment(Pos.CENTER);
                    opponentsWindowPane.setHgap(10);
                    for (int i = 0; i < playersList.size(); i++) {
                        PlayerClass player = playersList.get(i);
                        player.playerText.setText(player.nickname + "\n" + Message.TOKENS + ": " + player.tokens);
                        player.playerText.setFill(WHITE);
                        VBox playerVBox = new VBox();
                        playerVBox.setAlignment(Pos.CENTER);
                        playerVBox.setSpacing(15);
                        playerVBox.getChildren().add(player.playerText);
                        playerVBox.getChildren().add(player.glassWindow);
                        if (i == 0) {
                            mainPlayerStackPane.getChildren().add(playerVBox);
                            player.playerText.setFont(Font.font(FONT,25));
                            mainPlayerWindow = player.glassWindow;

                        } else {
                            opponentsWindowPane.add(playerVBox, i / 2, i % 2);
                            player.playerText.setFont(Font.font(FONT,10));
                        }
                        mainPlayerStackPane.setAlignment(Pos.CENTER);
                        opponentsWindowPane.setAlignment(Pos.CENTER);
                    }

        }
        );
    }

    private void waitInput() {
        input = null;
        Thread t = new Thread(() -> {
            while (input == null) {
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
