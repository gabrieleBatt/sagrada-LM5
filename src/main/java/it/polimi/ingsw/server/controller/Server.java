package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.controller.commChannel.CommunicationChannel;
import it.polimi.ingsw.server.controller.commChannel.rmi.RmiCommunicationChannel;
import it.polimi.ingsw.server.controller.commChannel.socket.SocketCommunicationChannel;
import it.polimi.ingsw.server.controller.commChannel.rmi.rmiInterface.RemoteGameScreen;
import it.polimi.ingsw.server.controller.commChannel.rmi.rmiInterface.RemoteServer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Server extends UnicastRemoteObject implements RemoteServer {

    private static Logger logger = LogMaker.getLogger(Server.class.getName(), Level.ALL);
    private static int rmiPortNumber;
    private static int socketPortNumber;
    private static final Lobby lobby = new Lobby();
    private static Set<Game> games = new HashSet<>();
    private static int loginTime;
    private static Server server;

    static {
        JSONObject config = null;
        try {
            JSONParser parser = new JSONParser();
            config = (JSONObject)parser.parse(new FileReader(new File("resources/ServerResources/config.json")));
            loginTime = Math.toIntExact((long)config.get("loginTime"));
            rmiPortNumber  = Math.toIntExact((long)config.get("rmiPortNumber"));
            socketPortNumber = Math.toIntExact((long)config.get("socketPortNumber"));
        } catch (ParseException | IOException e) {
            loginTime = 60;
            rmiPortNumber  = 50001;
            socketPortNumber = 50000;
        }

        try {
            server = new Server();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Server didn't started");
        }
    }

    private Server() throws RemoteException {
        super();
    }

    public static Server getServer() {
        return server;
    }

    public static void addGame(Game game){
        games.add(game);
        new Thread(game).start();
    }

    public static void main(String args[]) {
        try {
            LocateRegistry.createRegistry(rmiPortNumber)
                    .rebind("Server", server);
            logger.log(Level.CONFIG, "Rmi server ready");
        } catch (RemoteException e) {
            logger.log(Level.WARNING, "Rmi server failed", e);
            System.exit(-1);
        }
        try{
            ServerSocket serverSocket = new ServerSocket(socketPortNumber);
            logger.log(Level.CONFIG, "Socket server ready");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> socketLogin(clientSocket)).start();
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Socket server failed", e);
        }
    }

    @Override
    public void rmiLogin(RemoteGameScreen gameScreen, String nickname) throws RemoteException {
        logger.log(Level.FINE, nickname + " logged!");

        addToGame(new RmiCommunicationChannel(gameScreen, nickname), nickname);
    }

    private static void socketLogin(Socket socket){
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            JSONObject loginMessage;

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    //end
                }
            }, loginTime * 1000);

            if((loginMessage = (JSONObject)(new JSONParser()).parse((String)in.readObject())) != null) {
                if (loginMessage.get("header").equals("login")) {
                    String nickname = (String)loginMessage.get("mainParam");
                    logger.log(Level.FINE, nickname + " logged!");

                    addToGame(new SocketCommunicationChannel(socket, in, out, nickname), nickname);

                } else {
                    throw new IOException();
                }
            }
        } catch (ClassNotFoundException | ParseException | IOException e) {
            logger.log(Level.WARNING, "Socket login failed", e);
        }
    }

    private static void addToGame(CommunicationChannel ccToAdd, String nickname){
        Boolean alreadyInGame = false;
        for (CommunicationChannel communicationChannel : lobby.getCommChannels()) {
            if (communicationChannel.getNickname().equals(nickname)) {
                alreadyInGame = true;
                lobby.changeChannel(ccToAdd);
            }
        }
        for (Game game : games) {
            for (CommunicationChannel communicationChannel : game.getCommChannels()) {
                if (communicationChannel.getNickname().equals(nickname)) {
                    alreadyInGame = true;
                    game.changeChannel(ccToAdd);
                }
            }
        }
        if (!alreadyInGame) {
            synchronized (lobby) {
                lobby.addChannel(ccToAdd);
            }
        }
    }
}
