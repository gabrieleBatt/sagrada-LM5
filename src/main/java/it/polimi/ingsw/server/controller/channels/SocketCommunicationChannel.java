package it.polimi.ingsw.server.controller.channels;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.net.socket.JSONBuilder;
import it.polimi.ingsw.net.socket.SocketProtocol;
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


public class SocketCommunicationChannel implements CommunicationChannel {

    private static final Logger logger = LogMaker.getLogger(SocketCommunicationChannel.class.getName(), Level.ALL);
    private static long responseTime;
    private final Socket socket;
    private final String nickname;
    private final BufferedReader in;
    private final PrintWriter out;
    private boolean connected;

    static {
        JSONObject config;
        try {
            config = (JSONObject)new JSONParser().parse(new FileReader(new File("resources/ServerResources/config.json")));
            responseTime = (long)config.get("turnTime");
        } catch (ParseException | IOException e) {
            responseTime = 60;
        }
    }

    public SocketCommunicationChannel(Socket socket, BufferedReader in, PrintWriter out, String nickname){
        this.connected = true;
        this.socket = socket;
        this.nickname = nickname;
        this.in = in;
        this.out = out;
        sendJSON((new JSONBuilder()).build(SocketProtocol.LOGIN)
                .build(SocketProtocol.RESULT, "success"));
        logger.log(Level.FINE, "logged!", nickname);
        out.flush();
    }

