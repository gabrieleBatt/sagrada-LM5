package it.polimi.ingsw.server.controller.channels;

import it.polimi.ingsw.net.identifiables.Identifiable;
import it.polimi.ingsw.net.identifiables.StdId;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.Pool;
import it.polimi.ingsw.server.model.table.RoundTrack;
import it.polimi.ingsw.server.model.table.Table;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;
import javafx.util.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

/**
 * Class used by the game to communicate with a single client,
 * in case the clients goes offline it keeps being used by the Game as nothing append
 * but it produces a fake response
 */
public abstract class CommunicationChannel{

    private static long responseTime;

    static {
        JSONObject config = null;
        try {
            config = (JSONObject)new JSONParser().parse(new FileReader(new File("resources/ServerResources/config.json")));
            responseTime = (long) config.get("turnTime");
        } catch (ParseException | IOException e) {
            responseTime = 60;
        }
    }

    /**
     * To prevent any exploit the communication channel first tries to skip any action
     * the client should do, if it can't tries to undo the turn and then skip, else
     * it chooses randomly.
     * @param canSkip if the action choice can be skipped
     * @param undoEnabled if its still possible to undo the turn
     * @param op list of options
     * @return the identifiable ch osen
     */
    static Identifiable fakeResponse(boolean canSkip, boolean undoEnabled, List<Identifiable> op){
        if(canSkip)
            return StdId.SKIP;
        else if(undoEnabled)
            return StdId.UNDO;
        else
            return op.get(ThreadLocalRandom.current().nextInt(0, op.size()));
    }

    static void startTimer(Timer timer, CommunicationChannel channel) {
        Game.getLogger().log(Level.FINER, "Starting response timer");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Game.getLogger().log(Level.FINER, "Time's up, client is not responding" );
                channel.setOffline();
                endTimer(timer);
            }
        }, responseTime * 1000);
    }

    static void endTimer(Timer timer){
        timer.cancel();
    }

    /**
     * Creates a communication channel setting the player's nickname.
     * @return String, player's nickname.
     */
    public abstract String getNickname();

    /**
     * Returns true if player is connected.
     * @return true if is connected, false otherwise.
     */
    public abstract boolean isOffline();

    /**
     * Sends a message to visualize
     */
    public abstract void sendMessage(String message);

    /**
     * Updates any change in the pool.
     * @param pool
     */
    public abstract void updateView(Pool pool);

    /**
     * Updates any change in the roundTrack.
     * @param roundTrack
     */
    public abstract void updateView(RoundTrack roundTrack);

    /**
     * Updates any change in the public cards(objectives and tools) and the name of the players.
     * @param table
     */
    public abstract void updateView(Table table);

    /**
     * Updates any change in the player and their glassWindow.
     * @param connected true if player is still connected
     * @param player player to update
     */
    public abstract void updateView(Player player, boolean connected);

    /**
     * tells the client the game has ended and the results
     * @param scores - list of players and their scores
     */
    public abstract void endGame(List<Pair<Player, Integer>> scores);

    /**
     * Returns the chosen glasswindow among the given.
     * @param glassWindows List of glassWindows given.
     * @return Object glasswindow, the one chosen.
     */
    public abstract GlassWindow chooseWindow(List<GlassWindow> glassWindows);

    /**
     * Returns the chosen option among the given.
     * @param options List of options given.
     * @param container it's the object that contains the options
     * @param canSkip tells if the button canSkip is available for that player in that move
     * @param undoEnabled tells if the button undo is available for that player in that move
     * @return The option chosen.
     */
    public abstract Identifiable selectObject(List<Identifiable> options, Identifiable container, boolean canSkip, boolean undoEnabled);


    /**
     * Returns the chosen option among the given.
     * @param options List of options given.
     * @return The option chosen.
     */
    public abstract Identifiable chooseFrom(List<Identifiable> options, String message, boolean canSkip, boolean undoEnabled);

    /**
     * Used to set a channel as it went offline
     */
    public abstract void setOffline();
}