package it.polimi.ingsw.client.viewFactory;

import it.polimi.ingsw.client.cli.CliConnectionScreen;
import it.polimi.ingsw.client.cli.CliEndScreen;
import it.polimi.ingsw.client.cli.CliGameScreen;

public class CliViewFactory extends ViewAbstractFactory {
    @Override
    public CliGameScreen makeGameScreen() {
        return null;
    }

    @Override
    public CliConnectionScreen makeConnectionScreen() {

        return new CliConnectionScreen(System.in);
    }

    @Override
    public CliEndScreen makeEndScreen() {
        return null;
    }
}


