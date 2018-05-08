package it.polimi.ingsw.model.exception;

/**
 * thrown by server whether there's an attempt to look into an objective having illegal parameter
 */
public class IllegalObjectiveException extends Exception {
    public IllegalObjectiveException(String message){
        super(message);
    }
}
