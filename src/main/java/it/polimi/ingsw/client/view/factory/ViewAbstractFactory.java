package it.polimi.ingsw.client.view.factory;

/**
 * Abstract factory for different view modes
 */
public interface ViewAbstractFactory {

    /**
     * Returns a new GameScreen
     * @return a new GameScreen
     */
    GameScreen makeGameScreen ();

    /**
     * Returns a new ConnectionScreen
     * @return a new ConnectionScreen
     */
    ConnectionScreen makeConnectionScreen ();

    /**
     * Returns a new EndScreen
     * @return a new EndScreen
     */
    EndScreen makeEndScreen();
}
