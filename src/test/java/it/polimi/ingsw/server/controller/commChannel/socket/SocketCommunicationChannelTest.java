package it.polimi.ingsw.server.controller.commChannel.socket;

import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.Pool;
import it.polimi.ingsw.server.model.table.RoundTrack;
import it.polimi.ingsw.server.model.table.Table;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SocketCommunicationChannelTest {


    @DisplayName("Update test")
    @Test
    void update() throws IOException {
        Socket socket = new Socket();
        BufferedReader reader = new BufferedReader(new FileReader("resources/testResources/socketOut.txt"));
        PrintWriter o = new PrintWriter(new FileOutputStream("resources/testResources/socketOut.txt"));
        SocketCommunicationChannel socketCommunicationChannel= new SocketCommunicationChannel(socket, null, o, "test");
        Assertions.assertEquals("{\"header\":\"login\",\"mainParam\":\"success\"}", reader.readLine());
        socketCommunicationChannel.updateView(new Pool());
        Assertions.assertEquals("{\"-p\":[],\"header\":\"update\"}", reader.readLine());
        List<Player> players = new ArrayList<>();
        players.add(new Player("test"));
        players.add(new Player("test1"));
        socketCommunicationChannel.updateView(players.get(0));
        Assertions.assertEquals("{\"-t\":\"0\",\"header\":\"update\",\"mainParam\":\"test\"}", reader.readLine());
        socketCommunicationChannel.updateView(players.get(1));
        Assertions.assertEquals("{\"-t\":\"0\",\"header\":\"update\",\"mainParam\":\"test1\"}", reader.readLine());
        socketCommunicationChannel.updateView(new RoundTrack());
        Assertions.assertEquals("{\"header\":\"update\",\"-rt\":[]}", reader.readLine());
        socketCommunicationChannel.updateView(new Table(players));
        Assertions.assertEquals("{\"header\":\"update\",\"-pl\":[\"test\",\"test1\"]}", reader.readLine());
    }
}