package it.polimi.ingsw.client.view.cli;

public class MainCli {
    public static void main(String[] args) {
        System.console().writer().println(new CliConnectionScreen(System.in).getConnectionInfo());
    }
}
