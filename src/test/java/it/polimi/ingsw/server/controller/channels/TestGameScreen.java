package it.polimi.ingsw.server.controller.channels;

import it.polimi.ingsw.net.interfaces.RemoteGameScreen;
import javafx.util.Pair;

import java.util.Collection;
import java.util.List;

/**
 * used for lobby testing
 */
public class TestGameScreen implements RemoteGameScreen {
    @Override
    public void setPlayers(List<Pair<String, Boolean>> nicknames) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPrivateObjectives(List<String> privateObjectives) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPublicObjective(List<String> publicObjectives) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTools(List<Pair<String, Boolean>> publicObjectives) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setToolUsed(String tool, boolean used) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPlayerToken(String nickname, int tokens) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPlayerWindow(String nickname, String windowName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCellContent(String nickname, int x, int y, String Die) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPool(Collection<String> dice) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRoundTrack(List<List<String>> dice) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getWindow(Collection<String> o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getInput(Collection<String> options, String container) {
        return options.iterator().next();
    }

    @Override
    public String getInputFrom(Collection<String> strings, String message) {
        return strings.iterator().next();
    }
}
