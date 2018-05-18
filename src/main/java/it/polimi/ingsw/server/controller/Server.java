package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.net.socket.JSONBuilder;
import it.polimi.ingsw.server.controller.channels.CommunicationChannel;
import it.polimi.ingsw.server.controller.channels.RmiCommunicationChannel;
import it.polimi.ingsw.net.socket.SocketProtocol;
import it.polimi.ingsw.server.controller.channels.SocketCommunicationChannel;
import it.polimi.ingsw.net.interfaces.RemoteGameScreen;
import it.polimi.ingsw.net.interfaces.RemoteServer;
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

public class Server extends UnicastRemoteObject implements RemoteServer {

    private static Logger logger = LogMaker.getLogger(Server.class.getName(), Level.ALL);
    private static int rmiPortNumber;
    private static int socketPortNumber;
    private static final Lobby lobby = new Lobby();
    private static Set<Game> games = new HashSet<>();
    private static long loginTime;
    private static Server server;
    private static UsersDatabase usersDatabase = new UsersDatabase();

    static {
        JSONObject config;
        try {
            JSONParser parser = new JSONParser();
            config = (JSONObject)parser.parse(new FileReader(new File("resources/ServerResources/config.json")));
            loginTime = (long)config.get("loginTime");
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

    /**
     * gets the instance of the server
     * @return
     */
    public static Server getServer() {
        return server;
    }

    public static void addGame(Game game){
        games.add(game);
        new Thread(game).start();
    }

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(rmiPortNumber)
                    .rebind("Server", server);
            logger.log(Level.CONFIG, "Rmi server ready");
        } catch (RemoteException e) {
            logger.log(Level.WARNING, "Rmi server failed", e);
            System.exit(-1);
        }
        while (!Thread.interrupted()) {
            try (ServerSocket serverSocket = new ServerSocket(socketPortNumber)) {
                logger.log(Level.CONFIG, "Socket server ready");
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> socketLogin(clientSocket)).start();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Socket server failed", e);
            }
        }
    }

    @Override
    public void rmiLogin(RemoteGameScreen gameScreen, String nickname) throws RemoteException {
        logger.log(Level.FINE,  "logged!", nickname);
        addToGame(new RmiCommunicationChannel(gameScreen, nickname), nickname);
    }

    public static void socketLogin(Socket socket){
        try {
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            JSONObject loginMessage;

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        logger.log(Level.WARNING, "Socket closing failed", e);
                    }
                }
            }, loginTime * 1000);

            if((loginMessage = (JSONObject)(new JSONParser()).parse(in.readLine())) != null) {
                timer.cancel();
                if (loginMessage.get(SocketProtocol.HEADER.get()).equals(SocketProtocol.LOGIN.get())) {
                    String nickname = (String)loginMessage.get(SocketProtocol.NICKNAME.get());
                    String password =  (String)loginMessage.get(SocketProtocol.PASSWORD.get());
                    if(!usersDatabase.userExists(nickname)){
                        usersDatabase.newUser(nickname, password);
                    }else if(usersDatabase.authentication(nickname, password)){
                        logger.log(Level.FINE, "logged!", nickname);
                        addToGame(new SocketCommunicationChannel(socket, in, out, nickname), nickname);
                    }else{
                        out.println((new JSONBuilder()).build(SocketProtocol.LOGIN)
                                .build(SocketProtocol.RESULT, "authentication failed"));
                        out.flush();
                    }
                } else {
                    throw new IOException();
                }
            }
        } catch (ParseException | IOException e) {
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
