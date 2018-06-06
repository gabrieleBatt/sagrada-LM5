package it.polimi.ingsw.server.controller.channels;

import it.polimi.ingsw.shared.interfaces.RemoteGameScreen;

import java.util.Collection;
import java.util.List;

/**
 * used for lobby testing
 */
public class TestGameScreen implements RemoteGameScreen {
    @Override
    public void addMessage(String message) {
        return;
    }

    @Override
    public void setPlayers(List<String> nicknames) {
        return;
    }

    @Override
    public void setPrivateObjectives(Collection<String> privateObjectives) {

    }

    @Override
    public void setPublicObjective(Collection<String> publicObjectives) {

    }

    @Override
    public void setTools(Collection<String> tools) {

    }
    @Override
    public void setToolUsed(String tool, boolean used) {
        return;
    }

    @Override
    public void setPlayerToken(String nickname, int tokens) {
        return;
    }

    @Override
    public void setPlayerConnection(String nickname, boolean isConnected) {
        return;
    }


    @Override
    public void setPlayerWindow(String nickname, String windowName) {
        return;
    }

    @Override
    public void setCellContent(String nickname, int x, int y, String Die) {
        return;
    }

    @Override
    public void setPool(Collection<String> dice) {
        return;
    }

    @Override
    public void setRoundTrack(List<List<String>> dice) {
        return;
    }

    @Override
    public String getWindow(Collection<String> o) {
        return "test";
    }

    @Override
    public String getInput(Collection<String> options, String container) {
        return options.iterator().next();
    }

    @Override
    public String getInputFrom(Collection<String> strings, String message) {
        return strings.iterator().next();
    }

    @Override
    public void showAll() {
        return;
    }
}
