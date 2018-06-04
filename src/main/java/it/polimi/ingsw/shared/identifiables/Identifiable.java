package it.polimi.ingsw.shared.identifiables;

/**
 * An object identifiable by an id, used for communication between server and client
 */
public interface Identifiable {

    /**
     * returns the identifiable unique id
     * @return the id
     */
    String getId();
}