    private void startTimer(Timer timer) {
        Game.getLogger().log(Level.FINER, "Starting response timer");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Game.getLogger().log(Level.FINER, "Time's up, client is not responding" );
                disconnect();
                endTimer(timer);
            }
        }, responseTime * 1000);
    }

    private void endTimer(Timer timer){
        timer.cancel();
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public boolean isOffline() {
        return !connected;
    }

    @Override
    public void sendMessage(String message) {
        sendJSON(new JSONBuilder().build(SocketProtocol.SEND, message));
    }

    private void sendJSON(JSONBuilder jsonBuilder){
        out.println(jsonBuilder.get().toString());
        logger.log(Level.FINE, "SentJson", jsonBuilder.get());
        out.flush();
    }

    @Override
    public void updateView(Pool pool) {
        List<String> param = new ArrayList<>();
        for (Die die : pool.getDice()) {
            param.add(die.getId());
        }
        sendJSON(new JSONBuilder()
                .build(SocketProtocol.UPDATE)
                .build(SocketProtocol.POOL, param));
    }

    @Override
    public void updateView(RoundTrack roundTrack) {
        List<String> param = new ArrayList<>();
        for (int i = 1; i < roundTrack.getRound(); i++) {
            for (Die die : roundTrack.getDice(i)) {
                param.add("r"+ i + ":" + die.getId());
            }
        }

        sendJSON(new JSONBuilder()
                .build(SocketProtocol.UPDATE)
                .build(SocketProtocol.ROUND_TRACK, param));
    }

    @Override
    public void updateView(Table table) {

        JSONBuilder jsonBuilder = new JSONBuilder()
                .build(SocketProtocol.UPDATE);

        if (!table.getPublicObjectives().isEmpty()){
            List<String> param = new ArrayList<>();
            table.getPublicObjectives()
                    .forEach(po -> param.add(po.getName()));
            jsonBuilder.build(SocketProtocol.PUBLIC_OBJ, param);
        }

        if (!table.getTools().isEmpty()){
            List<String> param = new ArrayList<>();
            table.getTools()
                    .forEach(t -> param.add(t.getName()+"-"+t.isUsed()));
            jsonBuilder.build(SocketProtocol.TOOL, param);
        }

        if (!table.getPlayers().isEmpty()){
            List<String> param = new ArrayList<>();
            table.getPlayers()
                    .forEach(p -> param.add(p.getNickname()));
            jsonBuilder.build(SocketProtocol.PLAYER, param);
        }
        sendJSON(jsonBuilder);
    }

    @Override
    public void updateView(Player player) {
        JSONBuilder jsonBuilder = new JSONBuilder()
                .build(SocketProtocol.UPDATE_PLAYER)
                .build(SocketProtocol.PLAYER, player.getNickname());


        if(this.getNickname().equals(player.getNickname()) && !player.getPrivateObjective().isEmpty()) {
            List<String> param = new ArrayList<>();
            player.getPrivateObjective()
                    .forEach(p -> param.add(p.getName()));
            jsonBuilder.build(SocketProtocol.PRIVATE_OBJ, param);
        }

        if(player.hasGlassWindow()){
            List<String> param = new ArrayList<>();
            param.add(player.getGlassWindow().getName());

            for (Cell cell : player.getGlassWindow().getCellList()) {
                if (cell.isOccupied())
                    param.add(cell.getDie().getId());
                else
                    param.add("empty ");
            }
            jsonBuilder.build(SocketProtocol.GLASS_WINDOW, param);

        }

        jsonBuilder.build(SocketProtocol.TOKEN, Integer.toString(player.getTokens()));

        sendJSON(jsonBuilder);

    }

    @Override
    public void endGame(List<Pair<Player, Integer>> scores) {
        List<String> param = new ArrayList<>();

        scores.forEach(p -> param.add(p.getKey().getNickname()+":"+p.getValue()));

        sendJSON(new JSONBuilder()
                    .build(SocketProtocol.END_GAME)
                    .build(SocketProtocol.LEADER_BOARD, param));
    }

    @Override
    public GlassWindow chooseWindow(List<GlassWindow> glassWindows) {
        Timer timer = new Timer();
        startTimer(timer);

        JSONBuilder jsonBuilder = new JSONBuilder()
                .build(SocketProtocol.CHOOSE_WINDOW);
        List<String> param = glassWindows
                .stream()
                .map(GlassWindow::getName)
                .collect(Collectors.toList());
        jsonBuilder.build(SocketProtocol.GLASS_WINDOW, param);
        sendJSON(jsonBuilder);

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
                        disconnect();
                    }
                } else {
                    disconnect();
                }
            }
        }catch(ParseException | IOException e){
            logger.log(Level.WARNING, e.getMessage(), e);
            disconnect();
        }
        return glassWindows.get(ThreadLocalRandom.current().nextInt(0, glassWindows.size()));
    }

    private void disconnect(){
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        connected = false;
    }

    @Override
    public Identifiable selectObject(List<Identifiable> options, Identifiable container, boolean canSkip, boolean undoEnabled) {
        if(isOffline()) return fakeResponse(canSkip, undoEnabled, options);
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
        sendJSON(jsonBuilder);
    }

    private Identifiable receiveResponse(List<Identifiable> options, SocketProtocol type, boolean canSkip, boolean undoEnabled){
        Timer timer = new Timer();
        startTimer(timer);
        JSONObject response;
        try {
            if((response = (JSONObject) (new JSONParser()).parse(in.readLine())) != null) {
                endTimer(timer);
                if (response.get(SocketProtocol.HEADER.get()).equals(type.get())) {
                    if (response.get(SocketProtocol.OPTION.get())
                            .equals(StdId.UNDO.getId())) {
                        return StdId.UNDO;
                    }
                    if (response.get(SocketProtocol.OPTION.get())
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
        }catch(ParseException  | IOException e){
            logger.log(Level.WARNING, e.getMessage(), e);
            disconnect();
        }
        disconnect();
        return fakeResponse(canSkip, undoEnabled, options);
    }

    @Override
    public Identifiable chooseFrom(List<Identifiable> options, String message, boolean canSkip, boolean undoEnabled) {
        if(isOffline()) return fakeResponse(canSkip, undoEnabled, options);
        JSONBuilder jsonBuilder = new JSONBuilder()
                .build(SocketProtocol.SELECT_FROM)
                .build(SocketProtocol.MESSAGE, message);
        sendMessage(options, canSkip, undoEnabled, jsonBuilder);
        return receiveResponse(options, SocketProtocol.SELECT_FROM, canSkip, undoEnabled);
    }

    @Override
    public void setOffline() {
        disconnect();
    }

    private Identifiable fakeResponse(boolean canSkip, boolean undoEnabled, List<Identifiable> op){
        if(canSkip)
            return StdId.SKIP;
        else if(undoEnabled)
            return StdId.UNDO;
        else
            return op.get(ThreadLocalRandom.current().nextInt(0, op.size()));
    }
}

