package it.polimi.ingsw.server.controller.commChannel.rmi.rmiInterface;

import javafx.util.Pair;

import java.rmi.Remote;
import java.util.Collection;
import java.util.List;

public interface RemoteGameScreen extends Remote {

    void setPlayers(List<Pair<String, Boolean>> nicknames);

    void setPrivateObjectives(List<String> privateObjectives);

    void setPublicObjective(List<String> publicObjectives);

    void setTools(List<Pair<String, Boolean>> publicObjectives);

    void setToolUsed(String tool, boolean used);

    void setPlayerToken(String nickname, int tokens);

    void setPlayerWindow(String nickname, String windowName);

    void setCellContent(String nickname, int x, int y, String Die);

    void setCellActive(String nickname, int x, int y);

    void setPool(Collection<Pair<String, Boolean>> dice);

    void setRoundTrack(List<Pair<String, Boolean>> dice);

    String getInput();

}
