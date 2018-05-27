package it.polimi.ingsw.net.identifiables;

import it.polimi.ingsw.net.identifiables.Identifiable;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * enum of standard identifiable
 */
public enum  StdId implements Identifiable {
    SKIP("skip"), UNDO("undo"), USE_TOOL("useTool"),
    DRAFT("draftDice"),
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
