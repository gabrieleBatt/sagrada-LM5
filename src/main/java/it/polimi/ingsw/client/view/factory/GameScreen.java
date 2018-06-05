package it.polimi.ingsw.client.view.factory;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.shared.Message;
import it.polimi.ingsw.shared.interfaces.RemoteGameScreen;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;

public abstract class GameScreen implements RemoteGameScreen {

    protected static final String TOOL_PATH;
    protected static final String GLASS_WINDOW_PATH = "clientResources/glassWindows/";
    protected static final String OBJECTIVE_PATH;

    static {
        String language = "ita";
        try {
            InputStreamReader in = new InputStreamReader(Message.class.getClassLoader().getResourceAsStream("clientResources/config.json"));
            JSONObject config = (JSONObject) new JSONParser().parse(in);
            language = ((String) config.get("language")).toLowerCase();
            in.close();
        } catch (IOException | ParseException e) {
            Client.getLogger().log(Level.WARNING, "Config directory not found");
        }
        OBJECTIVE_PATH = "clientResources/lang/"+language+"/objectives/";
        TOOL_PATH = "clientResources/lang/"+language+"/tools/";
    }

    protected GameScreen(){

    }
}