package it.polimi.ingsw.client.view.factory;

import it.polimi.ingsw.client.view.cli.CliEndScreen;
import it.polimi.ingsw.client.view.cli.CliGameScreen;
import it.polimi.ingsw.client.view.gui.GuiConnectionScreen;
import it.polimi.ingsw.client.view.gui.GuiEndScreen;
import it.polimi.ingsw.client.view.gui.GuiGameScreen;

public class GuiView implements ViewAbstractFactory {


    @Override
    public GameScreen makeGameScreen() {
        return new GuiGameScreen();
    }

    @Override
    public ConnectionScreen makeConnectionScreen() {
        return new GuiConnectionScreen();
    }

    @Override
    public EndScreen makeEndScreen() { return new CliEndScreen(System.in, System.out);
    }

}
