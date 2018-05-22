package it.polimi.ingsw.client.view.factory;

import it.polimi.ingsw.client.view.cli.CliConnectionScreen;
import it.polimi.ingsw.client.view.cli.CliEndScreen;
import it.polimi.ingsw.client.view.cli.CliGameScreen;

public class CliViewFactory implements ViewAbstractFactory {

    @Override
    public CliGameScreen makeGameScreen() {
        return new CliGameScreen(System.in, System.out);
    }

    @Override
    public CliConnectionScreen makeConnectionScreen() {

        return new CliConnectionScreen(System.in, System.out);
    }

    @Override
    public CliEndScreen makeEndScreen() {
        return new CliEndScreen();
    }
}


