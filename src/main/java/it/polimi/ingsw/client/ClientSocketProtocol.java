package it.polimi.ingsw.client;

import org.json.simple.JSONObject;

public enum ClientSocketProtocol{
    LOGIN("login"), SELECT_OBJECT("objectSelected"),
    CHOOSE_WINDOW("windowChosen"), SELECT_FROM("selected");

    private final String id;

    ClientSocketProtocol(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public JSONObject build(String param) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("header", this.getId());
        jsonObject.put("mainParam", param);
        return jsonObject;
    }
}

