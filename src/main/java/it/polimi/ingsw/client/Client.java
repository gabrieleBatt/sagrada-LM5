package it.polimi.ingsw.client;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.client.view.LoginInfo;
import it.polimi.ingsw.client.view.factory.CliViewFactory;
import it.polimi.ingsw.client.view.factory.GuiViewFactory;
import it.polimi.ingsw.client.view.factory.ViewAbstractFactory;

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

        LoginInfo loginInfo = factory.makeConnectionScreen().getConnectionInfo();

        if(loginInfo.connectionType.equalsIgnoreCase("Socket")){
            new SocketManager(loginInfo, factory.makeGameScreen()).run();
        }else{
            new RmiManager(loginInfo);
        }
    }

    public static void lostConnection() {
        //reLogin, reConnect
    }
}
