package it.polimi.ingsw.shared.identifiables;

import it.polimi.ingsw.shared.Message;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * enum of standard identifiable
 */
public enum  StdId implements Identifiable {
    SKIP(Message.SKIP.name()), UNDO(Message.UNDO.name()), USE_TOOL(Message.USE_TOOL.name()),
    DRAFT(Message.DRAFT.name()),
    TABLE("table"), ROUND_TRACK("roundTrack"), POOL("pool"),
    GLASS_WINDOW("glassWindow"), DICE_BAG("diceBag"),
    ONE("1"),TWO("2"),THREE("3"),FOUR("4"), FIVE("5"), SIX("6");

    final String id;

    StdId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    /**
     * Gets the standard identifiable from its id
     * @param id the id of the standard identifiable to get
     * @return the standard identifiable with the specified id as id
     */
    public static StdId getStdId(String id){
        Optional<StdId> optionalIdentifiable = Arrays
                .stream(StdId.values())
                .filter(i -> i.getId().equals(id))
                .findFirst();
        if(optionalIdentifiable.isPresent())
            return optionalIdentifiable.get();
        else
            throw new NoSuchElementException();
    }
}
