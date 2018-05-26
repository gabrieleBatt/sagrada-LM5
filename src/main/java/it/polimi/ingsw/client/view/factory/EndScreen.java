package it.polimi.ingsw.client.view.factory;

import it.polimi.ingsw.client.view.EndGameInfo;

public interface EndScreen {

    public void showRanking(EndGameInfo endGameInfo);

    public boolean playAgain();
}
