package it.polimi.ingsw.client.view.factory;

import it.polimi.ingsw.net.interfaces.RemoteGameScreen;
import javafx.util.Pair;
import java.util.Collection;
import java.util.List;

public interface GameScreen extends RemoteGameScreen {

    void setPlayers(List<Pair<String, Boolean>> nicknames);

    void setPrivateObjectives(Collection<String> privateObjectives);

    void setPublicObjective(Collection<String> publicObjectives);

    void setTools(Collection<Pair <String, Boolean>> tools);

    void setToolUsed(String tool, boolean used);

    void setPlayerToken(String nickname, int tokens);

    void setPlayerWindow(String nickname, String windowName);

    void setCellContent(String nickname, int x, int y, String die);

    void setPool(Collection<String> dice);

    void setRoundTrack(List<List<String>> dice);

    String getWindow(Collection<String> o);

    String getInput(Collection<String> options, String container);

    String getInputFrom(Collection<String> strings, String message);

    void showAll();
}
