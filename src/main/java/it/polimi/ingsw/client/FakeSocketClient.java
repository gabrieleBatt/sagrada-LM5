package it.polimi.ingsw.client;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.net.socket.SocketProtocol;
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
        run("player1");
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
            out.println("{\"header\":\"login\", \"nickname\":\""+nickname+"\"}");
            out.flush();
            while((received = (JSONObject) (new JSONParser()).parse(in.readLine())) != null) {
                System.out.println(received);
                if (received.get("header").equals(SocketProtocol.LOGIN.get()) && received.get(SocketProtocol.RESULT.get()).equals("success")){
                    logger.log(Level.FINE, "Login success");
                }else if (received.get("header").equals(SocketProtocol.CHOOSE_WINDOW.get())) {
                    System.out.println("Choose among these windows: " + received.get(SocketProtocol.GLASS_WINDOW.get()));
                    out.println("{\"header\":\"chooseWindow\"," +
                            "\"glassWindow\":\""+scanner.nextLine()+"\"}");
                    out.flush();
                } else if (received.get("header").equals(SocketProtocol.SELECT_OBJECT.get())) {
                    System.out.println("Choose among these : " + received.get(SocketProtocol.OPTION.get()));
                    out.println("{\"header\": \"selectObject\",\"option\":\""+scanner.nextLine()+"\"}");
                    out.flush();
                } else if (received.get("header").equals(SocketProtocol.SELECT_FROM.get())) {
                    System.out.println("Choose among these : " + received.get(SocketProtocol.OPTION.get()));
                    out.println("{\"header\": \"selectFrom\",\"option\":\""+scanner.nextLine()+"\"}");
                    out.flush();
                }
            }

        } catch (ParseException | IOException e){
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }
}
