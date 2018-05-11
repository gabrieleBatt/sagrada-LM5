package it.polimi.ingsw.server.model.objective;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.model.exception.DeckTooSmallException;
import it.polimi.ingsw.server.model.exception.IllegalObjectiveException;
import it.polimi.ingsw.server.model.exception.InvalidJSONException;
import it.polimi.ingsw.server.model.table.Deck;
import it.polimi.ingsw.server.model.table.dice.DieColor;
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
                    JSONObject js = (JSONObject)parser.parse(new FileReader(file));
                    privateObjectives.add(js);
                    logger.log(Level.FINEST,  "This private objective "+ js.get("name") +" has been added to privateObjectives", this);

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
            logger.log(Level.FINEST, num + " private objectives have been drawn ", this);
            logger.log(Level.FINEST, " These are private objectives added: " + ret, this);

            return ret;
        }

        private PrivateObjective readCard(JSONObject jsonObject) throws InvalidJSONException {
                return new ColorPrivateObjective((String)jsonObject.get("name"), DieColor.valueOf(((String)jsonObject.get("color"))));
        }

}
