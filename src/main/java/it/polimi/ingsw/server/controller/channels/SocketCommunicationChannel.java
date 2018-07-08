package it.polimi.ingsw.server.controller.channels;

import it.polimi.ingsw.shared.LogMaker;
import it.polimi.ingsw.shared.identifiables.Identifiable;
import it.polimi.ingsw.shared.identifiables.StdId;
import it.polimi.ingsw.shared.socket.JSONBuilder;
import it.polimi.ingsw.shared.socket.SocketProtocol;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.Pool;
import it.polimi.ingsw.server.model.table.RoundTrack;
import it.polimi.ingsw.server.model.table.Table;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.glasswindow.Cell;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;
import javafx.util.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Communication channel implemented with socket communication.
 */
public final class SocketCommunicationChannel extends CommunicationChannel {

    private static final Logger logger = LogMaker.getLogger(SocketCommunicationChannel.class.getName(), Level.ALL);
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private boolean connected;

    /**
     * Creates a new SocketCommChannel communicating with a single client
     * @param socket client socket
     * @param in input stream
     * @param out output stream
     * @param nickname client's nickname
     */
    public SocketCommunicationChannel(Socket socket, BufferedReader in, PrintWriter out, String nickname){
        super(nickname);
        this.connected = true;
        this.socket = socket;
        this.in = in;
        this.out = out;
    }

    /**
     * Returns true if player is connected.
     * @return true if is connected, false otherwise.
     */
    @Override
    public boolean isOffline() {
        if (!socket.isConnected())
            setOffline();
        return !connected;
    }
    /**
     * Sends a message to visualize
     */
    @Override
    public synchronized void sendMessage(String message) {
        new JSONBuilder()
                .build(SocketProtocol.SEND)
                .build(SocketProtocol.MESSAGE, message)
                .send(out);
    }

    /**
     * Updates any change in the pool.
     */
    @Override
    public synchronized void updateView(Pool pool) {
        List<String> param = new ArrayList<>();
        for (Die die : pool.getDice()) {
            param.add(die.getId());
        }
        new JSONBuilder()
                .build(SocketProtocol.UPDATE)
                .build(SocketProtocol.POOL, param)
                .send(out);
    }

    /**
     * Updates any change in the roundTrack.
     */
    @Override
    public synchronized void updateView(RoundTrack roundTrack) {
        List<String> param = new ArrayList<>();
        for (int i = 1; i < roundTrack.getRound(); i++) {
            for (Die die : roundTrack.getDice(i)) {
                param.add("r"+ i + ":" + die.getId());
            }
        }

        new JSONBuilder()
                .build(SocketProtocol.UPDATE)
                .build(SocketProtocol.ROUND_TRACK, param)
                .send(out);
    }

    /**
     * Updates any change in the public cards(objectives and tools) and the name of the players.
     */
    @Override
    public synchronized void updateView(Table table) {

        JSONBuilder jsonBuilder = new JSONBuilder()
                .build(SocketProtocol.UPDATE);

        if (!table.getPublicObjectives().isEmpty()){
            List<String> param = new ArrayList<>();
            table.getPublicObjectives()
                    .forEach(po -> param.add(po.getId()));
            jsonBuilder.build(SocketProtocol.PUBLIC_OBJ, param);
        }

        if (!table.getTools().isEmpty()){
            List<String> param = new ArrayList<>();
            table.getTools()
                    .forEach(t -> param.add(t.getId()+"-"+t.isUsed()));
            jsonBuilder.build(SocketProtocol.TOOL, param);
        }

        if (!table.getPlayers().isEmpty()){
            List<String> param = new ArrayList<>();
            table.getPlayers()
                    .forEach(p -> param.add(p.getNickname()));
            jsonBuilder.build(SocketProtocol.PLAYER, param);
        }
        jsonBuilder.send(out);
    }

    /**
     * Updates any change in the player and their glassWindow.
     * @param connected true if player is still connected
     * @param player player to update
     */
    @Override
    public synchronized void updateView(Player player, boolean connected) {
        JSONBuilder jsonBuilder = new JSONBuilder()
                .build(SocketProtocol.UPDATE_PLAYER)
                .build(SocketProtocol.PLAYER, player.getNickname())
                .build(SocketProtocol.CONNECTION, Boolean.toString(connected));


        if(this.getNickname().equals(player.getNickname()) && !player.getPrivateObjective().isEmpty()) {
            List<String> param = new ArrayList<>();
            player.getPrivateObjective()
                    .forEach(p -> param.add(p.getName()));
            jsonBuilder.build(SocketProtocol.PRIVATE_OBJ, param);
        }

        if(player.hasGlassWindow()){
            List<String> param = new ArrayList<>();
            param.add(player.getGlassWindow().getId());

            for (Cell cell : player.getGlassWindow().getCellList()) {
                if (cell.isOccupied())
                    param.add(cell.getDie().getId());
                else
                    param.add(" ");
            }
            jsonBuilder.build(SocketProtocol.GLASS_WINDOW, param);

        }

        jsonBuilder.build(SocketProtocol.TOKEN, Integer.toString(player.getTokens()));

        jsonBuilder.send(out);

    }

    /**
     * tells the client the game has ended and the results
     * @param scores - list of players and their scores
     */
    @Override
    public synchronized void endGame(List<Pair<Player, Integer>> scores) {
        List<String> param = new ArrayList<>();

        scores.forEach(p -> param.add(p.getKey().getNickname()+":"+p.getValue()));

        new JSONBuilder()
                .build(SocketProtocol.END_GAME)
                .build(SocketProtocol.LEADER_BOARD, param)
                .send(out);
    }

