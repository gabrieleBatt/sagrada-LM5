package it.polimi.ingsw.server.exception;

/**
 * thrown by server whether there's an attempt to draw a die from a empty dice bag
 */
public class BagEmptyException extends RuntimeException {
    public BagEmptyException(String message){
        super(message);
    }
}
