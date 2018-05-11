package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.FakeGameScreen;
import it.polimi.ingsw.controller.commChannel.CommunicationChannel;
import it.polimi.ingsw.controller.commChannel.RmiCommunicationChannel;
import it.polimi.ingsw.controller.commChannel.SocketCommunicationChannel;
import it.polimi.ingsw.rmiInterface.RemoteGameScreen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.*;

class LobbyTest {

    @DisplayName("Add commChannel to lobby and make game start")
    @Test
    void addChannel() throws IOException {
        Lobby lobby = new Lobby();
        lobby.addChannel(new RmiCommunicationChannel(new RemoteGameScreen() {}, "testR"));
        lobby.addChannel(new RmiCommunicationChannel(new RemoteGameScreen() {}, "testR"));
        lobby.addChannel(new RmiCommunicationChannel(new RemoteGameScreen() {}, "testR"));
        lobby.addChannel(new RmiCommunicationChannel(new RemoteGameScreen() {}, "testR"));
        Assertions.assertTrue(lobby.getCommChannelSet().isEmpty());

        CommunicationChannel cc = new RmiCommunicationChannel(new RemoteGameScreen() {}, "testR");
        lobby.addChannel(cc);
        Assertions.assertTrue(lobby.getCommChannelSet().contains(cc));
    }
}