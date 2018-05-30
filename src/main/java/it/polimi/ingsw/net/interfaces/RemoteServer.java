package it.polimi.ingsw.net.interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {

    RemoteChannel rmiLogin(RemoteGameScreen gameScreen, String nickname, String password) throws RemoteException;

}
