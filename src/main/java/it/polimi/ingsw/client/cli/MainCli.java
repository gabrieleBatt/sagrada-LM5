package it.polimi.ingsw.client.cli;

public class MainCli {
    public static void main(String args[]) {
        System.out.println(new CliConnectionScreen(System.in).getConnectionInfo());
    }
}
