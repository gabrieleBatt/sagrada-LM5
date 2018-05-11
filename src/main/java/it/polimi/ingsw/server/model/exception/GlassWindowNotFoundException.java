package it.polimi.ingsw.server.model.exception;

/**
 * thrown by server whether there's an attempt to access at a not existing glassWindow
 */
public class GlassWindowNotFoundException extends Exception {
    public GlassWindowNotFoundException(String message){
        super(message);
    }
}
