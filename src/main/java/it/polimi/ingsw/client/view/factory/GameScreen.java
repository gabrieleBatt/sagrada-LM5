package it.polimi.ingsw.client.view.factory;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.shared.interfaces.RemoteGameScreen;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;


/**
 * Handles getting from the user all server requested inputs
 */
public abstract class GameScreen implements RemoteGameScreen {

    protected static final String TOOL_PATH;
    protected static final String GLASS_WINDOW_PATH = "resources/clientResources/glassWindows/";
    protected static final String OBJECTIVE_PATH;
    protected static int MAX_LEN_RECORD = 10;

    static {
        String language = "ita";
        try {
            InputStreamReader in = new InputStreamReader(new FileInputStream("resources/clientResources/config.json"));
            JSONObject config = (JSONObject) new JSONParser().parse(in);
            language = ((String) config.get("language")).toLowerCase();
            MAX_LEN_RECORD = Math.toIntExact((long)config.get("recordLength"));
            in.close();
        } catch (IOException | ParseException e) {
            Client.getLogger().log(Level.WARNING, "Config directory not found");
        }
        OBJECTIVE_PATH = "resources/clientResources/lang/"+language+"/objectives/";
        TOOL_PATH = "resources/clientResources/lang/"+language+"/tools/";
    }

    protected GameScreen(){
    }
}