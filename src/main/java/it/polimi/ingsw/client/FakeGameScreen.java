package it.polimi.ingsw.client;

import it.polimi.ingsw.net.rmiInterface.RemoteGameScreen;
import javafx.util.Pair;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.List;

public class FakeGameScreen extends UnicastRemoteObject implements RemoteGameScreen {
    protected FakeGameScreen() throws RemoteException {
    }

    @Override
    public void setPlayers(List<Pair<String, Boolean>> nicknames) {

    }

    @Override
    public void setPrivateObjectives(List<String> privateObjectives) {

    }

    @Override
    public void setPublicObjective(List<String> publicObjectives) {

    }

    @Override
    public void setTools(List<Pair<String, Boolean>> publicObjectives) {

    }


    @Override
    public void setToolUsed(String tool, boolean used) {

    }

    @Override
    public void setPlayerToken(String nickname, int tokens) {

    }

    @Override
    public void setPlayerWindow(String nickname, String windowName) {

    }

    @Override
    public void setCellContent(String nickname, int x, int y, String Die) {

    }

    @Override
    public void setCellActive(String nickname, int x, int y) {

    }

    @Override
    public void setPool(Collection<Pair<String, Boolean>> dice) {

    }

    @Override
    public void setRoundTrack(List<Pair<String, Boolean>> dice) {

    }

    @Override
    public String getInput() {
        return null;
    }
}
