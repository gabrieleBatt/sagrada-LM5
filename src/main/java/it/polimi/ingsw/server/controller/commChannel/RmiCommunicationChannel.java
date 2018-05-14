package it.polimi.ingsw.server.controller.commChannel;

import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.Pool;
import it.polimi.ingsw.server.model.table.RoundTrack;
import it.polimi.ingsw.server.model.table.Table;
import it.polimi.ingsw.server.model.table.glassWindow.GlassWindow;
import it.polimi.ingsw.server.rmiInterface.RemoteGameScreen;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public class RmiCommunicationChannel implements CommunicationChannel {

    public RmiCommunicationChannel(RemoteGameScreen gameScreen, String nickname) {
    }

    @Override
    public String getNickname() {
        return null;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void updateView(Pool pool) {

    }

    @Override
    public void updateView(RoundTrack roundTrack) {

    }

    @Override
    public void updateView(Table table) {

    }

    @Override
    public void updateView(Player player) {

    }

    @Override
    public void endGame(List<Pair<Player, Integer>> scores) {

    }

    @Override
    public GlassWindow chooseWindow(List<GlassWindow> glassWindows) {
        return null;
    }

    @Override
    public String selectOption(List<String> ids, Object container, boolean canSkip, boolean undoEnabled) {
        return null;
    }

    @Override
    public String chooseFrom(List<String> options, String message, boolean canSkip, boolean undoEnabled) {
        return null;
    }
}
