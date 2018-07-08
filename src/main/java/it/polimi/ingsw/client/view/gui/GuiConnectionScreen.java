package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.LoginInfo;
import it.polimi.ingsw.client.view.factory.ConnectionScreen;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.factory.GuiView;
import it.polimi.ingsw.shared.Message;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static javafx.scene.paint.Color.WHITE;
import static javafx.scene.paint.Color.YELLOW;


public class GuiConnectionScreen extends ConnectionScreen {

    private static final int MIN_PORT = 1023;
    private static final int MAX_PORT = 65536;
    private static final int MAX_LENGTH = 12;
    private static final double SMALL_SPACING;
    private static final double BIG_SPACING;
    private static final double VERTICAL_SPACING;
    private static final double BUTTON_WIDTH_SIZE;
    private static final double BUTTON_HEIGHT_SIZE;
    private static final String RECONNECT = "Reconnect";

    static {
        VERTICAL_SPACING = 25/GuiView.DEFAULT_WIDTH*GuiView.WIDTH;
        BIG_SPACING = 50/GuiView.DEFAULT_WIDTH*GuiView.WIDTH;
        SMALL_SPACING = 5/GuiView.DEFAULT_WIDTH*GuiView.WIDTH;
        BUTTON_WIDTH_SIZE = 75/GuiView.DEFAULT_WIDTH*GuiView.WIDTH;
        BUTTON_HEIGHT_SIZE = 35/GuiView.DEFAULT_WIDTH*GuiView.WIDTH;
    }

    private LoginInfo loginInfo = null;
    private GridPane grid;
    private VBox connectionScreenVBox;
    private String RMI = "RMI";
    private String SOCKET = "SOCKET";
    private boolean isReadyToConnect;
    private Stage stage = Client.getStage();

    /**
     * Sets the login scene, catches login info.
     */

