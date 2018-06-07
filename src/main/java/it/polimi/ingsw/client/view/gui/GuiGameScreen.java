package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.factory.GameScreen;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

public class GuiGameScreen extends GameScreen {
    @Override
    public void addMessage(String message) throws RemoteException {

    }

    @Override
    public void setPlayers(List<String> nicknames) throws RemoteException {

    }

    @Override
    public void setPrivateObjectives(Collection<String> privateObjectives) throws RemoteException {

    }

    @Override
    public void setPublicObjective(Collection<String> publicObjectives) throws RemoteException {

    }

    @Override
    public void setTools(Collection<String> tools) throws RemoteException {

    }

    @Override
    public void setToolUsed(String tool, boolean used) throws RemoteException {

    }

    @Override
    public void setPlayerToken(String nickname, int tokens) throws RemoteException {

    }

    @Override
    public void setPlayerConnection(String nickname, boolean isConnected) throws RemoteException {

    }

    @Override
    public void setPlayerWindow(String nickname, String windowName) throws RemoteException {

    }

    @Override
    public void setCellContent(String nickname, int x, int y, String die) throws RemoteException {

    }

    @Override
    public void setPool(Collection<String> dice) throws RemoteException {

    }

    @Override
    public void setRoundTrack(List<List<String>> dice) throws RemoteException {

    }

    @Override
    public String getWindow(Collection<String> windows) throws RemoteException {
        return null;
    }

    @Override
    public String getInput(Collection<String> options, String container) throws RemoteException {
        return null;
    }

    @Override
    public String getInputFrom(Collection<String> options, String message) throws RemoteException {
        return null;
    }

    @Override
    public void showAll() throws RemoteException {

    }
}
