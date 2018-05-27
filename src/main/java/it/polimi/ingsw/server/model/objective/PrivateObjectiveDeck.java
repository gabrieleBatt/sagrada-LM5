package it.polimi.ingsw.server.model.objective;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.exception.DeckTooSmallException;
import it.polimi.ingsw.server.model.table.Deck;
import it.polimi.ingsw.server.model.table.dice.DieColor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class PrivateObjectiveDeck implements Deck {

    private static final Logger logger = LogMaker.getLogger(PrivateObjectiveDeck.class.getName(), Level.ALL);
    private static PrivateObjectiveDeck privateObjectiveDeck = new PrivateObjectiveDeck();
    private List<JSONObject> privateObjectives;

    private PrivateObjectiveDeck(){
        privateObjectives = new ArrayList<>();
        Path path = Paths.get("resources/serverResources/objectives/private");
        try (Stream<Path> files = Files.list(path)){
            files.forEach(f -> addCard(f.toFile()));
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    public static PrivateObjectiveDeck getPrivateObjectiveDeck() {
        return privateObjectiveDeck;
    }

    private void addCard(File file) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject js = (JSONObject)parser.parse(new FileReader(file));
            privateObjectives.add(js);
            logger.log(Level.FINEST,  "This private objective "+ js.get("name") +" has been added to privateObjectives", this);
        } catch (IOException | ParseException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    @Override
    public List<PrivateObjective> draw(int num) {
        List<PrivateObjective> ret = new ArrayList<>();

        if(privateObjectives.size() < num) throw new DeckTooSmallException(num + " cards requested, " + privateObjectives + " in deck");

        Set<Integer> integerSet = new HashSet<>();
        while(integerSet.size() < num) {
            integerSet.add(ThreadLocalRandom.current().nextInt(0, privateObjectives.size()));
        }

        for(Integer i: integerSet){
            ret.add(readCard(privateObjectives.get(i)));
        }
        logger.log(Level.FINEST, num + " private objectives have been drawn ", this);
        logger.log(Level.FINEST, " These are private objectives added: " + ret, this);

        return ret;
    }

    private PrivateObjective readCard(JSONObject jsonObject){
        return new ColorPrivateObjective((String)jsonObject.get("name"), DieColor.valueOf(((String)jsonObject.get("color"))));
    }

}
