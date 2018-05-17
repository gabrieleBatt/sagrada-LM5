package it.polimi.ingsw.client;

import org.json.simple.JSONObject;

public enum ClientSocketProtocol{
    LOGIN("login"), RESULT("result"), SELECT_OBJECT("selectObject"), UPDATE("update"),
    UPDATE_PLAYER("updatePlayer"), END_GAME("endGame"), CHOOSE_WINDOW("chooseWindow"), NICKNAME("nickname"),
    PASSWORD("password"), MESSAGE("message"), SELECT_FROM("selectFrom"),
    SEND("send"), OPTION("option"), LEADER_BOARD("leaderBoard"), CONTAINER("container"),
    POOL("pool"), ROUND_TRACK("roundTrack"), TOOL("tool"), PUBLIC_OBJ("pubObj"),
    PRIVATE_OBJ("prvObj"), TOKEN("token"), GLASS_WINDOW("glassWindow"),
    PLAYER("player");


    private final String id;

    ClientSocketProtocol(String id){
        this.id = id;
    }

    public String get() {
        return id;
    }
}

