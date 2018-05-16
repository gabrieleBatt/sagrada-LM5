package it.polimi.ingsw.server.controller.commChannel.socket;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.controller.Identifiable;
import it.polimi.ingsw.server.controller.StdId;
import it.polimi.ingsw.server.controller.commChannel.CommunicationChannel;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.Pool;
import it.polimi.ingsw.server.model.table.RoundTrack;
import it.polimi.ingsw.server.model.table.Table;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.glassWindow.Cell;
import it.polimi.ingsw.server.model.table.glassWindow.GlassWindow;
import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SocketCommunicationChannel implements CommunicationChannel {

    private static final Logger logger = LogMaker.getLogger(SocketCommunicationChannel.class.getName(), Level.ALL);
    private final Socket socket;
    private final String nickname;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private boolean connected;

    public SocketCommunicationChannel(Socket socket, ObjectInputStream in, ObjectOutputStream out, String nickname) throws IOException {
        this.connected = true;
        this.socket = socket;
        this.nickname = nickname;
        this.in = in;
        this.out = out;
        out.writeObject((new JSONBuilder()).build(ServerSocketProtocol.LOGIN, "success").get().toString());
        logger.log(Level.FINE, nickname + " logged!");
        out.flush();
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    private void sendJSON(JSONBuilder jsonBuilder){
        try {
            out.writeObject(jsonBuilder.get().toString());
            out.flush();
            System.out.println(jsonBuilder.get().toString());
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            disconnect();
        }
    }

    @Override
    public void updateView(Pool pool) {
        List<String> param = new ArrayList<>();
        for (Die die : pool.getDice()) {
            param.add(die.getId());
        }
        sendJSON(new JSONBuilder()
                .build(ServerSocketProtocol.UPDATE)
                .build(ServerSocketProtocol.POOL, param));
    }

    @Override
    public void updateView(RoundTrack roundTrack) {
        List<String> param = new ArrayList<>();
        for (int i = 1; i < roundTrack.getRound(); i++) {
            for (Die die : roundTrack.getDice(i)) {
                param.add("r"+ i + "-" + die.getId());
            }
        }

        sendJSON(new JSONBuilder()
                .build(ServerSocketProtocol.UPDATE)
                .build(ServerSocketProtocol.ROUND_TRACK, param));
    }

    @Override
    public void updateView(Table table) {

        JSONBuilder jsonBuilder = new JSONBuilder().build(ServerSocketProtocol.UPDATE);

        if (!table.getPublicObjectives().isEmpty()){
            List<String> param = new ArrayList<>();
            table.getPublicObjectives()
                    .forEach(po -> param.add(po.getName()));
            jsonBuilder.build(ServerSocketProtocol.PUBLIC_OBJ, param);
        }

        if (!table.getTools().isEmpty()){
            List<String> param = new ArrayList<>();
            table.getTools()
                    .forEach(t -> param.add(t.getName()+"-"+t.isUsed()));
            jsonBuilder.build(ServerSocketProtocol.TOOL, param);
        }

        if (!table.getPlayers().isEmpty()){
            List<String> param = new ArrayList<>();
            table.getPlayers()
                    .forEach(p -> param.add(p.getNickname()));
            jsonBuilder.build(ServerSocketProtocol.PLAYER, param);
        }
        sendJSON(jsonBuilder);
    }

    @Override
    public void updateView(Player player) {
        JSONBuilder jsonBuilder = new JSONBuilder().build(ServerSocketProtocol.UPDATE, player.getNickname());


        if(this.getNickname().equals(player.getNickname()) && !player.getPrivateObjective().isEmpty()) {
            List<String> param = new ArrayList<>();
            player.getPrivateObjective()
                    .forEach(p -> param.add(p.getName()));
            jsonBuilder.build(ServerSocketProtocol.PRIVATE_OBJ, param);
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
            jsonBuilder.build(ServerSocketProtocol.GLASS_WINDOW, param);

        }

        jsonBuilder.build(ServerSocketProtocol.TOKEN, Integer.toString(player.getTokens()));

        sendJSON(jsonBuilder);

    }

    @Override
    public void endGame(List<Pair<Player, Integer>> scores) {
        List<String> param = new ArrayList<>();

        scores.forEach(p -> param.add(p.getKey()+"-"+p.getValue()));

        sendJSON(new JSONBuilder()
                    .build(ServerSocketProtocol.END_GAME)
                    .build(ServerSocketProtocol.LEADER_BOARD, param));
    }

    @Override
    public GlassWindow chooseWindow(List<GlassWindow> glassWindows) {
        JSONBuilder jsonBuilder = new JSONBuilder().build(ServerSocketProtocol.CHOOSE_WINDOW);
        List<String> param = glassWindows
                .stream()
                .map(GlassWindow::getName)
                .collect(Collectors.toList());
        jsonBuilder.build(ServerSocketProtocol.GLASS_WINDOW, param);
        sendJSON(jsonBuilder);

        JSONObject response;
        try {
            if((response = (JSONObject) (new JSONParser()).parse((String) in.readObject())) != null) {
                if (response.get("header").equals("windowChosen")) {
                    Optional<GlassWindow> glassWindow = glassWindows.stream().filter(g -> g.getName().equals(response.get("mainParam"))).findFirst();
                    if (glassWindow.isPresent()) {
                        return glassWindow.get();
                    } else {
                        disconnect();
                    }
                } else {
                    disconnect();
                }
            }
        }catch(ClassNotFoundException | ParseException | IOException e){
            logger.log(Level.WARNING, e.getMessage(), e);
            disconnect();
        }
        return glassWindows.get(ThreadLocalRandom.current().nextInt(0, glassWindows.size()));
    }

    private void disconnect(){
        try {
            out.close();
            in.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        connected = false;
    }

    @Override
    public Identifiable selectObject(List<Identifiable> options, Identifiable container, boolean canSkip, boolean undoEnabled) {
        if(!isConnected()) return fakeResponse(canSkip, undoEnabled, options);

        //Send message
        JSONBuilder jsonBuilder = new JSONBuilder().build(ServerSocketProtocol.SELECT_OBJECT, container.getId());
        List<String> ids = options.stream().map(Identifiable::getId).collect(Collectors.toList());
        if (canSkip)
            ids.add(StdId.SKIP.getId());
        if(undoEnabled)
            ids.add(StdId.UNDO.getId());
        jsonBuilder.build(ServerSocketProtocol.OPTION, ids);
        sendJSON(jsonBuilder);
        //Receive response
        JSONObject response;
        try {
            if((response = (JSONObject) (new JSONParser()).parse((String) in.readObject())) != null) {
                if (response.get("header").equals("objectSelected")) {
                    if (response.get("mainParam").equals(StdId.UNDO.getId())) {
                        return StdId.UNDO;
                    }
                    if (response.get("mainParam").equals(StdId.SKIP.getId())) {
                        return StdId.SKIP;
                    }
                    Optional<Identifiable> selection = options.stream().filter(op -> op.getId().equals(response.get("mainParam"))).findFirst();
                    if (selection.isPresent()) {
                        return selection.get();
                    } else {
                        disconnect();
                    }
                } else {
                    disconnect();
                }
            }
        }catch(ClassNotFoundException | ParseException  | IOException e){
            logger.log(Level.WARNING, e.getMessage(), e);
            disconnect();
        }
        return fakeResponse(canSkip, undoEnabled, options);
    }

    @Override
    public Identifiable chooseFrom(List<Identifiable> options, String message, boolean canSkip, boolean undoEnabled) {
        if(!isConnected()) return fakeResponse(canSkip, undoEnabled, options);

        //send message
        //Send message
        JSONBuilder jsonBuilder = new JSONBuilder().build(ServerSocketProtocol.SELECT_FROM, message);
        List<String> opt = options.stream().map(Identifiable::getId).collect(Collectors.toList());
        if (canSkip)
            opt.add(StdId.SKIP.getId());
        if(undoEnabled)
            opt.add(StdId.UNDO.getId());
        jsonBuilder.build(ServerSocketProtocol.OPTION, opt);
        sendJSON(jsonBuilder);

        //Receive response
        JSONObject response;
        try {
            if((response = (JSONObject) (new JSONParser()).parse((String) in.readObject())) != null) {
                if (response.get("header").equals("selected")) {
                    if (response.get("mainParam").equals(StdId.UNDO.getId())) {
                        return StdId.UNDO;
                    }
                    if (response.get("mainParam").equals(StdId.SKIP.getId())) {
                        return StdId.SKIP;
                    }
                    Optional<Identifiable> selection = options.stream().filter(op -> op.getId().equals(response.get("mainParam"))).findFirst();
                    if (selection.isPresent()) {
                        return selection.get();
                    } else {
                        disconnect();
                    }
                } else {
                    disconnect();
                }
            }
        }catch(ClassNotFoundException | ParseException | IOException e){
            logger.log(Level.WARNING, e.getMessage(), e);
            disconnect();
        }
        return fakeResponse(canSkip, undoEnabled, options);
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

