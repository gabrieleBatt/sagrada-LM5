package it.polimi.ingsw.shared.socket;

/**
 * Enum of messages used in socket communication
 */
public enum SocketProtocol {
    HEADER(), LOGIN(), RESULT(),
    SELECT_OBJECT(),
    UPDATE(), CONNECTION(),
    UPDATE_PLAYER(), END_GAME(),
    CHOOSE_WINDOW(),
    NICKNAME(), PASSWORD(),
    MESSAGE(),
    SELECT_FROM(),
    SEND(), OPTION(), LEADER_BOARD(),
    CONTAINER(),
    POOL(), ROUND_TRACK(), TOOL(),
    PUBLIC_OBJ(),
    PRIVATE_OBJ(), TOKEN(),
    GLASS_WINDOW(),
    PLAYER();

    /**
     * returns the message name
     * @return the message name
     */
    public String get() {
        return name();
    }
}

