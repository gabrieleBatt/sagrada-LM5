package it.polimi.ingsw.shared.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

/**
 * Handles getting from the user all server requested inputs
 */
public interface RemoteGameScreen extends Remote {

    /**
     * Adds a message to show on screen
     * @param message to add
     * @throws RemoteException in case the connection is down
     */
    void addMessage(String message) throws RemoteException;

    /**
     * Sets the players names in play
     * @param nicknames nicknames of players
     * @throws RemoteException in case the connection is down
     */
    void setPlayers(List<String> nicknames) throws RemoteException;

    /**
     * Sets the user private objective
     * @param privateObjectives of the user
     * @throws RemoteException in case the connection is down
     */
    void setPrivateObjectives(Collection<String> privateObjectives) throws RemoteException;

    /**
     * Sets the public objectives in game
     * @param publicObjectives in game
     * @throws RemoteException in case the connection is down
     */
    void setPublicObjective(Collection<String> publicObjectives) throws RemoteException;

    /**
     * Sets the tools in games
     * @param tools in game
     * @throws RemoteException in case the connection is down
     */
    void setTools(Collection<String> tools) throws RemoteException;

    /**
     * Sets a tool as used or unused
     * @param tool to set
     * @param used true if used
     * @throws RemoteException in case the connection is down
     */
    void setToolUsed(String tool, boolean used) throws RemoteException;

    /**
     * Sets the remaining token of a player
     * @param nickname player to update
     * @param tokens remaining
     * @throws RemoteException in case the connection is down
     */
    void setPlayerToken(String nickname, int tokens) throws RemoteException;

    /**
     * Sets the connection status of a player
     * @param nickname nickname of the player to update
     * @param isConnected true if player is still online
     * @throws RemoteException in case the connection is down
     */
    void setPlayerConnection(String nickname, boolean isConnected) throws RemoteException;

    /**
     * Sets the window of a player
     * @param nickname of the player
     * @param windowName name of the window to set
     * @throws RemoteException in case the connection is down
     */
    void setPlayerWindow(String nickname, String windowName) throws RemoteException;

    /**
     * Sets the content of a player window
     * @param nickname of the window owner
     * @param x row
     * @param y column
     * @param die id of die to place
     * @throws RemoteException in case the connection is down
     */
    void setCellContent(String nickname, int x, int y, String die) throws RemoteException;

    /**
     * Sets the Pool content
     * @param dice Pool content
     * @throws RemoteException in case the connection is down
     */
    void setPool(Collection<String> dice) throws RemoteException;

    /**
     * Sets the round track content
     * @param dice round track content
     * @throws RemoteException in case the connection is down
     */
    void setRoundTrack(List<List<String>> dice)throws RemoteException;

    /**
     * Ask the user to choose one window form the ones proposed
     * @param windows option to choose from
     * @return the window chosen
     * @throws RemoteException in case the connection is down
     */
    String getWindow(Collection<String> windows)throws RemoteException;

    /**
     * Ask the user to choose one element of the board
     * @param options elements from which the user must choose
     * @param container in which the elements are
     * @return the option chosen
     * @throws RemoteException in case the connection is down
     */
    String getInput(Collection<String> options, String container)throws RemoteException;

    /**
     * Ask the user to choose one option
     * @param options from which the user must choose
     * @param message to show to the user
     * @return the option chosen
     * @throws RemoteException in case the connection is down
     */
    String getInputFrom(Collection<String> options, String message)throws RemoteException;

    /**
     * Shows to the user the current state of the game
     * @throws RemoteException in case the connection is down
     */
    void showAll()throws RemoteException;
}
