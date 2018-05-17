package it.polimi.ingsw.server.model.tool;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.exception.DeckTooSmallException;
import it.polimi.ingsw.server.model.table.Deck;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class ToolDeck implements Deck {

    private static final Logger logger = LogMaker.getLogger(ToolDeck.class.getName(), Level.ALL);
    private static ToolDeck toolDeck = new ToolDeck();
    private List<JSONObject> tools;

    private ToolDeck(){
        tools = new ArrayList<>();
        Path path = Paths.get("resources/ServerResources/tools");
        try (Stream<Path> files = Files.list(path)){
            files.forEach((f) -> addCard(f.toFile()));
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }

    }

    public static ToolDeck getToolDeck() {
        return toolDeck;
    }

    private void addCard(File file) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject js = (JSONObject) parser.parse(new FileReader(file));
            tools.add(js);
            logger.log(Level.FINEST, "This tool " + js.get("name") + " has been added to tools", this);

        } catch (IOException | ParseException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }



    @Override
    public List<Tool> draw(int num){
        List<Tool> ret = new ArrayList<>();

        if(tools.size() < num) throw new DeckTooSmallException(num + " tools requested, " + tools );
        Set<Integer> integerSet = new HashSet<>();
        while(integerSet.size() < num) {
            integerSet.add(ThreadLocalRandom.current().nextInt(0, tools.size()));
        }

        for(Integer i: integerSet){
                ret.add(readCard(tools.get(i)));


        }
        logger.log(Level.FINEST, num + " tools have been drawn ", this);
        logger.log(Level.FINEST, " These tools have been added: " + ret, this);

        return ret;
    }

    private Tool readCard(JSONObject jsonObject) {
        // TODO
        return null;
    }


}
