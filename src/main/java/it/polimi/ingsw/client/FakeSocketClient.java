package it.polimi.ingsw.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class FakeSocketClient {

    public static void main(String args[]) {
        String hostName = "localhost";
        int portNumber = 1101;

        try {
            Socket socket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.println("testS");
            out.flush();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
