package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.LoginInfo;
import it.polimi.ingsw.shared.Message;
import it.polimi.ingsw.client.view.factory.ConnectionScreen;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CliConnectionScreen extends ConnectionScreen {

    private static final int MAX_LEN_CREDENTIAL = 12;
    private static final int CLEAR_SPACE = 20;
    private static final String RMI = "rmi";
    private static final String SOCKET = "socket";
    private final Scanner scanner;
    private final PrintStream printStream;
    private Optional<LoginInfo> loginInfo;

    public CliConnectionScreen(InputStream in, PrintStream out){
        this.scanner = new Scanner(in);
        this.printStream = out;
        loginInfo = Optional.empty();
    }

    @Override
    public LoginInfo getConnectionInfo () {
        if(loginInfo.isPresent()){
            return loginInfo.get();
        }else {
            loginInfo = Optional.of(new LoginInfo(getConnectionChoiceFromUser(),
                    getNicknameChoiceFromUser(),
                    getPortNumberChoiceFromUser(),
                    getIpFromUser(),
                    getPasswordFromUser()));
        }
        return loginInfo.get();
    }

    @Override
    public boolean reConnect() {
        printStream.println(Message.RE_CONNECT+"? y/n");
        return scanner.nextLine().equalsIgnoreCase("y");
    }

    private String getConnectionChoiceFromUser(){
        clearScreen();
        printStream.println(Message.CHOOSE_CONNECTION+": \n" +
                "**RMI**  **Socket** : \n");

        String choice = scanner.nextLine();

        while (!(choice.equalsIgnoreCase(RMI) || choice.equalsIgnoreCase("R") ||
                choice.equalsIgnoreCase(SOCKET) || choice.equalsIgnoreCase("S") )){
            clearScreen();
            printStream.println(Message.INVALID_CHOICE+"\n" +
                    "**"+RMI+"**  **"+SOCKET+"** :  \n");

            choice = scanner.nextLine();
        }
        if (choice.toUpperCase().contains("R")) {
            return RMI;
        } else
            return SOCKET;

    }

    private String getNicknameChoiceFromUser (){
        clearScreen();
        printStream.println(Message.CHOOSE_NICKNAME+ "\n" +
                        Message.CREDENTIAL_POLICY +"\n");

        String choice = scanner.nextLine();
        while (notValid(choice)) {
            clearScreen();
            printStream.println(Message.INVALID_CHOICE+"\n" +
                            Message.CREDENTIAL_POLICY+ "\n");

            choice = scanner.nextLine();
        }
        clearScreen();
        return choice;
    }

    private boolean notValid(String s){
        return s.contains(" ") || s.isEmpty() || s.length() > MAX_LEN_CREDENTIAL;
    }

    private String getPasswordFromUser(){
        clearScreen();
        printStream.println(Message.INSERT_PASSWORD+ "\n" +
                Message.CREDENTIAL_POLICY +"\n");

        String choice = scanner.nextLine();
        while (notValid(choice)) {
            clearScreen();
            printStream.println(Message.INVALID_CHOICE+"\n" +
                    Message.CREDENTIAL_POLICY+ "\n");

            choice = scanner.nextLine();
        }
        clearScreen();
        return choice;

    }

    private int getPortNumberChoiceFromUser() {
        clearScreen();
        printStream.println(Message.PORT_NUMBER+"\n");

        String choice;
        boolean valid = false;
        int port = 0;
        while (!valid){
            choice = scanner.nextLine();
            try {
                port = Integer.parseInt(choice);
                valid = true;
            } catch (NumberFormatException e) {
                clearScreen();
                printStream.println(Message.INVALID_CHOICE+"\n");
            }
        }
        clearScreen();
        return port;

    }

    private String getIpFromUser() {
        clearScreen();
        printStream.println(Message.IP_NUMBER+"\n");

        boolean valid = false;
        String ip;
        do{
            ip = scanner.nextLine();
            List<String> numbers = Arrays.stream(ip
                    .split("\\."))
                    .collect(Collectors.toList());
            if(numbers.stream().allMatch(z -> z.matches("\\d+")) && numbers.size() == 4){
                valid = true;
            }else{
                clearScreen();
                printStream.println(Message.INVALID_CHOICE+"\n");
            }

        }while (!valid);
        clearScreen();
        return ip;
    }

    private void clearScreen() {
        for (int i = 0; i < CLEAR_SPACE; i++) {
            printStream.println();
        }
    }

}

