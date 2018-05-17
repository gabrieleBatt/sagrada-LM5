package it.polimi.ingsw.client;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.net.interfaces.RemoteGameScreen;
import it.polimi.ingsw.net.interfaces.RemoteServer;
import javafx.util.Pair;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FakeRmiClient {

    private static final Logger logger = LogMaker.getLogger(FakeRmiClient.class.getName(), Level.ALL);

    public static void main(String[] args){
        try {
            RemoteServer remoteServer = (RemoteServer) LocateRegistry
                    .getRegistry(50000).lookup("Server");
            remoteServer.rmiLogin(new FakeGameScreen() {}, "testR");

        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }
}

class FakeGameScreen extends UnicastRemoteObject implements RemoteGameScreen {
    protected FakeGameScreen() throws RemoteException {
    }

    @Override
    public void setPlayers(List<Pair<String, Boolean>> nicknames) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPrivateObjectives(List<String> privateObjectives) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPublicObjective(List<String> publicObjectives) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTools(List<Pair<String, Boolean>> publicObjectives) {
        throw new UnsupportedOperationException();
    }


    @Override
    public void setToolUsed(String tool, boolean used) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPlayerToken(String nickname, int tokens) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPlayerWindow(String nickname, String windowName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCellContent(String nickname, int x, int y, String die) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCellActive(String nickname, int x, int y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPool(Collection<Pair<String, Boolean>> dice) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRoundTrack(List<Pair<String, Boolean>> dice) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getInput() {
        throw new UnsupportedOperationException();
    }
}
