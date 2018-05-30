package it.polimi.ingsw.net.interfaces;

import it.polimi.ingsw.server.model.table.Player;
import javafx.util.Pair;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

public interface RemoteGameScreen extends Remote {

    void addMessage(String message) throws RemoteException;

    void setPlayers(List<String> nicknames) throws RemoteException;

    void setPrivateObjectives(Collection<String> privateObjectives) throws RemoteException;

    void setPublicObjective(Collection<String> publicObjectives) throws RemoteException;

    void setTools(Collection<String> tools) throws RemoteException;

    void setToolUsed(String tool, boolean used) throws RemoteException;

    void setPlayerToken(String nickname, int tokens) throws RemoteException;

    void setPlayerConnection(String nickname, boolean isConnected) throws RemoteException;

    void setPlayerWindow(String nickname, String windowName) throws RemoteException;

    void setCellContent(String nickname, int x, int y, String die) throws RemoteException;

    void setPool(Collection<String> dice) throws RemoteException;

    void setRoundTrack(List<List<String>> dice)throws RemoteException;

    String getWindow(Collection<String> o)throws RemoteException;

    String getInput(Collection<String> options, String container)throws RemoteException;

    String getInputFrom(Collection<String> strings, String message)throws RemoteException;

    void showAll()throws RemoteException;
}
