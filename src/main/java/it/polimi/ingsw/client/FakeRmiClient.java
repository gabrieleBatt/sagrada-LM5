package it.polimi.ingsw.client;

import it.polimi.ingsw.rmiInterface.RemoteServer;

import java.rmi.registry.LocateRegistry;

public class FakeRmiClient {

    public static void main(String[] args){
        String host = "localhost";
        try {
            RemoteServer remoteServer = (RemoteServer) LocateRegistry.getRegistry(1100).lookup("Server");
            remoteServer.rmiLogin(new FakeGameScreen() {}, "testR");

        } catch (Exception e) {
            System.err.println("client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