    public void setScene(){
        Platform.runLater(() -> {

            connectionScreenVBox = new VBox();

            stage.setTitle(Message.START_GAME.toString());
            Integer number = ThreadLocalRandom.current().nextInt(1, 10000);

            grid = new GridPane();
            grid.setAlignment(Pos.BOTTOM_CENTER);
            grid.setHgap(BIG_SPACING);
            grid.setVgap(VERTICAL_SPACING);
            grid.getHgap();
            Button btn = new Button();
            if(loginInfo == null)
                btn.setText(Message.PLAY.toString());
            else
                btn.setText(RECONNECT);
            btn.setMinSize(BUTTON_WIDTH_SIZE,BUTTON_HEIGHT_SIZE);




            ChoiceBox<String> box = new ChoiceBox<>();
            box.getItems().addAll(RMI, SOCKET);
            if(loginInfo != null)
                box.setValue(loginInfo.connectionType);
            grid.add(box,1,4);
            final Text actiontarget = new Text("");
            setAlgerian(actiontarget);
            actiontarget.setFill(YELLOW);
            connectionScreenVBox.getChildren().add(grid);
            connectionScreenVBox.getChildren().add(actiontarget);
            connectionScreenVBox.getChildren().add(btn);
            //invisible button to speed up connection
            Button button = new Button();
            button.setOnAction(event -> {
                loginInfo = new LoginInfo(SOCKET, "Player"+number,50003,"127.0.0.1","p"+number);
                isReadyToConnect = true;
                btn.setDisable(true);
            });
            button.setOpacity(0);
            button.setAlignment(Pos.CENTER);
            button.setMinSize(SMALL_SPACING,SMALL_SPACING);
            connectionScreenVBox.getChildren().add(button);
            connectionScreenVBox.setAlignment(Pos.BOTTOM_CENTER);
            connectionScreenVBox.setSpacing(VERTICAL_SPACING);

            Scene scene = new Scene(connectionScreenVBox,GuiView.WIDTH*0.99, GuiView.HEIGHT*0.99);
            stage.setScene(scene);

            scene.getStylesheets().add(GuiConnectionScreen.class.getResource("/clientResources/gui/Login.css").toExternalForm());
            List<TextField> textFieldArray = new ArrayList<>();


            Text userName = new Text(Message.CHOOSE_NICKNAME.toString());
            setAlgerian(userName);
            grid.add(userName, 0, 1);
            TextField userTextField = new TextField();
            grid.add(userTextField, 1, 1);
            if(loginInfo != null)
                userTextField.setText(loginInfo.nickname);
            textFieldArray.add(userTextField);

            Text pw = new Text(Message.INSERT_PASSWORD.toString());
            setAlgerian(pw);
            grid.add(pw, 0, 2);

            Text ip = new Text(Message.IP_NUMBER.toString());
            setAlgerian(ip);
            setAlgerian(ip);
            grid.add(ip, 0, 3);

            Text cnn = new Text(Message.CHOOSE_CONNECTION.toString());
            setAlgerian(cnn);
            grid.add(cnn, 0, 4);

            TextField ipTextField = new TextField();
            if(loginInfo != null)
                ipTextField.setText(loginInfo.ip);
            grid.add(ipTextField, 1, 3);
            textFieldArray.add(ipTextField);

            Text port = new Text(Message.PORT_NUMBER.toString());
            setAlgerian(port);
            grid.add(port, 0, 5);

            TextField portTextField = new TextField();
            if(loginInfo != null)
                portTextField.setText(Integer.toString(loginInfo.portNumber));
            grid.add(portTextField, 1, 5);
            textFieldArray.add(portTextField);
            textFieldArray.add(userTextField);
            grid.setGridLinesVisible(false);
            PasswordField pwBox = new PasswordField();
            grid.add(pwBox, 1, 2);


            btn.setOnAction(e -> {


                if(box.getValue() == null || !isFilledOut(pwBox) || !isFilledOut(textFieldArray))
                    actiontarget.setText(Message.INCOMPLETE_FIELDS.toString());
                else{
                    if (!validNickname(userTextField) || !validPassword(pwBox) ||
                            !validPort(portTextField) || !validIp(ipTextField))
                        actiontarget.setText(Message.INVALID_CHOICE.toString());
                    else{
                        loginInfo = new LoginInfo(
                                box.getValue(),
                                userTextField.getCharacters().toString(),
                                Integer.parseInt(portTextField.getCharacters().toString()),
                                ipTextField.getCharacters().toString(),
                                pwBox.getCharacters().toString());
                        actiontarget.setText(Message.WAITING_OTHER_PLAYERS.toString());
                        btn.setDisable(true);
                        isReadyToConnect = true;
                    }
                }
            });
            stage.show();
        });

    }


    @Override
    public LoginInfo getConnectionInfo() {
        setScene();
        waitInput();
        return loginInfo;
    }

    @Override
    public boolean reConnect() {
        setScene();
        waitInput();
        return isReadyToConnect;
    }

    private void waitInput() {
        isReadyToConnect = false;

        Thread t = new Thread(() -> {
            while (!isReadyToConnect) {
                try {
                    Thread.sleep(4);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        t.start();
        while (true) {
            try {
                t.join();
                break;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

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
        return (!userTextField.getCharacters().toString().contains(" ")&& userTextField.getCharacters().length()<MAX_LENGTH);
    }


    /**
     * Checks whether the insert port is valid.
     * @param portTextField port field in connection screen.
     * @return boolean, true if port insert is valid.
     */
    private boolean validPort(TextField portTextField){
        String port = portTextField.getCharacters().toString();
        try {
            return  (!port.contains(" ") && Integer.parseInt(port)>MIN_PORT && Integer.parseInt(port)<MAX_PORT);

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
        return (!pf.getCharacters().toString().contains(" ") && pf.getCharacters().length()<MAX_LENGTH);
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

    private void setAlgerian(Text algerian) {
        algerian.setFill(WHITE);
        algerian.setFont(Font.font(GuiView.FONT,GuiView.MEDIUM_FONT));
    }
}
