package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.table.glassWindow.GlassWindow;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class used for testing; returns plausible object in each method.
 * Simulates a client behaviour.
 */
public class MockCommunicationChannel extends CommunicationChannel{
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

    /**
     * Updates any change occurred in the game.
     */
    @Override
    public void updateView() {
        //TODO
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

    /**
     * Returns the chosen ids among the given.
     * @param ids List of ids given.
     * @return String ids, the one randomly chosen.
     */
    @Override
    public String selectOption(List<String> ids) {
        int index = ThreadLocalRandom.current().nextInt(0, ids.size());
        return ids.get(index);
    }

    /**
     * Returns the chosen option among the given.
     * @param options List of options given.
     * @return String option, the one randomly chosen.
     */
    @Override
    public String chooseFrom(List<String> options) {
        int index = ThreadLocalRandom.current().nextInt(0, options.size());
        return options.get(index);
    }
}
