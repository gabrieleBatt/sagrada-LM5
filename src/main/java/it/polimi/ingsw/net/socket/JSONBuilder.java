package it.polimi.ingsw.net.socket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

/**
 * Builder of json objects used for socket communication
 */
public class JSONBuilder{

    private JSONObject jsonObject;

    public JSONBuilder(){
        jsonObject = new JSONObject();
    }

    /**
     * Gets the JSONObject built
     * @return the JSONObject built
     */
    public JSONObject get() {
        return jsonObject;
    }

    /**
     * it creates a une parameter field
     * @param message type of field
     * @param param content of the field
     * @return this
     */
    public JSONBuilder build(SocketProtocol message, String param) {
        this.jsonObject.put(message.get(), param);
        return this;
    }

    /**
     * builds the header of the message if not already present
     * @param message type of header, or of header
     * @return this
     */
    public JSONBuilder build(SocketProtocol message) {
        if(jsonObject.get(SocketProtocol.HEADER.get()) == null) {
            this.jsonObject.put(SocketProtocol.HEADER.get(), message.get());
        }
        return this;
    }

    /**
     * it creates a multiple parameter field
     * @param message type of field
     * @param param list of parameters
     * @return this
     */
    public JSONBuilder build(SocketProtocol message, List<String> param) {
        JSONArray jsonArray = new JSONArray();
        for (String s : param) {
            jsonArray.add(s);
        }
        this.jsonObject.put(message.get(), jsonArray);
        return this;
    }
}
