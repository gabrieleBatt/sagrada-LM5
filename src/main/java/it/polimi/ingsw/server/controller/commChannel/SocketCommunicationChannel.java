package it.polimi.ingsw.server.controller.commChannel;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.model.table.glassWindow.GlassWindow;

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
    public void updateView() {
    }

    @Override
    public GlassWindow chooseWindow(List<GlassWindow> glassWindows) {
        String message = "chooseWindow ";
        //glassWindows.forEach(gw -> message = message + gw.getName() + " ");
        for (GlassWindow glassWindow : glassWindows) {
            message = message + glassWindow.getName() + " ";
        }
        out.println(message);
        out.flush();
        logger.log(Level.FINE, "Sent "+ message, this);
        String response;
        try {
            response = in.readLine();
            List<String> streamList = Stream.of(response.split(" ")).map(String::new).filter(x -> !x.equals("")).collect(Collectors.toList());
            if(streamList.get(0).equals("windowChosen")){
                Optional<GlassWindow> glassWindow = glassWindows.stream().filter(g -> g.equals(streamList.get(1))).findFirst();
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
        }finally {
            int index = ThreadLocalRandom.current().nextInt(0, glassWindows.size());
            return glassWindows.get(index);
        }
    }

    private void disconnect(){
        connected = false;
    }

    @Override
    public String selectOption(List<String> ids, String container, boolean canSkip, boolean undoEnabled) {
        String message = "selectObject " + container + " ";

        for ( String s : ids) {
            message = message + s + " ";
        }
        if (canSkip)
            message = message + " -s ";
        if(undoEnabled)
            message = message + " -u ";

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
        }finally {
            //TODO
            int index = ThreadLocalRandom.current().nextInt(0, ids.size());
            return ids.get(index);
        }
    }

    @Override
    public String chooseFrom(List<String> options, String message, boolean canSkip, boolean undoEnabled) {
        String toSend = "selectFrom " + message;

        for ( String s : options) {
            toSend = toSend + s + " ";
        }
        if (canSkip)
            toSend = toSend + " -s ";
        if(undoEnabled)
            toSend = toSend + " -u ";

        out.println(toSend);
        out.flush();

        logger.log(Level.FINE, "Sent "+ toSend, this);

        String response;

        try {
            response = in.readLine();
            List<String> streamList = Stream.of(response.split(" ")).map(String::new).filter(x -> !x.equals("")).collect(Collectors.toList());
            if(streamList.get(0).equals("selected")){
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
            logger.log(Level.WARNING, e.getMessage(), e);
            disconnect();
        }finally {
            int index = ThreadLocalRandom.current().nextInt(0, options.size());
            return options.get(index);
        }

    }
}

