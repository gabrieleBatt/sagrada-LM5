package it.polimi.ingsw.shared.socket;

import it.polimi.ingsw.shared.LogMaker;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Builder of json objects used for socket communication
 */
public final class JSONBuilder{

    private static Logger logger = LogMaker.getLogger(JSONBuilder.class.getName(), Level.OFF);
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

    /**
     * Prints the message built in out
     * @param out output to print on
     */
    public void send(PrintWriter out){
        out.println(this.get().toString());
        logger.log(Level.FINE, "SentJson", this.get());
        out.flush();
    }
}
