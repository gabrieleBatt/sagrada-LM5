package it.polimi.ingsw.server.model.exception;

/**
 * thrown if a json file is not in the expected format
 */
public class InvalidJSONException extends Exception {
    public InvalidJSONException(String s) {
        super(s);
    }
}
