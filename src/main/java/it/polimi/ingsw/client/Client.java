package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.factory.*;
import it.polimi.ingsw.shared.LogMaker;
import it.polimi.ingsw.client.connection.ConnectionManager;
import it.polimi.ingsw.client.connection.RmiManager;
import it.polimi.ingsw.client.connection.SocketManager;
import it.polimi.ingsw.client.view.EndGameInfo;
import it.polimi.ingsw.client.view.LoginInfo;
import javafx.application.Application;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * client main class
 */
public class Client extends Application {

    private static Logger logger = LogMaker.getLogger(Client.class.getName(), Level.ALL);

    private static ViewAbstractFactory view;
    private static ConnectionScreen connectionScreen;
    private static ConnectionManager connectionManager;
    private static Stage stage;


    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
    }

    public static Optional<Stage> getStage() {
        return Optional.ofNullable(stage);
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void main(String[] args) {
        if(args.length == 1 &&
                (args[0].equalsIgnoreCase("-gui") ||
                        args[0].equalsIgnoreCase("-g"))){
            view = new GuiView();
            new Thread(Application::launch).start();
        }else{
            view = new CliView();
        }
        play();
    }

    private static void play(){
        connectionScreen = view.makeConnectionScreen();
        EndScreen endScreen = view.makeEndScreen();
        setConnection();
        do {
            Optional<EndGameInfo> endGameInfo = Optional.empty();
            do{
                try {
                    if (connectionManager.login()) {
                        endGameInfo = connectionManager.run();
                    }else{
                        break;
                    }
                } catch (InterruptedException | NotBoundException | IOException | ParseException | NullPointerException e) {
                    logger.log(Level.WARNING, e.getMessage());
                }
            }while ((!endGameInfo.isPresent() && connectionScreen.reConnect()));
            endGameInfo.ifPresent(endScreen::showRanking);
        }while (endScreen.playAgain());
    }

    private static void setConnection(){
        LoginInfo loginInfo = connectionScreen.getConnectionInfo();
        GameScreen gameScreen = view.makeGameScreen();
        if (loginInfo.connectionType.equalsIgnoreCase("Socket")) {
            connectionManager = new SocketManager(loginInfo, gameScreen);
        } else {
            connectionManager = new RmiManager(loginInfo, gameScreen);
        }

    }
}
