package it.polimi.ingsw.shared.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {

    /**
     * Login for a client using rmi connection
     * @param gameScreen the client remote game screen
     * @param nickname the player nickname
     * @param password the player password
     * @return the coupled remote channel
     * @throws RemoteException if export fails
     */
    RemoteChannel rmiLogin(RemoteGameScreen gameScreen, String nickname, String password) throws RemoteException;

}
