package it.polimi.ingsw.client;

import it.polimi.ingsw.server.rmiInterface.RemoteGameScreen;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FakeGameScreen extends UnicastRemoteObject implements RemoteGameScreen {
    protected FakeGameScreen() throws RemoteException {
    }
}