    /**
     * Returns the chosen glassWindow among the given.
     * @param glassWindows List of glassWindows given.
     * @return Object glassWindow, the one chosen.
     */
    @Override
    public synchronized GlassWindow chooseWindow(List<GlassWindow> glassWindows) {
        Timer timer = new Timer();
        startTimer(timer, this);

        JSONBuilder jsonBuilder = new JSONBuilder()
                .build(SocketProtocol.CHOOSE_WINDOW);
        List<String> param = glassWindows
                .stream()
                .map(GlassWindow::getName)
                .collect(Collectors.toList());
        jsonBuilder.build(SocketProtocol.GLASS_WINDOW, param);
        jsonBuilder.send(out);

        JSONObject response;
        try {
            if((response = (JSONObject) (new JSONParser()).parse(in.readLine())) != null) {
                endTimer(timer);
                if (response.get(SocketProtocol.HEADER.get()).equals(SocketProtocol.CHOOSE_WINDOW.get())) {
                    Optional<GlassWindow> glassWindow = glassWindows.stream()
                            .filter(g -> g.getName().equals(response.get(SocketProtocol.GLASS_WINDOW.get())))
                            .findFirst();
                    if (glassWindow.isPresent()) {
                        return glassWindow.get();
                    } else {
                        endTimer(timer);
                        setOffline();
                    }
                } else {
                    endTimer(timer);
                    setOffline();
                }
            }
        }catch(ParseException | IOException | NullPointerException e){
            logger.log(Level.WARNING, e.getMessage(), e);
            endTimer(timer);
            setOffline();
        }
        return glassWindows.get(ThreadLocalRandom.current().nextInt(0, glassWindows.size()));
    }


    /**
     * Returns the chosen option among the given.
     * @param options List of options given.
     * @param container it's the object that contains the options
     * @param canSkip tells if the button canSkip is available for that player in that move
     * @param undoEnabled tells if the button undo is available for that player in that move
     * @return The option chosen.
     */
    @Override
    public synchronized Identifiable selectObject(List<Identifiable> options, Identifiable container, boolean canSkip, boolean undoEnabled) {
        if(isOffline()) return CommunicationChannel.fakeResponse(canSkip, undoEnabled, options);
        JSONBuilder jsonBuilder = new JSONBuilder()
                .build(SocketProtocol.SELECT_OBJECT)
                .build(SocketProtocol.CONTAINER, container.getId());
        sendMessage(options, canSkip, undoEnabled, jsonBuilder);
        return receiveResponse(options, SocketProtocol.SELECT_OBJECT, canSkip, undoEnabled);
    }

    private void sendMessage(List<Identifiable> options,  boolean canSkip, boolean undoEnabled, JSONBuilder jsonBuilder){
        List<String> opt = options.stream().map(Identifiable::getId).collect(Collectors.toList());
        if (canSkip)
            opt.add(StdId.SKIP.getId());
        if(undoEnabled)
            opt.add(StdId.UNDO.getId());
        jsonBuilder.build(SocketProtocol.OPTION, opt);
        jsonBuilder.send(out);
    }

    private Identifiable receiveResponse(List<Identifiable> options, SocketProtocol type, boolean canSkip, boolean undoEnabled){
        Timer timer = new Timer();
        startTimer(timer, this);
        JSONObject response;
        try {
            if((response = (JSONObject) (new JSONParser()).parse(in.readLine())) != null) {
                endTimer(timer);
                if (response.get(SocketProtocol.HEADER.get()).equals(type.get())) {
                    if (undoEnabled && response.get(SocketProtocol.OPTION.get())
                            .equals(StdId.UNDO.getId())) {
                        return StdId.UNDO;
                    }
                    if (canSkip && response.get(SocketProtocol.OPTION.get())
                            .equals(StdId.SKIP.getId())) {
                         return StdId.SKIP;
                    }
                    Optional<Identifiable> selection = options
                            .stream()
                            .filter(op -> op.getId().equals(response.get(SocketProtocol.OPTION.get())))
                            .findFirst();
                    if (selection.isPresent()) {
                        return selection.get();
                    }
                }
            }
        }catch(ParseException  | IOException |NullPointerException e){
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        setOffline();
        return CommunicationChannel.fakeResponse(canSkip, undoEnabled, options);
    }

    /**
     * Returns the chosen option among the given.
     * @param options List of options given.
     * @return The option chosen.
     */
    @Override
    public synchronized Identifiable chooseFrom(List<Identifiable> options, String message, boolean canSkip, boolean undoEnabled) {
        if(isOffline()) return CommunicationChannel.fakeResponse(canSkip, undoEnabled, options);
        JSONBuilder jsonBuilder = new JSONBuilder()
                .build(SocketProtocol.SELECT_FROM)
                .build(SocketProtocol.MESSAGE, message);
        sendMessage(options, canSkip, undoEnabled, jsonBuilder);
        return receiveResponse(options, SocketProtocol.SELECT_FROM, canSkip, undoEnabled);
    }

    /**
     * Used to set a channel as it went offline
     */
    @Override
    public void setOffline() {
        super.setOffline();
        logger.log(Level.WARNING, getNickname() + " diconnected");
        connected = false;
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }
}


