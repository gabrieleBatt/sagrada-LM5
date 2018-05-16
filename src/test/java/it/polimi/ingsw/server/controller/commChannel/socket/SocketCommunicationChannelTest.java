package it.polimi.ingsw.server.controller.commChannel.socket;

import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.Pool;
import it.polimi.ingsw.server.model.table.RoundTrack;
import it.polimi.ingsw.server.model.table.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SocketCommunicationChannelTest {


    @BeforeEach
    @Test
    void setup() throws IOException {
        Socket socket = new Socket();
        ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream("resources/testResources/socketOut.txt"));
        SocketCommunicationChannel socketCommunicationChannel= new SocketCommunicationChannel(socket, null, o, "test");
        socketCommunicationChannel.updateView(new Pool());
        List<Player> players = new ArrayList<>();
        players.add(new Player("test"));
        players.add(new Player("test1"));
        socketCommunicationChannel.updateView(players.get(0));
        socketCommunicationChannel.updateView(players.get(1));
        socketCommunicationChannel.updateView(new RoundTrack());
        socketCommunicationChannel.updateView(new Table(players));
    }
}