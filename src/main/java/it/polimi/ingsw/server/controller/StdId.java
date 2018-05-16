package it.polimi.ingsw.server.controller;

public enum StdId implements Identifiable {
    SKIP("skip"), UNDO("undo"), USE_TOOL("UsaUnAttrezzo"), DRAFT("PescaUnDado"),
    TABLE("table"), ROUND_TRACK("roundTrack"), GLASS_WINDOW("glassWindow");

    final String id;

    StdId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }
}
