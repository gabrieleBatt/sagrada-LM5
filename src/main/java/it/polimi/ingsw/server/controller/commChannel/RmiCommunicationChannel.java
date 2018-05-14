package it.polimi.ingsw.server.controller.commChannel;

import it.polimi.ingsw.server.model.table.glassWindow.GlassWindow;
import it.polimi.ingsw.server.rmiInterface.RemoteGameScreen;

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
    public void updateView() {

    }

    @Override
    public GlassWindow chooseWindow(List<GlassWindow> glassWindows) {
        return null;
    }

    @Override
    public String selectOption(List<String> ids, String container, boolean canSkip, boolean undoEnabled) {
        return null;
    }

    @Override
    public String chooseFrom(List<String> options, String message, boolean canSkip, boolean undoEnabled) {
        return null;
    }
}
