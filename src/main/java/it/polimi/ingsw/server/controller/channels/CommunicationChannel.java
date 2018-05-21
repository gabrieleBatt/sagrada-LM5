package it.polimi.ingsw.server.controller.channels;

import it.polimi.ingsw.net.identifiables.Identifiable;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.Pool;
import it.polimi.ingsw.server.model.table.RoundTrack;
import it.polimi.ingsw.server.model.table.Table;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;
import javafx.util.Pair;

import java.util.List;

/**
 * Class used by the game to communicate with a single client,
 * in case the clients goes offline it keeps being used by the Game as nothing append
 * but it produces a fake response
 */
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
    boolean isOffline();

    /**
     * Sends a message to visualize
     */
    void sendMessage(String message);

    /**
     * Updates any change in the pool.
     * @param pool
     */
    void updateView(Pool pool);

    /**
     * Updates any change in the roundTrack.
     * @param roundTrack
     */
    void updateView(RoundTrack roundTrack);

    /**
     * Updates any change in the cards.
     * @param table
     */
    void updateView(Table table);

    /**
     * Updates any change in the player and their glasswindow.
     * @param connected true if player is still connected
     * @param player player to update
     */
    void updateView(Player player, boolean connected);

    /**
     * tells the client the game has ended and the results
     * @param scores - list of players and their scores
     */
    void endGame(List<Pair<Player, Integer>> scores);

    /**
     * Returns the chosen glasswindow among the given.
     * @param glassWindows List of glassWindows given.
     * @return Object glasswindow, the one chosen.
     */
    GlassWindow chooseWindow(List<GlassWindow> glassWindows);

    /**
     * Returns the chosen option among the given.
     * @param options List of options given.
     * @param container it's the object that contains the options
     * @param canSkip tells if the button canSkip is available for that player in that move
     * @param undoEnabled tells if the button undo is available for that player in that move
     * @return The option chosen.
     */
    Identifiable selectObject(List<Identifiable> options, Identifiable container, boolean canSkip, boolean undoEnabled);


    /**
     * Returns the chosen option among the given.
     * @param options List of options given.
     * @return The option chosen.
     */
    Identifiable chooseFrom(List<Identifiable> options, String message, boolean canSkip, boolean undoEnabled);

    /**
     * Used to set a channel as it went offline
     */
    void setOffline();
}