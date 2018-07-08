package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.EndGameInfo;
import it.polimi.ingsw.client.view.factory.EndScreen;
import it.polimi.ingsw.client.view.factory.GuiView;
import it.polimi.ingsw.shared.Message;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GuiEndScreen extends EndScreen {

    private static final String CSS_PATH = "/clientResources/gui/endGame/endGame.css";
    private static final double BUTTON_HEIGHT = 100/GuiView.DEFAULT_HEIGHT*GuiView.HEIGHT;
    private static final double BUTTON_WIDTH = 250/GuiView.DEFAULT_WIDTH*GuiView.WIDTH;
    private static final double REQUEST_SIZE = 200/GuiView.DEFAULT_HEIGHT*GuiView.HEIGHT;
    private boolean input = false;
    private EndGameInfo endGameInfo;

    private void setScene() {
        Stage stage = Client.getStage();

        Image image = new Image("/clientResources/gui/endGame/fillOption2.PNG", REQUEST_SIZE, REQUEST_SIZE, false, false);
        Text sceneTitle = new Text(Message.LEADER_BOARD.toString());
        sceneTitle.setFont(Font.font(GuiView.FONT, FontWeight.EXTRA_BOLD, GuiView.BIG_END_SCREEN_FONT));

        sceneTitle.setFill(new ImagePattern(image,
                0, 0, image.getWidth(), image.getHeight(), false));

        BorderPane borderPane = new BorderPane();
        BorderPane.setAlignment(sceneTitle, Pos.TOP_CENTER);
        BorderPane.setMargin(sceneTitle, new Insets(GuiView.BIG_SPACING));
        borderPane.setTop(sceneTitle);

        VBox vbox = new VBox();
        Text player;

        Image image2 = new Image("/clientResources/gui/endGame/fillOption2.PNG", REQUEST_SIZE, REQUEST_SIZE, false, false);
        //if(endGameInfo != null)
        for (int i = 0; i < endGameInfo.getRanking().size(); i++) {
                player = new Text(10, 0, endGameInfo.getRanking().get(i).getKey() + "\t" + endGameInfo.getRanking().get(i).getValue());
                player.setFont(Font.font(GuiView.FONT, FontWeight.EXTRA_BOLD, GuiView.MEDIUM_END_SCREEN_FONT));
                player.setFill(new ImagePattern(image2,
                        0, 0, image.getWidth(), image.getHeight(), false));

                vbox.alignmentProperty().setValue(Pos.CENTER);
                vbox.getChildren().add(player);
                vbox.setSpacing(GuiView.MEDIUM_SPACING);

            }

        Button button = new Button();
        button.setText("Play Again");

        button.setStyle("-fx-background-image: url('/clientResources/gui/endGame/fillOption2.PNG')");

        button.setShape(new Circle(GuiView.RADIUS));

        button.setTextFill(Color.WHITE);
        button.setFont(Font.font(GuiView.FONT, FontWeight.EXTRA_BOLD, GuiView.SMALL_END_SCREEN_FONT));
        button.setMinSize(REQUEST_SIZE, BUTTON_HEIGHT);
        button.setMaxSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        button.setOnAction(event -> Platform.runLater(() -> input = true));
        button.setOnMouseEntered(event -> Platform.runLater(() -> button.setTextFill(Color.YELLOW)));
        button.setOnMouseExited(event -> Platform.runLater(() -> button.setTextFill(Color.WHITE)));

        BorderPane.setAlignment(button, Pos.TOP_CENTER);
        BorderPane.setMargin(button, new Insets(GuiView.BIG_SPACING));
        borderPane.setBottom(button);

        BorderPane.setAlignment(vbox, Pos.CENTER);
        BorderPane.setMargin(vbox, new Insets(GuiView.END_SCREEN_SPACING));
        borderPane.setCenter(vbox);

        Scene scene = new Scene(borderPane, GuiView.WIDTH , GuiView.HEIGHT );
        scene.getStylesheets().add
                (getClass().getResource(CSS_PATH).toExternalForm());

        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void showRanking(EndGameInfo endGameInfo) {
        this.endGameInfo = endGameInfo;
        Platform.runLater(this::setScene);
    }

    @Override
    public boolean playAgain() {
        Platform.runLater(this::setScene);

        Thread t = new Thread(() -> {
            while(!input) {
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return input;
    }


}
