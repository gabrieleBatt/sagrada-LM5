
package it.polimi.ingsw.server.model.exception;
/**
 * thrown by server whether there's an attempt to use, create or look into a dash board which is over/under size
 */
public class IllegalGlassWindowException extends Exception {
    public IllegalGlassWindowException(String message){
        super(message);
    }
}
