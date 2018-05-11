package it.polimi.ingsw.model.objective;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.model.exception.DeckTooSmallException;
import it.polimi.ingsw.model.exception.IllegalObjectiveException;
import it.polimi.ingsw.model.exception.InvalidJSONException;
import it.polimi.ingsw.model.table.Deck;
import it.polimi.ingsw.model.table.dice.DieColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrivateObjectiveDeck implements Deck {

    private static final Logger logger = LogMaker.getLogger(PrivateObjectiveDeck.class.getName(), Level.ALL);
    private static PrivateObjectiveDeck privateObjectiveDeck = new PrivateObjectiveDeck();
        private List<JSONObject> privateObjectives;

        private PrivateObjectiveDeck(){
            privateObjectives = new ArrayList<>();
            try {
                Files.list(Paths.get("resources/ServerResources/objectives/private"))
                        .forEach((f) -> addCard(f.toFile()));
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
                    privateObjectives.add((JSONObject)parser.parse(new FileReader(file)));
                } catch (IOException | ParseException e) {
                    logger.log(Level.WARNING, e.getMessage(), e);
                }
        }

        @Override
        public List<PrivateObjective> draw(int num) throws DeckTooSmallException {
            List<PrivateObjective> ret = new ArrayList<>();

            if(privateObjectives.size() < num) throw new DeckTooSmallException(num + " cards requested, " + privateObjectives + " in deck");

            Set<Integer> integerSet = new HashSet<>();
            while(integerSet.size() < num) {
                integerSet.add(ThreadLocalRandom.current().nextInt(0, privateObjectives.size()));
            }

            for(Integer i: integerSet){
                try {
                    ret.add(readCard(privateObjectives.get(i)));
                } catch (InvalidJSONException e) {
                    logger.log(Level.WARNING, e.getMessage(), e);
                }

            }
            return ret;
        }

        private PrivateObjective readCard(JSONObject jsonObject) throws InvalidJSONException {
                return new ColorPrivateObjective((String)jsonObject.get("name"), DieColor.valueOf(((String)jsonObject.get("color"))));
        }

}
