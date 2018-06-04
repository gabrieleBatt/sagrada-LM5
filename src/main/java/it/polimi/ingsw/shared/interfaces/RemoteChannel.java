package it.polimi.ingsw.shared.interfaces;

import javafx.util.Pair;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteChannel extends Remote {

    /**
     * Return the scores
     * @return the scores
     */
    List<Pair<String, Integer>> getScores() throws RemoteException;

}
