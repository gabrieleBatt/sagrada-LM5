package it.polimi.ingsw.server.controller.channels;

import it.polimi.ingsw.shared.identifiables.Identifiable;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.Pool;
import it.polimi.ingsw.server.model.table.RoundTrack;
import it.polimi.ingsw.server.model.table.Table;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class used for testing; returns plausible object in each method.
 * Simulates a client behaviour.
 */
public class MockCommunicationChannel extends CommunicationChannel {

    private boolean connected=true;

    /**
     * Creates a communication channel setting the player's nickname.
     * @param nickName String, player's nickname.
     */
    public MockCommunicationChannel(String nickName){
        super(nickName);
    }

    /**
     * Returns true if player is connected.
     * @return always true; the mock player is always connected.
     */
    @Override
    public boolean isOffline() {
        return !connected;
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
    public void updateView(Player player, boolean connected) {

    }

    @Override
    public void endGame(List<Pair<Player, Integer>> scores) {

    }


    /**
     * Returns the chosen glasswindow among the given.
     * @param glassWindows List of glassWindows given.
     * @return Object glasswindow, the one randomly chosen.
     */
    @Override
    public GlassWindow chooseWindow(List<GlassWindow> glassWindows) {
        int index = ThreadLocalRandom.current().nextInt(0, glassWindows.size());
        return glassWindows.get(index);
    }

    @Override
    public Identifiable selectObject(List<Identifiable> options, Identifiable container, boolean canSkip, boolean undoEnabled) {
        List<Identifiable> idList = new ArrayList<>(options);
        if(idList.size() == 0)
           return CommunicationChannel.fakeResponse(canSkip, undoEnabled, options);
        return idList.get(ThreadLocalRandom.current().nextInt(0, idList.size()));
    }

    /**
     * Returns the chosen option among the given.
     * @param options List of options given.
     * @return String option, the one randomly chosen.
     */
    @Override
    public Identifiable chooseFrom(List<Identifiable> options, String message, boolean canSkip, boolean undoEnabled) {
        List<Identifiable> op = new ArrayList<>(options);
        if(op.size() == 0)
            return CommunicationChannel.fakeResponse(canSkip, undoEnabled, options);
        return op.get(ThreadLocalRandom.current().nextInt(0, op.size()));
    }

    @Override
    public void setOffline() {
        this.connected = false;
    }
}
