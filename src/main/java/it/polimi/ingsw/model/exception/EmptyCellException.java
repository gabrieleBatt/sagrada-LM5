package it.polimi.ingsw.model.exception;

/**
 * thrown by server whether there's an attempt to get a die from an empty cell
 */
public class EmptyCellException extends Exception {
    public EmptyCellException(String message){
        super(message);
    }
}
