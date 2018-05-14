package it.polimi.ingsw.client;

import it.polimi.ingsw.LogMaker;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FakeSocketClient {

    private static final Logger logger = LogMaker.getLogger(FakeSocketClient.class.getName(), Level.ALL);
    private static final String nickname = "Giampi";
    public static void main(String args[]) {
        String hostName = "localhost";
        int portNumber = 1101;
        BufferedReader in = null;
        PrintWriter out= null;
        Scanner scanner = new Scanner(System.in);
        String received;

        try (Socket socket = new Socket(hostName, portNumber)) {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.println("login "+nickname);
            out.flush();
            while((received = in.readLine()) != null) {
                List<String> streamList = Stream.of(received.split(" ")).map(String::new).filter(x -> !x.equals("")).collect(Collectors.toList());
                if (received.equals(nickname+ " logged!")){
                    logger.log(Level.FINE, "Login success");
                }else if (streamList.get(0).equals("chooseWindow")) {
                    System.out.println("Choose among these windows: " + streamList.subList(1, streamList.size()));
                    System.out.println("Type the displayed name of your choice only ");
                    out.println("windowChosen " + scanner.nextLine());
                    out.flush();
                } else if (streamList.get(0).equals("selectObject")) {
                    System.out.println("Choose among these : " + streamList.subList(2, streamList.size()));
                    System.out.println("Type the displayed name of your choice only ");
                    out.println("optionSelected " + scanner.nextLine());
                    out.flush();
                } else if (streamList.get(0).equals("selectFrom")) {
                    System.out.println("Choose among these : " + streamList.subList(2, streamList.size()));
                    System.out.println("Type the displayed name of your choice only ");
                    out.println("selected " + scanner.nextLine());
                    out.flush();
                }
            }

        } catch (IOException e){
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }
}
