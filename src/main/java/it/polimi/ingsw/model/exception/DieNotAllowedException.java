package it.polimi.ingsw.model.exception;

/**
 * thrown by server whether there's a cell restriction's violation, and not ignoring
 */
public class DieNotAllowedException extends Exception {
    public DieNotAllowedException(String message){
        super(message);
    }
}
