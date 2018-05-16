package it.polimi.ingsw.server.rmiInterface;

import java.rmi.Remote;
import java.util.List;

public interface RemoteGameScreen extends Remote {

    void setPlayers(List<String> nicknames);

    void setPrivateObjectives(List<String> privateObjectives);

    void setPublicObjective();

}
