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
import java.util.Scanner;

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

        Scanner scanner1 = new Scanner(new FileInputStream(fileOut));
        Scanner scanner2 = new Scanner(new FileInputStream(fileExpected));
        while(scanner1.hasNext()){
            Assertions.assertEquals(scanner1.next(), scanner2.next());
        }
    }
}