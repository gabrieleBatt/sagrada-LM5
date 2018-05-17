package it.polimi.ingsw.client.view.factory;

import javafx.util.Pair;
import java.util.Collection;
import java.util.List;

public interface GameScreen {

    void setPlayers(List<Pair<String, Boolean>> nicknames);

    void setPrivateObjectives(List<String> privateObjectives);

    void setPublicObjective(List<String> publicObjectives);

    void setTools(List<Pair <String, Boolean>> tools);

    void setToolUsed(String tool, boolean used);

    void setPlayerToken(String nickname, int tokens);

    void setPlayerWindow(String nickname, String windowName);

    void setCellContent(String nickname, int x, int y, String die);

    void setCellActive(String nickname, int x, int y);

    void setPool(Collection<Pair<String, Boolean>> dice);

    void setRoundTrack(List<Pair<String, Boolean>> dice);


}
