package it.polimi.ingsw.client;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.client.view.EndGameInfo;
import it.polimi.ingsw.client.view.LoginInfo;
import it.polimi.ingsw.client.view.factory.*;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.Connection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    private static Logger logger = LogMaker.getLogger(Client.class.getName(), Level.ALL);
    private static Client client = new Client();
    private static ViewAbstractFactory factory;

    private Client(){}

    public static void main(String[] args) {
        if(args.length == 1 &&
                (args[0].equalsIgnoreCase("-gui") ||
                        args[0].equalsIgnoreCase("-g"))){
            factory = new GuiViewFactory();
        }else{
            factory = new CliViewFactory();
        }

        ConnectionScreen connectionScreen = factory.makeConnectionScreen();
        GameScreen gameScreen = factory.makeGameScreen();
        EndScreen endScreen = factory.makeEndScreen();

        LoginInfo loginInfo = connectionScreen.getConnectionInfo();

        Optional<EndGameInfo> endGameInfo = Optional.empty();
        while (!endGameInfo.isPresent()) {
            if (loginInfo.connectionType.equalsIgnoreCase("Socket")) {
                try {
                    SocketManager socketManager = new SocketManager(loginInfo, gameScreen);
                    if (socketManager.login()) {
                        endGameInfo = Optional.of(socketManager.run());
                    }
                } catch (IOException | ParseException e) {
                    //TODO
                    //reconnection
                }
            }else {
                new RmiManager(loginInfo);
            }
        }
        endScreen.showRanking(endGameInfo.get());
    }

    public static void lostConnection() {
        //reLogin, reConnect
    }
}
