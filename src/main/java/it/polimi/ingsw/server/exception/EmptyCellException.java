package it.polimi.ingsw.server.exception;

/**
 * thrown by server whether there's an attempt to get a die from an empty cell
 */
public class EmptyCellException extends RuntimeException {
    public EmptyCellException(String message){
        super(message);
    }
}
