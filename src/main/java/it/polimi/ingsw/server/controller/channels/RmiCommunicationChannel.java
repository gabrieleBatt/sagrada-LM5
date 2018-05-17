package it.polimi.ingsw.server.controller.channels;

import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.Pool;
import it.polimi.ingsw.server.model.table.RoundTrack;
import it.polimi.ingsw.server.model.table.Table;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;
import it.polimi.ingsw.net.interfaces.RemoteGameScreen;
import javafx.util.Pair;

import java.rmi.Remote;
import java.util.List;

public class RmiCommunicationChannel implements CommunicationChannel {

    private final String nickname;
    private final RemoteGameScreen gameScreen;

    public RmiCommunicationChannel(RemoteGameScreen gameScreen, String nickname) {
        this.nickname = nickname;
        this.gameScreen = gameScreen;
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
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateView(Pool pool) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateView(RoundTrack roundTrack) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateView(Table table) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateView(Player player) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void endGame(List<Pair<Player, Integer>> scores) {
        throw new UnsupportedOperationException();
    }

    @Override
    public GlassWindow chooseWindow(List<GlassWindow> glassWindows) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Identifiable selectObject(List<Identifiable> options, Identifiable container, boolean canSkip, boolean undoEnabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Identifiable chooseFrom(List<Identifiable> options, String message, boolean canSkip, boolean undoEnabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOffline() {
        throw new UnsupportedOperationException();
    }
}
