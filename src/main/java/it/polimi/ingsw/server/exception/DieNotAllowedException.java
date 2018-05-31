package it.polimi.ingsw.server.exception;

/**
 * thrown by server whether there's a cell restriction's violation, and not ignoring
 */
public class DieNotAllowedException extends RuntimeException {
    public DieNotAllowedException(String message){
        super(message);
    }
}
