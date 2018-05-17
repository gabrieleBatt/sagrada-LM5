package it.polimi.ingsw.client.view.factory;


public interface ViewAbstractFactory {

    GameScreen makeGameScreen ();

    ConnectionScreen makeConnectionScreen ();

    EndScreen makeEndScreen();
}
