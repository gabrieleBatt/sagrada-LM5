package it.polimi.ingsw.client;

import it.polimi.ingsw.server.rmiInterface.RemoteServer;

import java.rmi.registry.LocateRegistry;

public class FakeRmiClient {

    private static final  String host = "localhost";

    public static void main(String[] args){
        try {
            RemoteServer remoteServer = (RemoteServer) LocateRegistry
                    .getRegistry(50000).lookup("Server");
            remoteServer.rmiLogin(new FakeGameScreen() {}, "testR");

        } catch (Exception e) {
            System.err.println("client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
