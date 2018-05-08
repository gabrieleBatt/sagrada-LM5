package it.polimi.ingsw.model.exception;


/**
 * Thrown if requested too many cards from a deck
 */
public class DeckTooSmallException extends Exception {

    public DeckTooSmallException(String message){
        super(message);
    }

}
