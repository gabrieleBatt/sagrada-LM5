package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.Identifiable;

public enum StdId implements Identifiable {
    SKIP("skip"), UNDO("undo"), USE_TOOL("UsaUnTool"), DRAFT("PescaUnDado");

    final String id;

    StdId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }
}
