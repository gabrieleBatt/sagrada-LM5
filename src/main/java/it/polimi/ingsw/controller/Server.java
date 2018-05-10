package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commChannel.RmiCommunicationChannel;
import it.polimi.ingsw.controller.commChannel.SocketCommunicationChannel;
import it.polimi.ingsw.rmiInterface.RemoteGameScreen;
import it.polimi.ingsw.rmiInterface.RemoteServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements RemoteServer {

    private static final int rmiPortNumber = 1100;
    private static final int socketPortNumber = 1101;
    private static Lobby lobby;

    private Server() throws RemoteException {
        super();
        lobby = new Lobby();
    }

    public static void main(String args[]) {
        try {
            Registry registry = LocateRegistry.createRegistry(rmiPortNumber);
            registry.rebind("Server", new Server());

            System.out.println("Rmi server ready");
        } catch (RemoteException e) {
            System.out.println("Server failed to start");
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        try{
            ServerSocket serverSocket = new ServerSocket(socketPortNumber);
            while (true) {
                System.out.println("Socket server ready");
                Socket clientSocket = serverSocket.accept();
                System.out.println("New socket client!");
                socketLogin(clientSocket);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rmiLogin(RemoteGameScreen gameScreen, String nickname) throws RemoteException{
        System.out.println(nickname + " logged!");
        lobby.addChannel(new RmiCommunicationChannel(gameScreen, nickname));
    }

    private static void socketLogin(Socket socket){
        lobby.addChannel(new SocketCommunicationChannel(socket));
    }
}
