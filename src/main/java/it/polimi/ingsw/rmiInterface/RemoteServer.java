package it.polimi.ingsw.rmiInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {

    public void rmiLogin(RemoteGameScreen gameScreen, String nickname) throws RemoteException;

}
