package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.controller.commChannel.RmiCommunicationChannel;
import it.polimi.ingsw.server.controller.commChannel.SocketCommunicationChannel;
import it.polimi.ingsw.server.rmiInterface.RemoteGameScreen;
import it.polimi.ingsw.server.rmiInterface.RemoteServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Server extends UnicastRemoteObject implements RemoteServer {

    private static Logger logger = LogMaker.getLogger(Server.class.getName(), Level.ALL);
    private static final int rmiPortNumber = 1100;
    private static final int socketPortNumber = 1101;
    private static final Lobby lobby = new Lobby();
    private static final long  loginTime = 10;


    private Server() throws RemoteException {
        super();
    }

    public static void main(String args[]) {
        try {
            Registry registry = LocateRegistry.createRegistry(rmiPortNumber);
            registry.rebind("Server", new Server());

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
        synchronized (lobby) {
            lobby.addChannel(new RmiCommunicationChannel(gameScreen, nickname));
        }
    }

    private static void socketLogin(Socket socket){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String loginMessage;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                }
            }, loginTime * 1000);

            if((loginMessage = in.readLine()) != null) {
                List<String> streamList = Stream.of(loginMessage.split(" ")).map(String::new).filter(x -> !x.equals("")).collect(Collectors.toList());
                if (streamList.get(0).equals("login")) {
                    String nickname = streamList.get(1);
                    logger.log(Level.FINE, nickname + " logged!");
                    synchronized (lobby) {
                        lobby.addChannel(new SocketCommunicationChannel(socket, nickname));
                    }
                } else {
                    throw new IOException();
                }
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Socket login failed", e);
        }
    }
}
