package it.polimi.ingsw.net.identifiables;

import it.polimi.ingsw.net.identifiables.Identifiable;

/**
 * enum of standard identifiable
 */
public enum  StdId implements Identifiable {
    SKIP("skip"), UNDO("undo"), USE_TOOL("UsaUnAttrezzo"), DRAFT("PescaUnDado"),
    TABLE("table"), ROUND_TRACK("roundTrack"), POOL("pool"), GLASS_WINDOW("glassWindow"),
    ONE("1"),
    TWO("2"),THREE("3"),FOUR("4"), FIVE("5"), SIX("6");

    final String id;

    StdId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }
}
