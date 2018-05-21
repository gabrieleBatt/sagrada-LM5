package it.polimi.ingsw.server.controller.channels;

import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.Pool;
import it.polimi.ingsw.server.model.table.RoundTrack;
import it.polimi.ingsw.server.model.table.Table;
import org.junit.jupiter.api.*;

import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class SocketCommunicationChannelTest{


    @DisplayName("Update test")
    @Test
    void update() throws IOException {
        String fileOut = "resources/testResources/socketOut.txt";
        String fileExpected = "resources/testResources/socketExpected.txt";

        Socket socket = new Socket();
        PrintWriter o = new PrintWriter(new FileOutputStream(fileOut));
        SocketCommunicationChannel socketCommunicationChannel= new SocketCommunicationChannel(socket, null, o, "test");
        socketCommunicationChannel.updateView(new Pool());
        List<Player> players = new ArrayList<>();
        players.add(new Player("test"));
        players.add(new Player("test1"));
        socketCommunicationChannel.updateView(players.get(0), true);
        socketCommunicationChannel.updateView(players.get(1), true);
        socketCommunicationChannel.updateView(new RoundTrack());
        socketCommunicationChannel.updateView(new Table(players));
        byte[] f1 = Files.readAllBytes(Paths.get(fileOut));
        byte[] f2 = Files.readAllBytes(Paths.get(fileExpected));
        Assertions.assertTrue(Arrays.equals(f1, f2));
    }

    @AfterEach
    void report(){
    }
}