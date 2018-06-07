package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.shared.LogMaker;
import it.polimi.ingsw.shared.interfaces.RemoteChannel;
import it.polimi.ingsw.shared.socket.JSONBuilder;
import it.polimi.ingsw.server.controller.channels.CommunicationChannel;
import it.polimi.ingsw.server.controller.channels.RmiCommunicationChannel;
import it.polimi.ingsw.shared.socket.SocketProtocol;
import it.polimi.ingsw.server.controller.channels.SocketCommunicationChannel;
import it.polimi.ingsw.shared.interfaces.RemoteGameScreen;
import it.polimi.ingsw.shared.interfaces.RemoteServer;
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

/**
 * GuiConnectionScreen server class, handles new connection and re-connections
 */
public class Server extends UnicastRemoteObject implements RemoteServer {

    transient private static Logger logger = LogMaker.getLogger(Server.class.getName(), Level.ALL);
    private static int rmiPortNumber;
    private static int socketPortNumber;
    private static final Lobby lobby = new Lobby();
    private static Set<Game> games = new HashSet<>();

    private static long loginTime;
    private static Server server;

    private static final String CONFIG_PATH = "resources/ServerResources/config.json";
    private static final String LOGIN_TIME = "loginTime";
    private static final int STD_LOGIN_TIME = 60;
    private static final String RMI_PORT = "rmiPortNumber";
    private static final int STD_RMI_PORT = 50001;
    private static final String SOCKET_PORT = "socketPortNumber";
    private static final int STD_SOCKET_PORT = 50000;

    /*
     * Server configuration
     */
    static {
        JSONObject config;
        try {
            JSONParser parser = new JSONParser();
            config = (JSONObject)parser.parse(new FileReader(new File(CONFIG_PATH)));
            loginTime = (long)config.get(LOGIN_TIME);
            rmiPortNumber  = Math.toIntExact((long)config.get(RMI_PORT));
            socketPortNumber = Math.toIntExact((long)config.get(SOCKET_PORT));
        } catch (ParseException | IOException e) {
            loginTime = STD_LOGIN_TIME;
            rmiPortNumber  = STD_RMI_PORT;
            socketPortNumber = STD_SOCKET_PORT;
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
     * @return the instance of the server
     */
    public static Server getServer() {
        return server;
    }

    static void addGame(Game game){
        games.add(game);
        new Thread(game).start();
    }

    /**
     * GuiConnectionScreen server
     */
    public static void main(String[] args) {
        //Rmi connection
        try {
            LocateRegistry.createRegistry(rmiPortNumber)
                    .rebind("Server", server);
            logger.log(Level.CONFIG, "Rmi server ready on port: " + rmiPortNumber);
        } catch (RemoteException e) {
            logger.log(Level.WARNING, "Rmi server failed", e);
            return;
        }
        //Socket connection
        while (!Thread.interrupted()) {
            try (ServerSocket serverSocket = new ServerSocket(socketPortNumber)) {
                logger.log(Level.CONFIG, "Socket server ready on port: " + socketPortNumber);
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> socketLogin(clientSocket)).start();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Socket server failed", e);
            }
        }
    }

    /**
     * Login for a client using rmi connection
     * @param gameScreen the client remote game screen
     * @param nickname the player nickname
     * @param password the player password
     * @return the coupled remote channel
     */
    @Override
    public synchronized RemoteChannel rmiLogin(RemoteGameScreen gameScreen, String nickname, String password) throws RemoteException {
        logger.log(Level.FINE,  "logged!", nickname);
        if(UsersDatabase.createOrAuthenticate(nickname, password)){
            RmiCommunicationChannel rcc = new RmiCommunicationChannel(gameScreen, nickname);
            addToGame(rcc, nickname);
            UnicastRemoteObject.exportObject(rcc, 0);
            return rcc;
        }else{
            return null;
        }
    }

    private synchronized static void socketLogin(Socket socket){
        try {
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            JSONObject loginMessage;

            //login timer start
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

            //receive login message
            if((loginMessage = (JSONObject)(new JSONParser()).parse(in.readLine())) != null) {
                timer.cancel();
                if (loginMessage.get(SocketProtocol.HEADER.get()).equals(SocketProtocol.LOGIN.get())) {
                    String nickname = (String)loginMessage.get(SocketProtocol.NICKNAME.get());
                    String password =  (String)loginMessage.get(SocketProtocol.PASSWORD.get());
                    if(UsersDatabase.createOrAuthenticate(nickname, password)){
                        logger.log(Level.FINE, "logged!", nickname);
                        addToGame(new SocketCommunicationChannel(socket, in, out, nickname), nickname);
                        new JSONBuilder()
                                .build(SocketProtocol.LOGIN)
                                .build(SocketProtocol.RESULT, "success")
                                .send(out);
                    }else{
                        new JSONBuilder()
                                .build(SocketProtocol.LOGIN)
                                .build(SocketProtocol.RESULT, "authentication failed")
                                .send(out);
                    }
                } else {
                    throw new IOException();
                }
            }
        } catch (ParseException | IOException e) {
            logger.log(Level.WARNING, "Socket login failed", e);
        }
    }

    private synchronized static void addToGame(CommunicationChannel ccToAdd, String nickname){
        Boolean alreadyInGame = false;
        //check if already in lobby
        for (CommunicationChannel communicationChannel : lobby.getCommChannelSet()) {
            if (communicationChannel.getNickname().equals(nickname)) {
                alreadyInGame = true;
                lobby.changeChannel(ccToAdd);
                logger.log(Level.FINE, nickname + " reconnected to lobby");
            }
        }
        //check if already in game
        for (Game game : games) {
            for (CommunicationChannel communicationChannel : game.getCommChannels()) {
                if (communicationChannel.getNickname().equals(nickname)) {
                    alreadyInGame = true;
                    game.changeChannel(ccToAdd);
                    logger.log(Level.FINE, nickname + " reconnected to game");
                }
            }
        }
        if (!alreadyInGame) {
            synchronized (lobby) {
                lobby.addChannel(ccToAdd);
                logger.log(Level.FINE, nickname + " added to lobby");
            }
        }
    }

    synchronized static void endGame(Game game){
        games.remove(game);
    }
}
