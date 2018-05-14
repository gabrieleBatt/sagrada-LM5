package it.polimi.ingsw.server.controller.commChannel;

import it.polimi.ingsw.server.model.table.glassWindow.GlassWindow;

import java.util.List;

public interface CommunicationChannel{

    /**
     * Creates a communication channel setting the player's nickname.
     * @return String, player's nickname.
     */
    String getNickname();

    /**
     * Returns true if player is connected.
     * @return true if is connected, false otherwise.
     */
    boolean isConnected();

    /**
     * Updates any change occurred in the game.
     */
    void updateView();

    /**
     * Returns the chosen glassWindow among the given.
     * @param glassWindows List of glassWindows given.
     * @return Object glassWindow, the one chosen.
     */
    GlassWindow chooseWindow(List<GlassWindow> glassWindows);


    //TODO
    /**
     * Returns the chosen String among the given.
     * @param Strings List of Strings given.
     * @param container it's an String that contains other String objects
     * @param canSkip tells if the button canSkip is available for that player in that move
     * @param undoEnabled tells if the button undo is available for that player in that move
     * @return The String one chosen.
     */
    String selectOption(List<String> Strings, String container, boolean canSkip, boolean undoEnabled);

    /**
     * Returns the chosen option among the given.
     * @param options List of options given.
     * @return String option, the one chosen.
     */
    String chooseFrom(List<String> options, String message, boolean canSkip, boolean undoEnabled);
}