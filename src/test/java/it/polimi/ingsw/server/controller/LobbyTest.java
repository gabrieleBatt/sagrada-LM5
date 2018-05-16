package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.FakeGameScreen;
import it.polimi.ingsw.server.controller.commChannel.CommunicationChannel;
import it.polimi.ingsw.server.controller.commChannel.RmiCommunicationChannel;
import it.polimi.ingsw.server.controller.commChannel.SocketCommunicationChannel;
import it.polimi.ingsw.server.rmiInterface.RemoteGameScreen;
import it.polimi.ingsw.server.rmiInterface.TestGameScreen;
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
        lobby.addChannel(new RmiCommunicationChannel(new TestGameScreen(), "testR1"));
        lobby.addChannel(new RmiCommunicationChannel(new TestGameScreen(), "testR2"));
        lobby.addChannel(new RmiCommunicationChannel(new TestGameScreen(), "testR3"));
        lobby.addChannel(new RmiCommunicationChannel(new TestGameScreen(), "testR4"));
        Assertions.assertTrue(lobby.getCommChannelSet().isEmpty());

        CommunicationChannel cc = new RmiCommunicationChannel(new TestGameScreen(), "testR");
        lobby.addChannel(cc);
        Assertions.assertTrue(lobby.getCommChannelSet().contains(cc));
    }
}