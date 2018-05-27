package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.LoginInfo;
import it.polimi.ingsw.client.view.Message;
import it.polimi.ingsw.client.view.factory.ConnectionScreen;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CliConnectionScreen implements ConnectionScreen {

    private final Scanner scanner;
    private final PrintStream printStream;
    boolean flag;
    public CliConnectionScreen(InputStream inputStream, PrintStream printStream){
        scanner = new Scanner(inputStream);
        this.printStream = printStream;
    }

    @Override
    public LoginInfo getConnectionInfo () {

        if (getConnectionChoiceFromUser().equalsIgnoreCase("rmi")){
            getNicknameChoiceFromUser();
            getPasswordFromUser();
            return new LoginInfo("rmi",
                    getNicknameChoiceFromUser(),
                    0,
                    null,
                    getPasswordFromUser());
        }
        return new LoginInfo("socket",
                getNicknameChoiceFromUser(),
                getPortNumberChoiceFromUser(),
                getIpFromUser(),
                getPasswordFromUser());
    }

    private String getConnectionChoiceFromUser(){
        clearScreen();
        printStream.println(Message.CHOOSE_CONNECTION+": \n" +
                "**RMI**  **Socket** : \n");

        String choice = scanner.nextLine();

        while (!(choice.equalsIgnoreCase("RMI") || choice.equalsIgnoreCase("R") ||
                choice.equalsIgnoreCase("Socket") || choice.equalsIgnoreCase("S") )){
            clearScreen();
            printStream.println(Message.INVALID_CHOICE+"\n" +
                    "**RMI**  **Socket** :  \n");

            choice = scanner.nextLine();
        }
        if (choice.toUpperCase().contains("R")) {
            flag = true;
            return "rmi";
        } else
            return "socket";

    }

    private String getNicknameChoiceFromUser (){
        clearScreen();
        printStream.println(Message.CHOOSE_NICKNAME+ "\n" +
                        Message.CREDENTIAL_POLICY +"\n");

        String choice = scanner.nextLine();
        while (isValid(choice)) {
            clearScreen();
            printStream.println(Message.INVALID_CHOICE+"\n" +
                            Message.CREDENTIAL_POLICY+ "\n");

            choice = scanner.nextLine();
        }
        clearScreen();
        return choice;
    }

    private boolean isValid(String s){
        return s.contains(" ") || s.isEmpty();
    }

    private String getPasswordFromUser(){
        clearScreen();
        printStream.println(Message.INSERT_PASSWORD+ "\n" +
                Message.CREDENTIAL_POLICY +"\n");

        String choice = scanner.nextLine();
        while (isValid(choice)) {
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
        for (int i = 0; i < 20; i++) {
            printStream.println();
        }
    }

}

