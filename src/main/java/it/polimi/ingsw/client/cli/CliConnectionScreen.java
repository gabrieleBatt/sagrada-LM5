package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.LoginInfo;
import it.polimi.ingsw.client.viewFactory.ConnectionScreen;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CliConnectionScreen extends ConnectionScreen {

    private Scanner scanner;

    public CliConnectionScreen(InputStream inputStream){
        scanner = new Scanner(inputStream);
    }

    @Override
    public LoginInfo getConnectionInfo () {
        LoginInfo ret = new LoginInfo();
        ret.connectionType = getConnectionChoiceFromUser();
        ret.nickname = getNicknameChoiceFromUser();
        ret.password = getPasswordFromUser();
        ret.portNumber = getPortNumberChoiceFromUser();
        ret.IP = getIpFromUser();
        return ret;
    }

    private String getConnectionChoiceFromUser(){
        clearScreen();
        System.out.println("Scegli il tuo tipo di connessione: \n");
        System.out.println("**RMI**  **Socket** : \n");
        System.out.println("\n \n \n ");

        String choice = scanner.nextLine();

        while (!(choice.toString().equals("RMI") || choice.toString().equals("R") || choice.toString().equals("r") || choice.toString().equals("rmi") ||
                choice.toString().equals("Socket") || choice.toString().equals("s") || choice.toString().equals("S") || choice.toString().equals("socket"))){
            clearScreen();
            System.out.println("\nLa tua scelta non è valida!");
            System.out.println("Ora imposta correttamente la tua connessione  \n");
            System.out.println("\n \n \n ");

            choice = scanner.nextLine();
        }
        if (choice.toString().equals("RMI") || choice.toString().equals("r") || choice.toString().equals("R") || choice.toString().equals("rmi")) {
            return "rmi";
        } else //if (choice.toString() == "Socket" || choice.toString() == "s" || choice.toString() == "S" || choice.toString() == "socket")
            return "socket";

    }

    private String getNicknameChoiceFromUser (){
        clearScreen();
        System.out.println("\nOra scegli il tuo nickname! \n");
        System.out.println("\nIl tuo nickname non dovrà contenere alcuno spazio \n");
        System.out.println("\n \n \n ");

        String choice = scanner.nextLine();
        while (choice.toString().contains(" ") || choice.toString().isEmpty()) {
            clearScreen();
            System.out.println("\nIl tuo nickname non è valido! Ricorda che non può contenere spazi  \n");
            System.out.println("\nInserisci nuovamente il tuo nickname  \n");
            System.out.println("\n \n \n ");

            choice = scanner.nextLine();
        }
        clearScreen();
        return choice.toString();
    }

    private String getPasswordFromUser(){
        clearScreen();
        System.out.println("\nOra inserisci la tua password! \n");
        System.out.println("\nLa tua password non dovrà contenere alcuno spazio \n");
        System.out.println("\n \n \n ");

        String choice = scanner.nextLine();
        while (choice.toString().contains(" ") || choice.toString().isEmpty()) {
            clearScreen();
            System.out.println("\nLa tua password non è valida! Ricorda che non può contenere spazi  \n");
            System.out.println("\nInserisci nuovamente la tua password  \n");
            System.out.println("\n \n \n ");

            choice = scanner.nextLine();
        }
        clearScreen();
        return choice.toString();

    }

    private int getPortNumberChoiceFromUser() {
        clearScreen();
        System.out.println("\nQual è il numero di porta?  \n");
        System.out.println("\n \n \n ");

        String choice;
        boolean valid = false;
        int port = 0;
        while (!valid){
            choice = scanner.nextLine();
            try {
                port = Integer.parseInt(choice);
                //portNumber = port;
                valid = true;
            } catch (NumberFormatException e) {
                clearScreen();
                System.out.println("\nRicorda di inserire un numero di porta valido! \n");
                System.out.println("\n \n \n ");

            }
        }
        clearScreen();
        return port;

    }

    private String getIpFromUser() {
        clearScreen();
        System.out.println("\nQual è l'IP?  \n");
        System.out.println("\n \n \n ");

        String choice;
        boolean valid = false;
        boolean numeric = false;
        while (!valid){
            int i;
            String s = scanner.nextLine();
            List<String> ip = Arrays.stream(s
                    .split("\\."))
                    .collect(Collectors.toList());
            for (String str: ip){
                try {
                     i = Integer.parseInt(str);
                     numeric= true;
                }
                catch (NumberFormatException e){
                    numeric = false;
                    break;
                }
            }
            if (ip.size() == 4 && numeric==true){
                valid = true;
            }else{
                clearScreen();
                System.out.println("\nRicorda di inserire un IP valido! \n");
                System.out.println("\n \n \n ");

            }

        }
        clearScreen();
        return "lwl";
    }

    private void clearScreen() {
        for (int i = 0; i < 0; i++) {
            System.out.println();
        }
    }

}

