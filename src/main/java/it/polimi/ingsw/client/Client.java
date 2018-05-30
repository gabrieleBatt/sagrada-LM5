package it.polimi.ingsw.client;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.client.connection.ConnectionManager;
import it.polimi.ingsw.client.connection.RmiManager;
import it.polimi.ingsw.client.connection.SocketManager;
import it.polimi.ingsw.client.view.EndGameInfo;
import it.polimi.ingsw.client.view.LoginInfo;
import it.polimi.ingsw.client.view.factory.*;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.rmi.NotBoundException;
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
            ConnectionManager connectionManager;

            if (loginInfo.connectionType.equalsIgnoreCase("Socket")) {
                connectionManager = new SocketManager(loginInfo, gameScreen);
            } else {
                connectionManager = new RmiManager(loginInfo, gameScreen);
            }
            do{
                try {
                    if (connectionManager.login()) {
                        endGameInfo = connectionManager.run();
                    }else{
                        break;
                    }
                } catch (InterruptedException | NotBoundException | IOException | ParseException | NullPointerException e) {
                    logger.log(Level.WARNING, e.getMessage());
                    if (!connectionScreen.reConnect()) {
                        break;
                    }
                }
            }while ((!endGameInfo.isPresent()));
            endGameInfo.ifPresent(endScreen::showRanking);
        }while (endScreen.playAgain());
    }
}
