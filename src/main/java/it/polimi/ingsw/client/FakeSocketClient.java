package it.polimi.ingsw.client;

import it.polimi.ingsw.LogMaker;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FakeSocketClient {

    private static final Logger logger = LogMaker.getLogger(FakeSocketClient.class.getName(), Level.ALL);

    public static void main(String args[]) {
        String hostName = "localhost";
        int portNumber = 1101;
        BufferedReader in = null;
        PrintWriter out= null;

        String command;

        /*try {
            Socket socket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("login anna");
            out.flush();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        try (Socket socket = new Socket(hostName, portNumber)) {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.println("login Anna");
            out.flush();
            String received;
            while((received = in.readLine()) != null) {
                if (received.equals("Anna logged!")) {
                    while (true) {
                        command = in.readLine();
                        List<String> streamList = Stream.of(command.split(" ")).map(String::new).filter(x -> !x.equals("")).collect(Collectors.toList());
                        if (streamList.get(0).equals("chooseWindow")) {
                            List windowNameList = streamList.subList(1, streamList.size());
                            out.println("windowChosen " + windowNameList.get(ThreadLocalRandom.current().nextInt(0, windowNameList.size())));
                            out.flush();
                        } else if (streamList.get(0).equals("selectObject")) {
                            out.println("optionSelected " + streamList
                                    .get(ThreadLocalRandom.current().nextInt(2, streamList.size())));
                            out.flush();
                        } else if (streamList.get(0).equals("selectFrom")) {
                            out.println("selected " + streamList
                                    .get(ThreadLocalRandom.current().nextInt(2, streamList.size())));
                            out.flush();
                        }
                    }
                } else {
                    logger.log(Level.WARNING, "Login failed");
                }
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }

    }
}
