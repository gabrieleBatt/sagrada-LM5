package it.polimi.ingsw.client.view.gui;

import com.sun.prism.paint.Color;
import it.polimi.ingsw.client.view.LoginInfo;
import it.polimi.ingsw.client.view.factory.ConnectionScreen;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.shared.Message;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
    private static final int MAX_LENGHT = 12;
    public static double WIDTH;
    public static double HEIGHT;
    private static final double DEFAULT_HEIGHT = 680;
    private static final double DEFAULT_WIDTH  = 1280;
    private static final double SMALL_SPACING;
    private static final double BIG_SPACING;
    private static final double VBOX_SPACING;
    private static final double BUTTON_WIDTH_SIZE;
    private static final double BUTTON_HEIGHT_SIZE;

    static {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        WIDTH = primaryScreenBounds.getWidth();
        HEIGHT = primaryScreenBounds.getHeight();

        if (DEFAULT_WIDTH / DEFAULT_HEIGHT < WIDTH / HEIGHT)
            WIDTH = DEFAULT_WIDTH * HEIGHT / DEFAULT_HEIGHT;
        else
            HEIGHT = DEFAULT_HEIGHT * WIDTH / DEFAULT_WIDTH;
        VBOX_SPACING = 25/DEFAULT_WIDTH*WIDTH;
        BIG_SPACING = 50/DEFAULT_WIDTH*WIDTH;
        SMALL_SPACING = 5/DEFAULT_WIDTH*WIDTH;
        BUTTON_WIDTH_SIZE = 75/DEFAULT_WIDTH*WIDTH;
        BUTTON_HEIGHT_SIZE = 35/DEFAULT_WIDTH*WIDTH;

    }

    private LoginInfo loginInfo = null;
    private GridPane grid;
    private VBox connectionScreenVBox;
    private String RMI = "RMI";
    private String SOCKET = "SOCKET";

    /**
     * Sets the login scene, catches login info.
     */

    public void setScene(){
        Platform.runLater(() -> {
            connectionScreenVBox = new VBox();
            Stage stage = Client.getStage();
            stage.setHeight(HEIGHT);
            stage.setWidth(WIDTH);
            stage.setTitle(Message.START_GAME.toString());
            stage.setX(Screen.getPrimary().getBounds().getMinX());
            stage.setY(Screen.getPrimary().getBounds().getMinY());
            Integer number = ThreadLocalRandom.current().nextInt(1, 10000);

            grid = new GridPane();
            grid.setAlignment(Pos.BOTTOM_CENTER);
            grid.setHgap(BIG_SPACING);
            grid.setVgap(SMALL_SPACING);
            grid.getHgap();
            Button btn = new Button(Message.PLAY.toString());
            btn.setMinSize(BUTTON_WIDTH_SIZE,BUTTON_HEIGHT_SIZE);




            ChoiceBox box = new ChoiceBox();
            box.getItems().addAll(RMI, SOCKET);
            grid.add(box,1,4);
            final Text actiontarget = new Text();
            setAlgerian(actiontarget);
            actiontarget.setFill(YELLOW);

            connectionScreenVBox.getChildren().add(grid);
            connectionScreenVBox.getChildren().add(actiontarget);
            connectionScreenVBox.getChildren().add(btn);
            //invisible button to speed up connection
            Button button = new Button();
            button.setOnAction(event -> {
                loginInfo = new LoginInfo(SOCKET, "Player"+number,50004,"127.0.0.1","Password"+number);
                btn.setDisable(true);
            });
            button.setOpacity(0);
            button.setAlignment(Pos.CENTER);
            button.setMinSize(SMALL_SPACING,SMALL_SPACING);
            connectionScreenVBox.getChildren().add(button);
            connectionScreenVBox.setAlignment(Pos.BOTTOM_CENTER);
            connectionScreenVBox.setSpacing(VBOX_SPACING);

            Scene scene = new Scene(connectionScreenVBox,WIDTH, HEIGHT);
            stage.setScene(scene);
            scene.getStylesheets().add
                    (GuiConnectionScreen.class.getResource("/clientResources/gui/Login.css").toExternalForm());
            List<TextField> textFieldArray = new ArrayList<>();

            Text userName = new Text(Message.CHOOSE_NICKNAME.toString());
            setAlgerian(userName);
            grid.add(userName, 0, 1);

            TextField userTextField = new TextField();
            grid.add(userTextField, 1, 1);
            textFieldArray.add(userTextField);

            Text pw = new Text(Message.INSERT_PASSWORD.toString());
            setAlgerian(pw);
            grid.add(pw, 0, 2);

            Text ip = new Text(Message.IP_NUMBER.toString());
            setAlgerian(ip);
            setAlgerian(ip);
            grid.add(ip, 0, 6);

            Text cnn = new Text(Message.CHOOSE_CONNECTION.toString());
            setAlgerian(cnn);
            grid.add(cnn, 0, 4);

            TextField ipTextField = new TextField();
            grid.add(ipTextField, 1, 6);
            textFieldArray.add(ipTextField);

            Text port = new Text(Message.PORT_NUMBER.toString());
            setAlgerian(port);
            grid.add(port, 0, 5);

            TextField portTextField = new TextField();
            grid.add(portTextField, 1, 5);
            textFieldArray.add(portTextField);
            textFieldArray.add(userTextField);
            grid.setGridLinesVisible(false);
            PasswordField pwBox = new PasswordField();
            grid.add(pwBox, 1, 2);


            btn.setOnAction(e -> {


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
        return (!userTextField.getCharacters().toString().contains(" ")&& userTextField.getCharacters().length()<MAX_LENGHT);
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
        return (!pf.getCharacters().toString().contains(" ") && pf.getCharacters().length()<MAX_LENGHT);
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

    public void setAlgerian(Text algerian) {
        algerian.setFill(WHITE);
        algerian.setFont(Font.font("Algerian",20));
    }
}
