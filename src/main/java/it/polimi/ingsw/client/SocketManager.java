package it.polimi.ingsw.client;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.client.view.LoginInfo;
import it.polimi.ingsw.client.view.factory.GameScreen;
import it.polimi.ingsw.net.identifiables.StdId;
import it.polimi.ingsw.net.socket.JSONBuilder;
import it.polimi.ingsw.net.socket.SocketProtocol;
import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.xml.stream.events.Characters;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SocketManager implements Runnable{

    private static Logger logger = LogMaker.getLogger(SocketManager.class.getName(), Level.ALL);
    private final String nickname;
    private final String password;
    private final String ip;
    private final int port;
    private BufferedReader in;
    private PrintWriter out;
    private final GameScreen gameScreen;

    public SocketManager(LoginInfo loginInfo, GameScreen gameScreen) {
        ip = loginInfo.ip;
        port = loginInfo.portNumber;
        nickname = loginInfo.nickname;
        password = loginInfo.password;
        this.gameScreen = gameScreen;
    }


    private Collection<String> jsonArrayToCollection(JSONArray jsonArray){
        Collection<String> ret = new ArrayList<>();
        for (Object o : jsonArray) {
            ret.add(o.toString());
        }
        return ret;
    }


    @Override
    public void run() {
        try (Socket socket = new Socket(ip, port)) {
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            if(!login()) {
                return;
            }
            JSONObject received;
            while ((received = (JSONObject) (new JSONParser()).parse(in.readLine())) != null) {
                Object header = received.get(SocketProtocol.HEADER.get());
                if (header.equals(SocketProtocol.CHOOSE_WINDOW.get())) {
                    String gw = gameScreen.getWindow(jsonArrayToCollection((JSONArray)received.get(SocketProtocol.GLASS_WINDOW.get())));
                    new JSONBuilder()
                            .build(SocketProtocol.CHOOSE_WINDOW)
                            .build(SocketProtocol.GLASS_WINDOW, gw)
                            .send(out);
                } else if (header.equals(SocketProtocol.SELECT_OBJECT.get())) {
                    String so = gameScreen
                            .getInput(jsonArrayToCollection((JSONArray)received.get(SocketProtocol.OPTION.get())),
                                    received.get(SocketProtocol.CONTAINER.get()).toString());
                    new JSONBuilder()
                            .build(SocketProtocol.SELECT_OBJECT)
                            .build(SocketProtocol.OPTION, so)
                            .send(out);
                } else if (header.equals(SocketProtocol.SELECT_FROM.get())) {
                    String sf = gameScreen
                            .getInputFrom(jsonArrayToCollection((JSONArray)received.get(SocketProtocol.OPTION.get())),
                                    received.get(SocketProtocol.MESSAGE.get()).toString());
                    new JSONBuilder()
                            .build(SocketProtocol.SELECT_FROM)
                            .build(SocketProtocol.OPTION, sf)
                            .send(out);
                }else if (header.equals(SocketProtocol.UPDATE.get())){
                    updateIf(received);
                }else if (header.equals(SocketProtocol.UPDATE_PLAYER.get())){
                    updatePlayerIf(received);
                }
                gameScreen.showAll();
            }
        }catch(ParseException | IOException | NullPointerException e){
            logger.log(Level.WARNING, "Connection failed");
        }
    }

    private void updatePlayerIf(JSONObject received){
        String player = received.get(SocketProtocol.PLAYER.get()).toString();
        if(received.containsKey(SocketProtocol.TOKEN.get())) {
            gameScreen.setPlayerToken(player, Integer.parseInt(received.get(SocketProtocol.TOKEN.get()).toString()));
        }
        if(received.containsKey(SocketProtocol.PRIVATE_OBJ.get())) {
            Collection<String> strings = getJsonList(received, SocketProtocol.PRIVATE_OBJ);
            gameScreen.setPrivateObjectives(strings);
        }
        if(received.containsKey(SocketProtocol.GLASS_WINDOW.get())) {
            List<String> strings = getJsonList(received, SocketProtocol.GLASS_WINDOW);
            gameScreen.setPlayerWindow(player, strings.get(0));
            for (int i = 0; i < 4; i++) {
                for (int i1 = 0; i1 < 5; i1++) {
                    gameScreen.setCellContent(player, i, i1, strings.get(i*5+i1+1));
                }
            }
        }
        gameScreen.setPlayerConnection(player, received.get(SocketProtocol.CONNECTION.get()).equals("true"));
    }


    private void updateIf(JSONObject received){
        if (received.containsKey(SocketProtocol.POOL.get())){
            Collection<String> strings = getJsonList(received, SocketProtocol.POOL);
            gameScreen.setPool(strings);
        }
        if (received.containsKey(SocketProtocol.ROUND_TRACK.get())){
            updateRoundTrack(received);
        }
        if (received.containsKey(SocketProtocol.TOOL.get())){
            Collection<String> strings = getJsonList(received, SocketProtocol.ROUND_TRACK);
            List<Pair<String, Boolean>> pairs = strings
                    .stream()
                    .map(s -> new Pair<>(s.substring(0, s.indexOf('-')), s.contains("true")))
                    .collect(Collectors.toList());
            gameScreen.setTools(pairs.stream().map(Pair::getKey).collect(Collectors.toList()));
            for (Pair<String, Boolean> pair : pairs) {
                gameScreen.setToolUsed(pair.getKey(), pair.getValue());
            }
        }
        if (received.containsKey(SocketProtocol.PLAYER.get())){
            List<String> strings = getJsonList(received, SocketProtocol.PLAYER);
            strings.remove(nickname);
            strings.add(0, nickname);
            gameScreen.setPlayers(strings);
        }
        if (received.containsKey(SocketProtocol.PRIVATE_OBJ.get())){
            Collection<String> strings = getJsonList(received, SocketProtocol.PRIVATE_OBJ);
            gameScreen.setPrivateObjectives(strings);
        }
    }

    private void updateRoundTrack(JSONObject received){
        Collection<String> strings = getJsonList(received, SocketProtocol.ROUND_TRACK);
        List<List<String>> lists = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final int x = i;
            if(strings.stream()
                    .anyMatch(s -> Character.getNumericValue(s.charAt(1)) == x+1 )){
                lists.add(new ArrayList<>());
                strings.stream()
                        .filter(s -> Character.getNumericValue(s.charAt(1)) == x+1 )
                        .forEach(s -> lists.get(x).add(s.substring(2)));
            }
        }
        gameScreen.setRoundTrack(lists);
    }

    private List<String> getJsonList(JSONObject jsonObject, SocketProtocol socketProtocol){
        JSONArray jsonArray = (JSONArray)jsonObject.get(socketProtocol.get());
        return new ArrayList<String>(jsonArray);
    }

    private boolean login() throws IOException, ParseException {
        new JSONBuilder().build(SocketProtocol.LOGIN)
                .build(SocketProtocol.NICKNAME, nickname)
                .build(SocketProtocol.PASSWORD, password)
                .send(out);
        JSONObject received;
        if ((received = (JSONObject) (new JSONParser()).parse(in.readLine())) != null) {
            if (received.get(SocketProtocol.HEADER.get()).equals(SocketProtocol.LOGIN.get())) {
                if (received.get(SocketProtocol.RESULT.get()).equals("success")) {
                    logger.log(Level.FINE, "Login success");
                    return true;
                } else {
                    logger.log(Level.FINE, received.get(SocketProtocol.RESULT.get()).toString());
                }
            }
        }
        Client.lostConnection();
        return false;
    }

}
