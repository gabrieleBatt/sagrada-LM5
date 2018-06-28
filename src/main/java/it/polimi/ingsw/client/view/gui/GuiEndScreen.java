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

    private static final double WIDTH_SCALE = 0.9;
    private static final double HEIGHT_SCALE = 1;
    private static final String FONT = "Forte";
    private static final String CSS_PATH = "/clientResources/gui/endGame/endGame.css";


    private boolean input = false;
    private EndGameInfo endGameInfo;

    private void setScene() {
        Stage stage = Client.getStage();

        Image image = new Image("/clientResources/gui/endGame/fillOption2.PNG", 200, 200, false, false);
        Text sceneTitle = new Text(Message.LEADER_BOARD.toString());
        sceneTitle.setFont(Font.font(FONT, FontWeight.EXTRA_BOLD, 90));

        sceneTitle.setFill(new ImagePattern(image,
                0, 0, image.getWidth(), image.getHeight(), false));


        BorderPane borderPane = new BorderPane();
        BorderPane.setAlignment(sceneTitle, Pos.TOP_CENTER);
        BorderPane.setMargin(sceneTitle, new Insets(50));
        borderPane.setTop(sceneTitle);

        VBox vbox = new VBox();
        Text player = null;

        Image image2 = new Image("/clientResources/gui/endGame/fillOption2.PNG", 200, 200, false, false);

        for (int i = 0; i < endGameInfo.getRanking().size(); i++) {
            player = new Text(10, 0, endGameInfo.getRanking().get(i).getKey() + "\t" + endGameInfo.getRanking().get(i).getValue());
            player.setFont(Font.font(FONT, FontWeight.EXTRA_BOLD, 60));
            player.setFill(new ImagePattern(image2,
                    0, 0, image.getWidth(), image.getHeight(), false));

            vbox.alignmentProperty().setValue(Pos.CENTER);
            vbox.getChildren().add(player);
            if (i != endGameInfo.getRanking().size() - 1) {
                Text space = new Text("\n \n");
                vbox.getChildren().add(space);
            }

        }

        Button button = new Button();
        button.setText("Play Again");

        button.setStyle("-fx-background-image: url('/clientResources/gui/endGame/fillOption2bisRid.jpg')");

        button.setShape(new Circle(70));

        button.setTextFill(Color.WHITE);
        button.setFont(Font.font(FONT, FontWeight.EXTRA_BOLD, 35));
        button.setMinSize(200, 100);
        button.setMaxSize(250, 100);
        button.setOnAction(event -> Platform.runLater(() -> input = true));
        button.setOnMouseEntered(event -> Platform.runLater(() -> button.setTextFill(Color.RED)));
        button.setOnMouseExited(event -> Platform.runLater(() -> button.setTextFill(Color.WHITE)));

        BorderPane.setAlignment(button, Pos.TOP_CENTER);
        BorderPane.setMargin(button, new Insets(50));
        borderPane.setBottom(button);

        BorderPane.setAlignment(vbox, Pos.CENTER);
        BorderPane.setMargin(vbox, new Insets(50));
        borderPane.setCenter(vbox);

        Scene scene = new Scene(borderPane, GuiView.WIDTH * WIDTH_SCALE, GuiView.HEIGHT * HEIGHT_SCALE);
        scene.getStylesheets().add
                (getClass().getResource(CSS_PATH).toExternalForm());

        stage.setScene(scene);
        stage.setMinWidth(GuiView.WIDTH * WIDTH_SCALE);
        stage.setMaxWidth(GuiView.WIDTH * WIDTH_SCALE);
        stage.setMinHeight(GuiView.HEIGHT * HEIGHT_SCALE);
        stage.setMaxHeight(GuiView.HEIGHT * HEIGHT_SCALE);

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
                    Thread.sleep(1);
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
