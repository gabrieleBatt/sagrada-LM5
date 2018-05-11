package it.polimi.ingsw.controller;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.controller.commChannel.RmiCommunicationChannel;
import it.polimi.ingsw.controller.commChannel.SocketCommunicationChannel;
import it.polimi.ingsw.rmiInterface.RemoteGameScreen;
import it.polimi.ingsw.rmiInterface.RemoteServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends UnicastRemoteObject implements RemoteServer {

    private static Logger logger = LogMaker.getLogger(Server.class.getName(), Level.ALL);
    private static final int rmiPortNumber = 1100;
    private static final int socketPortNumber = 1101;
    private static final Lobby lobby = new Lobby();



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
            String nickname;
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            nickname = in.readLine();
            logger.log(Level.FINE, nickname + " logged!");
            synchronized (lobby){
                lobby.addChannel(new SocketCommunicationChannel(socket, nickname));
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Socket login failed", e);
        }
    }
}
