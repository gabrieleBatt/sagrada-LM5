package it.polimi.ingsw.client;

import it.polimi.ingsw.LogMaker;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FakeSocketClient {

    private static final Logger logger = LogMaker.getLogger(FakeSocketClient.class.getName(), Level.ALL);
    private static final String nickname = "Berna";

    public static void main(String args[]) {
        String hostName = "localhost";
        int portNumber = 50000;
        ObjectInputStream in = null;
        ObjectOutputStream out= null;
        Scanner scanner = new Scanner(System.in);
        JSONObject received;
        try {
            Socket socket = new Socket(hostName, portNumber);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(ClientSocketProtocol.LOGIN.build(nickname).toString());
            out.flush();
            while((received = (JSONObject) (new JSONParser()).parse((String) in.readObject())) != null) {
                System.out.println(received);
                if (received.get("header").equals("login success")){
                    logger.log(Level.FINE, "Login success");
                }else if (received.get("header").equals("chooseWindow")) {
                    System.out.println("Choose among these windows: " + received.get("-w"));
                    out.writeObject(ClientSocketProtocol.CHOOSE_WINDOW.build(scanner.nextLine()).toString());
                    out.flush();
                } else if (received.get("header").equals("selectObject")) {
                    System.out.println("Choose among these : " + received.get("-o"));
                    out.writeObject(ClientSocketProtocol.SELECT_OBJECT.build(scanner.nextLine()).toString());
                    out.flush();
                } else if (received.get("header").equals("selectFrom")) {
                    System.out.println("Choose among these : " + received.get("-o"));
                    out.writeObject(ClientSocketProtocol.SELECT_FROM.build(scanner.nextLine()).toString());
                    out.flush();
                }
            }

        } catch (ClassNotFoundException | ParseException | IOException e){
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }
}
