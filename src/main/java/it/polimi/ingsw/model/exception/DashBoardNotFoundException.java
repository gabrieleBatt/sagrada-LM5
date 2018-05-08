package it.polimi.ingsw.model.exception;

/**
 * thrown by server whether there's an attempt to access at a not existing dashboard
 */
public class DashBoardNotFoundException extends Throwable {
    public DashBoardNotFoundException (String message){
        super(message);
    }
}
