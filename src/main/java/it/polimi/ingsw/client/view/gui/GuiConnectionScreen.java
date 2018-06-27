package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.LoginInfo;
import it.polimi.ingsw.client.view.factory.ConnectionScreen;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.shared.Message;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;



public class GuiConnectionScreen extends ConnectionScreen {

    private LoginInfo loginInfo = null;
    private GridPane grid;
    private String RMI = "RMI";
    private String SOCKET = "SOCKET";

    /**
     * Sets the login scene, catches login info.
     */

    public void setScene(){
        while (!Client.getStage().isPresent()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Platform.runLater(() -> {
            Stage stage = Client.getStage().get();
            stage.setTitle(Message.START_GAME.toString());

            Integer number = ThreadLocalRandom.current().nextInt(1, 10000);


            grid = new GridPane();
            grid.setAlignment(Pos.BOTTOM_CENTER);
            grid.setHgap(50);
            grid.setVgap(15);
            grid.setPadding(new Insets(25, 25, 25, 25));
            grid.getHgap();
            Button btn = new Button(Message.PLAY.toString());
            HBox hbBtn = new HBox(10);
            btn.setMinSize(75,35);
            hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
            hbBtn.getChildren().add(btn);
            grid.add(hbBtn, 1, 12);


            ChoiceBox box = new ChoiceBox();
            box.getItems().addAll(RMI, SOCKET);
            grid.add(box,1,4);
            final Text actiontarget = new Text();

            grid.add(actiontarget, 0, 11);

            Scene scene = new Scene(grid, 475, 630);
            stage.setScene(scene);
            scene.getStylesheets().add
                    (GuiConnectionScreen.class.getResource("/clientResources/gui/Login.css").toExternalForm());

            List<TextField> textFieldArray = new ArrayList<>();
            Text scenetitle = new Text(Message.WELCOME.toString());
            scenetitle.setId("welcome-text");
            grid.add(scenetitle, 0, 0, 2, 1);

            Label userName = new Label(Message.CHOOSE_NICKNAME.toString());
            userName.setId("actiontarget");
            grid.add(userName, 0, 1);

            TextField userTextField = new TextField();
            grid.add(userTextField, 1, 1);
            textFieldArray.add(userTextField);

            Label pw = new Label(Message.INSERT_PASSWORD.toString());
            pw.setId("actiontarget");
            grid.add(pw, 0, 2);

            Label ip = new Label(Message.IP_NUMBER.toString());
            ip.setId("actiontarget");
            grid.add(ip, 0, 6);

            Label cnn = new Label(Message.CHOOSE_CONNECTION.toString());
            cnn.setId("actiontarget");
            grid.add(cnn, 0, 4);

            TextField ipTextField = new TextField();
            ip.setId("actiontarget");
            grid.add(ipTextField, 1, 6);
            textFieldArray.add(ipTextField);

            Label port = new Label(Message.PORT_NUMBER.toString());
            port.setId("actiontarget");
            grid.add(port, 0, 5);

            TextField portTextField = new TextField();
            grid.add(portTextField, 1, 5);
            textFieldArray.add(portTextField);
            textFieldArray.add(userTextField);
            grid.setGridLinesVisible(false);
            PasswordField pwBox = new PasswordField();
            grid.add(pwBox, 1, 2);

            btn.setOnAction(e -> {
                actiontarget.setId("actiontarget");

                loginInfo = new LoginInfo("SOCKET", "Player"+number,50004,"127.0.0.1","Password"+number);

                if(box.getValue() == null || !isFilledOut(pwBox) || !isFilledOut(textFieldArray) || !isFilledOut(pwBox))
                    actiontarget.setText(Message.INCOMPLETE_FIELDS.toString());
                else{
                    if (!validNickname(userTextField) || !validPassword(pwBox) ||
                            !validPort(portTextField) || !validIp(ipTextField))
                        actiontarget.setText(Message.INVALID_CHOICE.toString());
                    else{
                        loginInfo = new LoginInfo(
                                box.getValue().toString(),
                                userTextField.getCharacters().toString(),
                                Integer.parseInt(portTextField.getCharacters().toString()),
                                ipTextField.getCharacters().toString(),
                                pwBox.getCharacters().toString());
                        actiontarget.setText(Message.WAITING_OTHER_PLAYERS.toString());
                        btn.setDisable(true);
                    }
                }
            });
            stage.setScene(scene);
            stage.show();
        });

    }

    @Override
    public LoginInfo getConnectionInfo() {
        setScene();
        Thread t = new Thread(() -> {
            while(loginInfo == null) {
                try {
                    Thread.sleep(1);
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
        return loginInfo;
    }

    @Override
    public boolean reConnect() {
        return false;
    }

    /**
     * Checks whether the insert ip is valid.
     * @param ipTextField ip field in connection screen.
     * @return boolean, true if ip insert is valid.
     */
    private boolean validIp(TextField ipTextField){
        String ip = ipTextField.getCharacters().toString();
        List<String> splitIp = Arrays.asList(ip.split("\\."));
        boolean validIp = (splitIp.size()==4);
        if(validIp)
            for(String s: splitIp) {
                try {
                    if( validIp && (Integer.parseInt(s)<0 || Integer.parseInt(s)>255))
                        validIp = false;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        return validIp;
    }

    /**
     * Checks whether the insert nickname is valid.
     * @param userTextField nickname field in connection screen.
     * @return boolean, true if nickname insert is valid.
     */
    private boolean validNickname(TextField userTextField){
        return (!userTextField.getCharacters().toString().contains(" ")&& userTextField.getCharacters().length()<20);
    }


    /**
     * Checks whether the insert port is valid.
     * @param portTextField port field in connection screen.
     * @return boolean, true if port insert is valid.
     */
    private boolean validPort(TextField portTextField){
        String port = portTextField.getCharacters().toString();
        try {
            return  (!port.contains(" ") && Integer.parseInt(port)>1023 && Integer.parseInt(port)<65536);

        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks whether the insert password is valid.
     * @param pf password field in connection screen.
     * @return boolean, true if password insert is valid.
     */
    private boolean validPassword(PasswordField pf){
        return (!pf.getCharacters().toString().contains(" ") && pf.getCharacters().length()<20);
    }

    /**
     * Checks if every TextField is filled out.
     * @param fields List of fields to fill.
     * @return boolean, true if every field is filled.
     */
    private boolean isFilledOut(List<TextField> fields){
        boolean ret = true;
        for(TextField tf: fields)
            if(ret)
                ret = tf.getCharacters().length()>0;
        return ret;
    }

    /**
     * Checks whether password field is filled out.
     * @param pf password field.
     * @return boolean, true if password field is filled out.
     */
    private boolean isFilledOut(PasswordField pf){
        return pf.getCharacters().length()>0;
    }

}
