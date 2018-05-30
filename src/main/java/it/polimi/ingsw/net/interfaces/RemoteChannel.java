package it.polimi.ingsw.net.interfaces;

import javafx.util.Pair;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

public interface RemoteChannel extends Remote {

    List<Pair<String, Integer>> getScores() throws RemoteException;

}
