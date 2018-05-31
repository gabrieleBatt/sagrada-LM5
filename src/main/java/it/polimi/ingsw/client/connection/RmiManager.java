package it.polimi.ingsw.client.connection;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.client.view.EndGameInfo;
import it.polimi.ingsw.client.view.LoginInfo;
import it.polimi.ingsw.net.interfaces.RemoteChannel;
import it.polimi.ingsw.net.interfaces.RemoteGameScreen;
import it.polimi.ingsw.net.interfaces.RemoteServer;
import javafx.util.Pair;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the view by rmi communication
 */
public class RmiManager extends ConnectionManager{

    private static Logger logger = LogMaker.getLogger(RmiManager.class.getName(), Level.ALL);
    private final LoginInfo loginInfo;
    private RemoteGameScreen gameScreen;
    private RemoteChannel remoteChannel;
    private List<Pair<String,Integer>> scores;

    public RmiManager(LoginInfo loginInfo, RemoteGameScreen gameScreen){
        this.loginInfo = loginInfo;
        this.gameScreen = gameScreen;
        scores = new ArrayList<>();
    }

    @Override
    public Optional<EndGameInfo> run() throws InterruptedException {

        Thread thread = new Thread(() -> {
            do{
                try {
                    Thread.sleep(1000);
                    this.scores = remoteChannel.getScores();
                } catch (InterruptedException e) {
                    logger.log(Level.WARNING, e.getMessage());
                } catch (RemoteException e) {
                    logger.log(Level.WARNING, e.getMessage());
                    break;
                }
            }while (scores.isEmpty());
        });

        thread.start();

        thread.join();

        if(scores.isEmpty()){
            return Optional.empty();
        }else{
            return Optional.of(new EndGameInfo(scores));
        }
    }

    @Override
    public boolean login() throws IOException, NotBoundException {
        RemoteServer server = (RemoteServer) LocateRegistry.getRegistry(loginInfo.portNumber).lookup("Server");
        UnicastRemoteObject.exportObject(gameScreen, 0);
        remoteChannel = server.rmiLogin(gameScreen, loginInfo.nickname, loginInfo.password);
        return remoteChannel != null;
    }


}
