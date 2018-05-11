package it.polimi.ingsw.server.model.exception;

/**
 * thrown by server whether there's an attempt to found a cell which doesn't exist
 */

public class CellNotFoundException extends Exception {

    public CellNotFoundException(String message){
        super(message);
    }

}
