package it.polimi.ingsw.client.view.factory;


import it.polimi.ingsw.client.view.LoginInfo;

/**
 * Handles getting the connection and reconnection info from the user
 */
public abstract class ConnectionScreen {

    /**
     * returns the LoginInfo, in the correct format, from user input
     * @return the LoginInfo, in the correct format, from user input
     */
    public abstract LoginInfo getConnectionInfo();

    /**
     * returns true if the user wants to reconnect
     * @return true if the user wants to reconnect
     */
    public abstract boolean reConnect();
}
