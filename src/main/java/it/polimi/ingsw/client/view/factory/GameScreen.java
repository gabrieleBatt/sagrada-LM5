package it.polimi.ingsw.client.view.factory;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.net.interfaces.RemoteGameScreen;
import javafx.util.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public abstract class GameScreen implements RemoteGameScreen {

    protected static final String TOOL_PATH = "resources/clientResources/tools/";
    protected static final String GLASS_WINDOW_PATH = "resources/clientResources/glassWindows/";
    protected static final String OBJECTIVE_PATH;

    static {
        String language = "ita";
        try {
            JSONObject config = (JSONObject) new JSONParser().parse(new FileReader("resources/clientResources/config.json"));
            language = ((String) config.get("language")).toLowerCase();
        } catch (IOException | ParseException e) {
            Client.getLogger().log(Level.WARNING, "Language directory not found");
        }
        OBJECTIVE_PATH = "resources/clientResources/lang/"+language+"/objectives/";
    }

    private GameScreen(){}
}