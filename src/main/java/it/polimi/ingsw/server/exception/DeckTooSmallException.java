package it.polimi.ingsw.server.exception;


/**
 * Thrown if requested too many cards from a deck
 */
public class DeckTooSmallException extends RuntimeException {

    public DeckTooSmallException(String message){
        super(message);
    }

}
