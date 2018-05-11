package it.polimi.ingsw.server.model.exception;

/**
 * thrown by server whether there's an attempt to put a parameter or some parameters which are not allowed
 */
public class NotValidNumberException extends Exception {
    public NotValidNumberException(String message){
        super(message);
    }
}
