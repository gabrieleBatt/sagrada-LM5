package it.polimi.ingsw.server.controller.deck;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.exception.DeckTooSmallException;
import it.polimi.ingsw.server.model.objective.ColorPrivateObjective;
import it.polimi.ingsw.server.model.objective.PrivateObjective;
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

public class PrivateObjectiveDeck extends Deck {

    private static final Logger logger = LogMaker.getLogger(PrivateObjectiveDeck.class.getName(), Level.ALL);
    private static PrivateObjectiveDeck privateObjectiveDeck = new PrivateObjectiveDeck(Paths.get("resources/serverResources/objectives/private"));
    private static final String NAME = "name";

    private PrivateObjectiveDeck(Path path){
        super(path);
    }

    public static PrivateObjectiveDeck getPrivateObjectiveDeck() {
        return privateObjectiveDeck;
    }

    private Optional<JSONObject> parse(File file) {
        JSONParser parser = new JSONParser();
        JSONObject js = null;
        try {
            js = (JSONObject)parser.parse(new FileReader(file));
            logger.log(Level.FINEST,  "This private objective "+ js.get(NAME) +" has been added to getPaths()", this);

        } catch (IOException | ParseException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return Optional.ofNullable(js);
    }

    @Override
    public List<PrivateObjective> draw(int num) {
        List<PrivateObjective> ret = new ArrayList<>();

        if(getPaths().size() < num) throw new DeckTooSmallException(num + " cards requested, " + getPaths() + " in deck");

        Set<Integer> integerSet = new HashSet<>();
        while(integerSet.size() < num) {
            integerSet.add(ThreadLocalRandom.current().nextInt(0, getPaths().size()));
        }

        for(Integer i: integerSet){
            Optional<JSONObject> optional = parse(getPaths().get(i).toFile());
            optional.ifPresent(jsonObject -> ret.add(readCard(jsonObject)));
        }
        logger.log(Level.FINEST, num + " private objectives have been drawn ", this);
        logger.log(Level.FINEST, " These are private objectives added: " + ret, this);

        return ret;
    }

    private PrivateObjective readCard(JSONObject jsonObject){
        return new ColorPrivateObjective((String)jsonObject.get(NAME), DieColor.valueOf(((String)jsonObject.get("color"))));
    }

}
