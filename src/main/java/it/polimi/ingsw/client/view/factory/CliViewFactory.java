package it.polimi.ingsw.client.view.factory;

import it.polimi.ingsw.client.view.cli.CliConnectionScreen;
import it.polimi.ingsw.client.view.cli.CliEndScreen;
import it.polimi.ingsw.client.view.cli.CliGameScreen;

public class CliViewFactory implements ViewAbstractFactory {

    @Override
    public CliGameScreen makeGameScreen() {
        return new CliGameScreen();
    }

    @Override
    public CliConnectionScreen makeConnectionScreen() {

        return new CliConnectionScreen(System.in);
    }

    @Override
    public CliEndScreen makeEndScreen() {
        return new CliEndScreen();
    }
}


