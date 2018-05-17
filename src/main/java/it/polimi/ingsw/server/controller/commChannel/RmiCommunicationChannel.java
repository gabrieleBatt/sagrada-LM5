package it.polimi.ingsw.server.controller.commChannel;

import it.polimi.ingsw.server.controller.commChannel.Identifiable;
import it.polimi.ingsw.server.controller.commChannel.CommunicationChannel;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.Pool;
import it.polimi.ingsw.server.model.table.RoundTrack;
import it.polimi.ingsw.server.model.table.Table;
import it.polimi.ingsw.server.model.table.glassWindow.GlassWindow;
import it.polimi.ingsw.net.rmiInterface.RemoteGameScreen;
import javafx.util.Pair;

import java.util.List;

public class RmiCommunicationChannel implements CommunicationChannel {

    private final String nickname;

    public RmiCommunicationChannel(RemoteGameScreen gameScreen, String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public boolean isOffline() {
        return true;
    }

    @Override
    public void sendMessage(String message) {

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
    public Identifiable selectObject(List<Identifiable> options, Identifiable container, boolean canSkip, boolean undoEnabled) {
        return null;
    }

    @Override
    public Identifiable chooseFrom(List<Identifiable> options, String message, boolean canSkip, boolean undoEnabled) {
        return null;
    }

    @Override
    public void setOffline() {
    }
}
