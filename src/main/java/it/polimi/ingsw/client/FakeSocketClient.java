package it.polimi.ingsw.client;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.net.socket.SocketProtocol;
import org.json.simple.JSONArray;
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

    public static void main(String[] args) {
        run("player1", false);
    }

    public static void run(String nickname, boolean real){
        String hostName = "localhost";
        int portNumber = 50000;
        BufferedReader in = null;
        PrintWriter out= null;
        Scanner scanner = new Scanner(System.in);
        JSONObject received;
        try (Socket socket = new Socket(hostName, portNumber)){
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            logger.log(Level.FINE, "{\"header\":\"login\", \"nickname\":\""+nickname+"\", \"password\":\"password\"}");
            out.println("{\"header\":\"login\", \"nickname\":\""+nickname+"\", \"password\":\"password\"}");
            out.flush();
            while((received = (JSONObject) (new JSONParser()).parse(in.readLine())) != null) {
                logger.log(Level.FINE, received.toString());
                if (received.get("header").equals(SocketProtocol.LOGIN.get())){
                    if (received.get(SocketProtocol.RESULT.get()).equals("success")) {
                        logger.log(Level.FINE, "Login success");
                    }else{
                        logger.log(Level.FINE, received.get(SocketProtocol.RESULT.get()).toString());
                    }
                }else if (received.get("header").equals(SocketProtocol.CHOOSE_WINDOW.get())) {
                    System.out.println("Choose among these windows: " + received.get(SocketProtocol.GLASS_WINDOW.get()));
                    logger.log(Level.FINE, "{\"header\":\"chooseWindow\"," +
                            "\"glasswindow\":\""+ ((JSONArray)received.get(SocketProtocol.GLASS_WINDOW.get())).get(0)+"\"}");
                    if(!real)
                        out.println("{\"header\":\"chooseWindow\"," +
                            "\"glasswindow\":\""+ ((JSONArray)received.get(SocketProtocol.GLASS_WINDOW.get())).get(0)+"\"}");
                    else
                        out.println("{\"header\":\"chooseWindow\"," +
                            "\"glasswindow\":\""+scanner.nextLine()+"\"}");
                    out.flush();
                } else if (received.get("header").equals(SocketProtocol.SELECT_OBJECT.get())) {
                    System.out.println("Choose among these : " + received.get(SocketProtocol.OPTION.get()));
                    logger.log(Level.FINE, "{\"header\": \"selectObject\",\"option\":\"skip\"}");
                    if(!real)
                        out.println("{\"header\": \"selectObject\",\"option\":\"skip\"}");
                    else
                        out.println("{\"header\": \"selectObject\",\"option\":\""+scanner.nextLine()+"\"}");
                    out.flush();
                } else if (received.get("header").equals(SocketProtocol.SELECT_FROM.get())) {
                    System.out.println("Choose among these : " + received.get(SocketProtocol.OPTION.get()));
                    logger.log(Level.FINE, "{\"header\": \"selectFrom\",\"option\":\"skip\"}");
                    if(!real)
                        out.println("{\"header\": \"selectFrom\",\"option\":\"skip\"}");
                    else
                        out.println("{\"header\": \"selectFrom\",\"option\":\""+scanner.nextLine()+"\"}");
                    out.flush();
                }
            }

        } catch (ParseException | IOException e){
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }
}
