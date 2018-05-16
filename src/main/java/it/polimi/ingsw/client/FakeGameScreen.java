package it.polimi.ingsw.client;

import it.polimi.ingsw.server.rmiInterface.RemoteGameScreen;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class FakeGameScreen extends UnicastRemoteObject implements RemoteGameScreen {
    protected FakeGameScreen() throws RemoteException {
    }

    @Override
    public void setPlayers(List<String> nicknames) {

    }

    @Override
    public void setPrivateObjectives(List<String> privateObjectives) {

    }

    @Override
    public void setPublicObjective() {

    }
}
