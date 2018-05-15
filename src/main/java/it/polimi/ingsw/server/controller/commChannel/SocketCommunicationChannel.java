package it.polimi.ingsw.server.controller.commChannel;

import com.sun.jndi.ldap.Connection;
import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.model.objective.PublicObjective;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.Pool;
import it.polimi.ingsw.server.model.table.RoundTrack;
import it.polimi.ingsw.server.model.table.Table;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.glassWindow.Cell;
import it.polimi.ingsw.server.model.table.glassWindow.GlassWindow;
import it.polimi.ingsw.server.model.tool.Tool;
import javafx.util.Pair;
import sun.net.ConnectionResetException;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SocketCommunicationChannel implements CommunicationChannel {

    private static final Logger logger = LogMaker.getLogger(SocketCommunicationChannel.class.getName(), Level.ALL);
    private final Socket socket;
    private final String nickname;
    private final BufferedReader in;
    private final PrintWriter out;
    private boolean connected;

    public SocketCommunicationChannel(Socket socket, String nickname) throws IOException {
        this.connected = true;
        this.socket = socket;
        this.nickname = nickname;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        out.println(nickname + " logged!");
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

    @Override
    public void updateView(Pool pool) {
        StringBuilder message = new StringBuilder("update -p ");
        for (Die die : pool.getDice()) {
            message.append(die.getId()).append(" ");
        }
        out.println(message);
        out.flush();
        logger.log(Level.FINE, "Pool updated");
    }

    @Override
    public void updateView(RoundTrack roundTrack) {
        StringBuilder message = new StringBuilder("update -rt ");
        for (int i = 1; i < roundTrack.getRound(); i++) {
            for (Die die : roundTrack.getDice(i)) {
                message.append("round").append(i).append(":").append(die.getId()).append(" ");
            }
        }
        out.println(message);
        out.flush();
        logger.log(Level.FINE, "RoundTrack updated");

    }

    @Override
    public void updateView(Table table) {
        StringBuilder message = new StringBuilder("update ");
        if (!table.getPublicObjectives().isEmpty()){
            message.append("-pub ");
            table.getPublicObjectives().forEach(po -> message.append(po.getName()).append(" "));
        }

        if (!table.getTools().isEmpty()){
            message.append("-t ");
            table.getTools().forEach(t -> message.append(t.getName()).append("-").append(t.isUsed()).append(" "));

        }

        if (!table.getPlayers().isEmpty()){
            message.append("-pl ");
            table.getPlayers().forEach(p -> message.append(p.getNickname()).append(" "));
        }

        out.println(message);
        out.flush();
        logger.log(Level.FINE, "Table updated");

    }

    @Override
    public void updateView(Player player) {
        StringBuilder message = new StringBuilder("update ");
        if(this.getNickname().equals(player.getNickname()) && !player.getPrivateObjective().isEmpty()) {
            message.append("-prv ");
            player.getPrivateObjective().forEach(p -> message.append(p.getName()).append(" "));
        }

        if(player.hasGlassWindow()){
            message.append("-wd ");
            if(!player.getNickname().equals(this.getNickname()))
                message.append(player.getNickname()).append(" ");
            message.append(player.getGlassWindow().getName()).append(" ");

            for (Cell cell : player.getGlassWindow().getCellList()) {
                if (cell.isOccupied())
                    message.append(cell.getDie().getId()).append(" ");
                else
                    message.append("empty ");
            }

        }

        message.append("-tk ");
        if(!player.getNickname().equals(this.getNickname()))
            message.append(player.getNickname()).append(" ");
        message.append(player.getTokens()).append(" ");

        out.println(message);
        out.flush();
        logger.log(Level.FINE, "Player updated");
    }

    @Override
    public void endGame(List<Pair<Player, Integer>> scores) {
        StringBuilder message = new StringBuilder("endGame ");
        scores.forEach(p -> message.append(p.getKey()).append("-").append(p.getValue()).append(" "));
        
        out.println(message);
        out.flush();
        logger.log(Level.FINE, "Game ended");

    }

    @Override
    public GlassWindow chooseWindow(List<GlassWindow> glassWindows) {
        StringBuilder message = new StringBuilder("chooseWindow ");
        for (GlassWindow glassWindow : glassWindows) {
            message.append(glassWindow.getName()).append(" ");
        }
        out.println(message);
        out.flush();
        logger.log(Level.FINE, "Sent "+ message, this);
        String response;
        try {
            response = in.readLine();
            List<String> streamList = Stream.of(response.split(" ")).map(String::new).filter(x -> !x.equals("")).collect(Collectors.toList());
            if(streamList.get(0).equals("windowChosen")){
                Optional<GlassWindow> glassWindow = glassWindows.stream().filter(g -> g.getName().equals(streamList.get(1))).findFirst();
                if (glassWindow.isPresent()){
                    return glassWindow.get();
                }else{
                    disconnect();
                }
            }else{
                disconnect();
            }

        }catch(IOException e){
            logger.log(Level.WARNING, e.getMessage(), e);
            disconnect();
        }
        return glassWindows.get(ThreadLocalRandom.current().nextInt(0, glassWindows.size()));
    }

    private void disconnect(){
        connected = false;
    }

    @Override
    public String selectOption(List<String> ids, Object container, boolean canSkip, boolean undoEnabled) {
        if(!isConnected()) return fakeResponse(canSkip, undoEnabled, ids);
        StringBuilder message = new StringBuilder("selectObject " + container.getClass().getName() + " ");

        for ( String s : ids) {
            message.append(s).append(" ");
        }
        if (canSkip)
            message.append(" -s ");
        if(undoEnabled)
            message.append(" -u ");

        out.println(message);
        out.flush();

        logger.log(Level.FINE, "Sent "+ message, this);
        String response;
        try {
            if((response  = in.readLine()) != null) {
                List<String> streamList = Stream.of(response.split(" ")).map(String::new).filter(x -> !x.equals("")).collect(Collectors.toList());
                if (streamList.get(0).equals("optionSelected")) {
                    if (streamList.get(1).equals("-u")) {
                        return "undo";
                    }
                    if (streamList.get(1).equals("-s")) {
                        return "skip";
                    }
                    Optional<String> selection = ids.stream().filter(i -> i.equals(streamList.get(1))).findFirst();
                    if (selection.isPresent()) {
                        return selection.get();
                    } else {
                        disconnect();
                    }
                } else {
                    disconnect();
                }
            }
        }catch(IOException e){
            logger.log(Level.WARNING, e.getMessage(), e);
            disconnect();
        }
        return fakeResponse(canSkip, undoEnabled, ids);
    }

    @Override
    public String chooseFrom(List<String> options, String message, boolean canSkip, boolean undoEnabled) {
        if(!isConnected()) return fakeResponse(canSkip, undoEnabled, options);

        StringBuilder toSend = new StringBuilder("selectFrom " + message + " ");

        for ( String s : options) {
            toSend.append(s).append(" ");
        }
        if (canSkip)
            toSend.append(" -s ");
        if(undoEnabled)
            toSend.append(" -u ");

        out.println(toSend);
        out.flush();
        logger.log(Level.FINE, "Sent "+ toSend, this);
        String response;
        try {
            response = in.readLine();
            List<String> streamList = Stream.of(response.split(" ")).map(String::new).filter(x -> !x.equals("")).collect(Collectors.toList());
            if(streamList.get(0).equals("selected")){
                if (streamList.get(1).equals("-u")) {
                    return "undo";
                }
                if (streamList.get(1).equals("-s")) {
                    return "skip";
                }
                Optional<String> selection = options.stream().filter(g -> g.equals(streamList.get(1))).findFirst();
                if (selection.isPresent()){
                    return selection.get();
                }else{
                    disconnect();
                }
            }else{
                disconnect();
            }

        }catch(IOException e){
            //logger.log(Level.WARNING, e.getMessage(), e);
            disconnect();
        }
        return fakeResponse(canSkip, undoEnabled, options);
    }

    private String fakeResponse(boolean canSkip, boolean undoEnabled, List<String> op){
        if(canSkip)
            return "skip";
        else if(undoEnabled)
            return "undo";
        else
            return op.get(ThreadLocalRandom.current().nextInt(0, op.size()));
    }
}

