package it.polimi.ingsw.server.controller.commChannel.rmi.rmiInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {

    public void rmiLogin(RemoteGameScreen gameScreen, String nickname) throws RemoteException;

}
