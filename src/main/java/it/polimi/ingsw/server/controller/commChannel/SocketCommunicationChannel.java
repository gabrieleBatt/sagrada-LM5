package it.polimi.ingsw.server.controller.commChannel;

import it.polimi.ingsw.server.controller.commChannel.CommunicationChannel;
import it.polimi.ingsw.server.model.table.glassWindow.GlassWindow;

import java.net.Socket;
import java.util.List;

public class SocketCommunicationChannel implements CommunicationChannel {

    public SocketCommunicationChannel(Socket socket, String nickname) {
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
    public String selectOption(List<String> ids) {
        return null;
    }

    @Override
    public String chooseFrom(List<String> options) {
        return null;
    }
}

