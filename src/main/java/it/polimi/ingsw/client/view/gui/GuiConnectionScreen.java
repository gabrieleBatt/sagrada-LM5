package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.LoginInfo;
import it.polimi.ingsw.client.view.factory.ConnectionScreen;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.factory.GuiView;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class GuiConnectionScreen extends ConnectionScreen {

    private LoginInfo loginInfo = null;

    public GuiConnectionScreen(){
        while (!Client.getStage().isPresent()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Stage stage = Client.getStage().get();

        Button button = new Button();
        button.setOnAction(e -> loginInfo = new LoginInfo("rmi",
                "gui",
                50000,
                "127.0.0.1",
                "password"));
        Scene scene = new Scene(button);
        Platform.runLater(() -> {
            stage.setScene(scene);
            stage.show();
        });

    }

    @Override
    public LoginInfo getConnectionInfo() {
        GuiView.waitFor(loginInfo);
        return loginInfo;
    }

    @Override
    public boolean reConnect() {
        return false;
    }
}
