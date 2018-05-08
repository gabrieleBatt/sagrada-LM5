package it.polimi.ingsw.model.exception;

/**
 * thrown by server whether there's an attempt to draw a die from a empty dice bag
 */
public class BagEmptyException extends Exception {
    public BagEmptyException(String message){
        super(message);
    }
    public BagEmptyException(){
    }
}
