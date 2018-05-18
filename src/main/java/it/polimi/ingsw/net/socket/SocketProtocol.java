package it.polimi.ingsw.net.socket;

/**
 * Enum of messages and flags used in socket communication
 */
public enum SocketProtocol {
    HEADER("header"), LOGIN("login"), RESULT("result"), SELECT_OBJECT("selectObject"),
    UPDATE("update"), CONNECTION("connection"),
    UPDATE_PLAYER("updatePlayer"), END_GAME("endGame"), CHOOSE_WINDOW("chooseWindow"), NICKNAME("nickname"),
    PASSWORD("password"), MESSAGE("message"), SELECT_FROM("selectFrom"),
    SEND("send"), OPTION("option"), LEADER_BOARD("leaderBoard"), CONTAINER("container"),
    POOL("pool"), ROUND_TRACK("roundTrack"), TOOL("tool"), PUBLIC_OBJ("pubObj"),
    PRIVATE_OBJ("prvObj"), TOKEN("token"), GLASS_WINDOW("glasswindow"),
    PLAYER("player");


    private final String id;

    SocketProtocol(String id){
        this.id = id;
    }

    public String get() {
        return id;
    }
}

