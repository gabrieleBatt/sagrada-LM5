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

public class FakeSocketClient{

    private static final Logger logger = LogMaker.getLogger(FakeSocketClient.class.getName(), Level.ALL);

    public static void main(String args[]) {
        run("player2");
    }

    public static void run(String nickname){
        String hostName = "localhost";
        int portNumber = 50000;
        BufferedReader in = null;
        PrintWriter out= null;
        Scanner scanner = new Scanner(System.in);
        JSONObject received;
        try {
            Socket socket = new Socket(hostName, portNumber);
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(ClientSocketProtocol.LOGIN.build(nickname).toString());
            out.flush();
            while((received = (JSONObject) (new JSONParser()).parse(in.readLine())) != null) {
                System.out.println(received);
                if (received.get("header").equals("login success")){
                    logger.log(Level.FINE, "Login success");
                }else if (received.get("header").equals("chooseWindow")) {
                    System.out.println("Choose among these windows: " + received.get("-w"));
                    out.println(ClientSocketProtocol.CHOOSE_WINDOW.build(scanner.nextLine()).toString());
                    out.flush();
                } else if (received.get("header").equals("selectObject")) {
                    System.out.println("Choose among these : " + received.get("-o"));
                    out.println(ClientSocketProtocol.SELECT_OBJECT.build(scanner.nextLine()).toString());
                    out.flush();
                } else if (received.get("header").equals("selectFrom")) {
                    System.out.println("Choose among these : " + received.get("-o"));
                    out.println(ClientSocketProtocol.SELECT_FROM.build(scanner.nextLine()).toString());
                    out.flush();
                }
            }

        } catch (ParseException | IOException e){
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }
}
