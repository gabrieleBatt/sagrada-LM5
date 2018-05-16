package it.polimi.ingsw.server.controller.commChannel.socket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

public enum ServerSocketProtocol{
    LOGIN("login"), SELECT_OBJECT("selectObject"), UPDATE("update"),
    END_GAME("endGame"), CHOOSE_WINDOW("chooseWindow"), SELECT_FROM("selectFrom"),
    OPTION("-o"), LEADER_BOARD("-l"),
    POOL("-p"), ROUND_TRACK("-rt"), TOOL("-t"), PUBLIC_OBJ("-pub"),
    PRIVATE_OBJ("-prv"), TOKEN("-t"), GLASS_WINDOW("-w"), PLAYER("-pl");


    private final String id;

    ServerSocketProtocol(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

class JSONBuilder{

    private JSONObject jsonObject;

    public JSONBuilder(){
        jsonObject = new JSONObject();
    }

    public JSONObject get() {
        return jsonObject;
    }

    public JSONBuilder build(ServerSocketProtocol message, String param) {
        if(jsonObject.get("header") == null) {
            this.jsonObject.put("header", message.getId());
            this.jsonObject.put("mainParam", param);
        }else{
            this.jsonObject.put(message.getId(), param);
        }
        return this;
    }

    public JSONBuilder build(ServerSocketProtocol message) {
        this.jsonObject.put("header", message.getId());
        return this;
    }

    public JSONBuilder build(ServerSocketProtocol message, List<String> param) {
        JSONArray jsonArray = new JSONArray();
        for (String s : param) {
            jsonArray.add(s);
        }
        this.jsonObject.put(message.getId(), jsonArray);
        return this;
    }
}
