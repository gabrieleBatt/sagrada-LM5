package it.polimi.ingsw.net.interfaces;

import it.polimi.ingsw.server.model.table.Player;
import javafx.util.Pair;

import java.rmi.Remote;
import java.util.Collection;
import java.util.List;

public interface RemoteGameScreen extends Remote {

    void addMessage(String message, boolean toKeep);

    void setPlayers(List<String> nicknames);

    void setPrivateObjectives(Collection<String> privateObjectives);

    void setPublicObjective(Collection<String> publicObjectives);

    void setTools(Collection<String> tools);

    void setToolUsed(String tool, boolean used);

    void setPlayerToken(String nickname, int tokens);

    void setPlayerConnection(String nickname, boolean isConnected);

    void setPlayerWindow(String nickname, String windowName);

    void setCellContent(String nickname, int x, int y, String die);

    void setPool(Collection<String> dice);

    void setRoundTrack(List<List<String>> dice);

    String getWindow(Collection<String> o);

    String getInput(Collection<String> options, String container);

    String getInputFrom(Collection<String> strings, String message);

    void endGame(List<Pair<Player, Integer>> scores);

    void showAll();
}
