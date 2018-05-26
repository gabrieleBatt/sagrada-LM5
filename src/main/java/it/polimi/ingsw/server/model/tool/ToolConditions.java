package it.polimi.ingsw.server.model.tool;

import it.polimi.ingsw.net.identifiables.StdId;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Enumeration of using condition for tools.
 */
public enum ToolConditions {
    CANCEL_DRAFTING("cancelDrafting"),
    BEFORE_DRAFTING("beforeDrafting"),
    SECOND_TURN("secondTurn"),
    FIRST_TURN("firstTurn"),
    WITH_DRAFTING("withDrafting");

    private String toString;

    /**
     * Tool condition builder.
     * @param toString Tool condition.
     */
    ToolConditions(String toString){
        this.toString = toString;
    }

    /**
     * Returns tool condition.
     * @return String, tool condition.
     */
    @Override
    public String toString() {
        return toString;
    }

    public static ToolConditions getCondition(String id){
        Optional<ToolConditions> optionalIdentifiable = Arrays
                .stream(ToolConditions.values())
                .filter(i -> i.toString().equals(id))
                .findFirst();
        if(optionalIdentifiable.isPresent())
            return optionalIdentifiable.get();
        else
            throw new NoSuchElementException();
    }
}
