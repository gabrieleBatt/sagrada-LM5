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
    private static ViewAbstractFactory factory;

    private Client(){}

    public static Logger getLogger() {
        return logger;
    }

    public static void main(String[] args) {
        if(args.length == 1 &&
                (args[0].equalsIgnoreCase("-gui") ||
                        args[0].equalsIgnoreCase("-g"))){
            factory = new GuiViewFactory();
        }else{
            factory = new CliViewFactory();
        }

        ConnectionScreen connectionScreen = factory.makeConnectionScreen();
        EndScreen endScreen = factory.makeEndScreen();

        do {
            LoginInfo loginInfo = connectionScreen.getConnectionInfo();
            GameScreen gameScreen = factory.makeGameScreen();

            Optional<EndGameInfo> endGameInfo = Optional.empty();
            if (loginInfo.connectionType.equalsIgnoreCase("Socket")) {
                while (!endGameInfo.isPresent()) {
                    try {
                        SocketManager socketManager = new SocketManager(loginInfo, gameScreen);
                        if (socketManager.login()) {
                            endGameInfo = Optional.of(socketManager.run());
                        }
                    } catch (IOException | ParseException | NullPointerException e) {
                        if (!connectionScreen.reConnect()) {
                            break;
                        }
                    }
                }
            } else {
                new RmiManager(loginInfo);
                //TODO
            }
            endGameInfo.ifPresent(endScreen::showRanking);
        }while (endScreen.playAgain());
    }
}
