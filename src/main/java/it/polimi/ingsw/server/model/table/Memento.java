package it.polimi.ingsw.server.model.table;


/**
 * Memento is an interface whose aim is to keep a stack of the objects' state
 * during the game, and offers to the player the possibility to restore a particular
 * game state (typically the one at the beginning of his player).
 */
public interface Memento {

    /**
     * Adds the current table's component state at the stack.
     */
    void addMemento();

    /**
     * Gets the last table's component state saved from the stack.
     */
    void getMemento();

}

