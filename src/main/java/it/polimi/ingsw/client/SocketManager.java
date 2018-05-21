package it.polimi.ingsw.client;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.client.view.LoginInfo;
import it.polimi.ingsw.client.view.factory.GameScreen;
import it.polimi.ingsw.net.socket.JSONBuilder;
import it.polimi.ingsw.net.socket.SocketProtocol;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketManager implements Runnable{

    private static Logger logger = LogMaker.getLogger(SocketManager.class.getName(), Level.ALL);
    private final String nickname;
    private final String password;
    private final String ip;
    private final int port;
    private BufferedReader in;
    private PrintWriter out;
    private final Scanner scanner;
    private final GameScreen gameScreen;

    public SocketManager(LoginInfo loginInfo, GameScreen gameScreen) {
        ip = loginInfo.ip;
        port = loginInfo.portNumber;
        nickname = loginInfo.nickname;
        password = loginInfo.password;
        scanner = new Scanner(System.in);
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
                if (received.get(SocketProtocol.HEADER).equals(SocketProtocol.CHOOSE_WINDOW.get())) {
                    String gw = gameScreen.getWindow(jsonArrayToCollection((JSONArray)received.get(SocketProtocol.GLASS_WINDOW.get())));
                    new JSONBuilder()
                            .build(SocketProtocol.CHOOSE_WINDOW)
                            .build(SocketProtocol.GLASS_WINDOW, gw)
                            .send(out);
                } else if (received.get(SocketProtocol.HEADER).equals(SocketProtocol.SELECT_OBJECT.get())) {
                    String so = gameScreen
                            .getInput(jsonArrayToCollection((JSONArray)received.get(SocketProtocol.OPTION.get())),
                                    received.get(SocketProtocol.CONTAINER.get()).toString());
                    new JSONBuilder()
                            .build(SocketProtocol.SELECT_OBJECT)
                            .build(SocketProtocol.OPTION, so)
                            .send(out);
                } else if (received.get(SocketProtocol.HEADER).equals(SocketProtocol.SELECT_FROM.get())) {
                    String sf = gameScreen
                            .getInputFrom(jsonArrayToCollection((JSONArray)received.get(SocketProtocol.OPTION.get())),
                                    received.get(SocketProtocol.MESSAGE.get()).toString());
                    new JSONBuilder()
                            .build(SocketProtocol.SELECT_FROM)
                            .build(SocketProtocol.OPTION, sf)
                            .send(out);
                    }
            }
        }catch(ParseException | IOException e){
            logger.log(Level.WARNING, "Connection failed");
        }
    }

    private boolean login() throws IOException, ParseException {
        new JSONBuilder().build(SocketProtocol.LOGIN)
                .build(SocketProtocol.NICKNAME, nickname)
                .build(SocketProtocol.PASSWORD, password)
                .send(out);
        JSONObject received;
        if ((received = (JSONObject) (new JSONParser()).parse(in.readLine())) != null) {
            if (received.get(SocketProtocol.HEADER).equals(SocketProtocol.LOGIN.get())) {
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
