package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.exception.DeckTooSmallException;

public interface Deck {

    Object draw(int num) throws DeckTooSmallException;

}
