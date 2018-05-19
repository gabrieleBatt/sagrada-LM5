package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.LoginInfo;
import it.polimi.ingsw.client.view.factory.ConnectionScreen;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CliConnectionScreen implements ConnectionScreen {

    private Scanner scanner;
    boolean flag;
    public CliConnectionScreen(InputStream inputStream){
        scanner = new Scanner(inputStream);
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
        System.out.println("Scegli il tuo tipo di connessione: \n" +
                "**RMI**  **Socket** : \n");

        String choice = scanner.nextLine();

        while (!(choice.equalsIgnoreCase("RMI") || choice.equalsIgnoreCase("R") ||
                choice.equalsIgnoreCase("Socket") || choice.equalsIgnoreCase("S") )){
            clearScreen();
            System.out.println("\nLa tua scelta non è valida!\n" +
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
        System.out.println("Ora scegli il tuo nickname! \n" +
                "Il nickname non deve contenere alcuno spazio \n");

        String choice = scanner.nextLine();
        while (isValid(choice)) {
            clearScreen();
            System.out.println("\nIl tuo nickname non è valido! Ricorda che non può contenere spazi\n" +
                    "Inserisci nuovamente il tuo nickname  \n");

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
        System.out.println("\nOra inserisci la tua password! \n" +
                "La tua password non deve contenere alcuno spazio \n");

        String choice = scanner.nextLine();
        while (isValid(choice)) {
            clearScreen();
            System.out.println("\nLa tua password non è valida! Ricorda che non può contenere spazi  \n" +
                    "Inserisci nuovamente la tua password  \n");

            choice = scanner.nextLine();
        }
        clearScreen();
        return choice;

    }

    private int getPortNumberChoiceFromUser() {
        clearScreen();
        System.out.println("Inserisci il numero di porta \n");

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
                System.out.println("Inserisci un numero di porta valido! \n");
            }
        }
        clearScreen();
        return port;

    }

    private String getIpFromUser() {
        clearScreen();
        System.out.println("Inserisci un indirizzo IP:  \n");

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
                System.out.println("Ricorda di inserire un IP valido! \n");
            }

        }while (!valid);
        clearScreen();
        return ip;
    }

    private void clearScreen() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }

}

