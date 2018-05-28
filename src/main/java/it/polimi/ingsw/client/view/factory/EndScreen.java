package it.polimi.ingsw.client.view.factory;

import it.polimi.ingsw.client.view.EndGameInfo;

public abstract class EndScreen {

    public abstract void showRanking(EndGameInfo endGameInfo);

    public abstract boolean playAgain();
}
