package it.polimi.ingsw.server.controller.channels;

import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.Pool;
import it.polimi.ingsw.server.model.table.RoundTrack;
import it.polimi.ingsw.server.model.table.Table;
import org.junit.jupiter.api.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class SocketCommunicationChannelTest{


    @DisplayName("Update test")
    @Test
    void update() throws IOException {
        Socket socket = new Socket();
        BufferedReader reader = new BufferedReader(new FileReader("resources/testResources/socketOut.txt"));
        PrintWriter o = new PrintWriter(new FileOutputStream("resources/testResources/socketOut.txt"));
        SocketCommunicationChannel socketCommunicationChannel= new SocketCommunicationChannel(socket, null, o, "test");
        Assertions.assertEquals("{\"result\":\"success\",\"header\":\"login\"}", reader.readLine());
        socketCommunicationChannel.updateView(new Pool());
        Assertions.assertEquals("{\"pool\":[],\"header\":\"update\"}", reader.readLine());
        List<Player> players = new ArrayList<>();
        players.add(new Player("test"));
        players.add(new Player("test1"));
        socketCommunicationChannel.updateView(players.get(0), true);
        Assertions.assertEquals("{\"header\":\"updatePlayer\",\"connection\":\"true\",\"player\":\"test\",\"token\":\"0\"}", reader.readLine());
        socketCommunicationChannel.updateView(players.get(1), true);
        Assertions.assertEquals("{\"header\":\"updatePlayer\",\"connection\":\"true\",\"player\":\"test1\",\"token\":\"0\"}", reader.readLine());
        socketCommunicationChannel.updateView(new RoundTrack());
        Assertions.assertEquals("{\"header\":\"update\",\"roundTrack\":[]}", reader.readLine());
        socketCommunicationChannel.updateView(new Table(players));
        Assertions.assertEquals("{\"header\":\"update\",\"player\":[\"test\",\"test1\"]}", reader.readLine());
    }

    @AfterEach
    void report(){
    }
}