
package it.polimi.ingsw.model.exception;
/**
 * thrown by server whether there's an attempt to use, create or look into a dash board which is over/under size
 */
public class IllegalDashboardException extends Exception {
    public IllegalDashboardException(String message){
        super(message);
    }
}
