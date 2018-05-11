package it.polimi.ingsw.server.model.exception;

/**
 * thrown by server whether there's an attempt to increase the turn count over the roundTrack limit
 */
public class EndGameException extends Exception {
    public EndGameException(String message){
        super(message);
    }

}
