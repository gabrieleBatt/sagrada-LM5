package it.polimi.ingsw.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class FakeSocketClient {

    public static void main(String args[]) {
        String hostName = "localhost";
        int portNumber = 1101;

        try {
                Socket socket = new Socket(hostName, portNumber);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
