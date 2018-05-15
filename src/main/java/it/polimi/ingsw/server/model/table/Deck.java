package it.polimi.ingsw.server.model.table;

import it.polimi.ingsw.server.exception.DeckTooSmallException;


/**
 * Contains always all possible cards, if cards are drawn they do not disappear from deck
 */
public interface Deck {

    /**
     * draws the specified amount of different cards from deck
     * @param num - number of cards in deck to draw
     * @return cards drawn
     * @throws DeckTooSmallException is thrown if there are not enough cards in the deck
     */
    Object draw(int num);

}
