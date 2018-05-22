package it.polimi.ingsw.client.view.factory;

public class GuiViewFactory implements ViewAbstractFactory {


    @Override
    public GameScreen makeGameScreen() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConnectionScreen makeConnectionScreen() {
        throw new UnsupportedOperationException();
    }

    @Override
    public EndScreen makeEndScreen() {
        throw new UnsupportedOperationException();
    }
}
