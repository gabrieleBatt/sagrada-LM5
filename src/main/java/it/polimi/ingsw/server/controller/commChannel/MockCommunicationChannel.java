package it.polimi.ingsw.server.controller.commChannel;

import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.Pool;
import it.polimi.ingsw.server.model.table.RoundTrack;
import it.polimi.ingsw.server.model.table.Table;
import it.polimi.ingsw.server.model.table.glassWindow.GlassWindow;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class used for testing; returns plausible object in each method.
 * Simulates a client behaviour.
 */
public class MockCommunicationChannel implements CommunicationChannel {
    String nickName;

    /**
     * Creates a communication channel setting the player's nickname.
     * @param nickName String, player's nickname.
     */
    public MockCommunicationChannel(String nickName){
        this.nickName=nickName;
    }

    /**
     * Gets player's nickname.
     * @return String, player's nickname.
     */
    @Override
    public String getNickname() {
        return this.nickName;
    }

    /**
     * Returns true if player is connected.
     * @return always true; the mock player is always connected.
     */
    @Override
    public boolean isConnected() {
        return true;
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


    /**
     * Returns the chosen glassWindow among the given.
     * @param glassWindows List of glassWindows given.
     * @return Object glassWindow, the one randomly chosen.
     */
    @Override
    public GlassWindow chooseWindow(List<GlassWindow> glassWindows) {
        int index = ThreadLocalRandom.current().nextInt(0, glassWindows.size());
        return glassWindows.get(index);
    }

    @Override
    public String selectOption(List<String> ids, Object container, boolean canSkip, boolean undoEnabled) {
        List<String> idList = new ArrayList<>(ids);
        if (undoEnabled)
            idList.add("undo");
        if (canSkip)
            idList.add("skip");
        return idList.get(ThreadLocalRandom.current().nextInt(0, idList.size()));
    }

    /**
     * Returns the chosen option among the given.
     * @param options List of options given.
     * @return String option, the one randomly chosen.
     */
    @Override
    public String chooseFrom(List<String> options, String message, boolean canSkip, boolean undoEnabled) {
        List<String> op = new ArrayList<>(options);
        if (undoEnabled)
            op.add("undo");
        if (canSkip)
            op.add("skip");
        return op.get(ThreadLocalRandom.current().nextInt(0, op.size()));
    }
}
