package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.channels.CommunicationChannel;
import it.polimi.ingsw.server.controller.channels.TestGameScreen;
import it.polimi.ingsw.server.controller.channels.RmiCommunicationChannel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class LobbyTest {

    @DisplayName("Add channels to lobby and make game start")
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