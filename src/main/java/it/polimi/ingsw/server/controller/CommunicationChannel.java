package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.table.glassWindow.GlassWindow;

import java.util.List;

public abstract class CommunicationChannel{

    /**
     * Creates a communication channel setting the player's nickname.
     * @return String, player's nickname.
     */
    public abstract String getNickname();

    /**
     * Returns true if player is connected.
     * @return true if is connected, false otherwise.
     */
    public abstract boolean isConnected();

    /**
     * Updates any change occurred in the game.
     */
    public abstract void updateView();

    /**
     * Returns the chosen glassWindow among the given.
     * @param glassWindows List of glassWindows given.
     * @return Object glassWindow, the one chosen.
     */
    public abstract GlassWindow chooseWindow(List<GlassWindow> glassWindows);

    /**
     * Returns the chosen ids among the given.
     * @param ids List of ids given.
     * @return String ids, the one chosen.
     */
    public abstract String selectOption(List<String> ids);

    /**
     * Returns the chosen option among the given.
     * @param options List of options given.
     * @return String option, the one chosen.
     */
    public abstract String chooseFrom(List<String> options);
}