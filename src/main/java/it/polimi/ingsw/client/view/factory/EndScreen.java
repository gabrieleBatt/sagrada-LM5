package it.polimi.ingsw.client.view.factory;

import it.polimi.ingsw.client.view.EndGameInfo;


/**
 * Handles showing the game results to the user
 */
public abstract class EndScreen {

    /**
     * Shows the end game results to the user
     */
    public abstract void showRanking(EndGameInfo endGameInfo);

    /**
     * return true if the user wants to play again
     * @return true if the user wants to play again
     */
    public abstract boolean playAgain();
}
