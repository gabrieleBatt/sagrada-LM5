package it.polimi.ingsw.server.model.table.glassWindow;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.model.exception.DeckTooSmallException;
import it.polimi.ingsw.server.model.exception.IllegalGlassWindowException;
import it.polimi.ingsw.server.model.objective.SetPublicObjective;
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

/**
 * Contains always all possible GlassWindows, if GlassWindows are drawn they do not disappear from deck
 */
public class GlassWindowDeck implements Deck {

    private static final Logger logger = LogMaker.getLogger(GlassWindowDeck.class.getName(), Level.ALL);
    private static GlassWindowDeck glassWindowDeck = new GlassWindowDeck();
    private List<JSONObject> glassWindowCards;

    private GlassWindowDeck(){
        glassWindowCards = new ArrayList<>();
        try {
            Files.list(Paths.get("resources/ServerResources/glassWindow"))
                    .forEach((f) -> addCard(f.toFile()));
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }

    }

    /**
     * draws the twice the specified amount of different GlassWindow from deck, paired correctly
     * @param num - number of cards in deck to draw, half og GlassWindows drawn
     * @return GlassWindows drawn
     * @throws DeckTooSmallException is thrown if there are not enough GlassWindows in the deck
     */
    @Override
    public List<GlassWindow> draw(int num) throws DeckTooSmallException {
        List<GlassWindow> ret = new ArrayList<>();

        if(glassWindowCards.size() < num) throw new DeckTooSmallException(num + " cards requested, " + glassWindowCards + " in deck");

        Set<Integer> integerSet = new HashSet<>();
        while(integerSet.size() < num) {
            integerSet.add(ThreadLocalRandom.current().nextInt(0, glassWindowCards.size()));
        }

        for(Integer i: integerSet){
            try {
                ret.add(readCard(glassWindowCards.get(i), 1));
                ret.add(readCard(glassWindowCards.get(i), 2));
            } catch (IllegalGlassWindowException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }

        }

        return ret;
    }

    private void addCard(File file){
        JSONParser parser = new JSONParser();
        try {
            glassWindowCards.add((JSONObject)parser.parse(new FileReader(file)));
        } catch (IOException | ParseException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    private GlassWindow readCard(JSONObject jsonGlassWindow, int x) throws IllegalGlassWindowException {
        String name = (String) jsonGlassWindow.get("name"+x);
        int difficulty = Math.toIntExact((long)jsonGlassWindow.get("difficulty"+x));
        List<Cell> cells = new ArrayList<>();

        Iterator<String> iterator = ((JSONArray)jsonGlassWindow.get("cells"+x)).iterator();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (iterator.hasNext()) {
                    switch (iterator.next()){
                        case "W": cells.add(new Cell(i+""+j)); break;
                        case "R": cells.add(new Cell(i+""+j, DieColor.RED)); break;
                        case "G": cells.add(new Cell(i+""+j, DieColor.GREEN)); break;
                        case "Y": cells.add(new Cell(i+""+j, DieColor.YELLOW)); break;
                        case "M": cells.add(new Cell(i+""+j, DieColor.MAGENTA)); break;
                        case "C": cells.add(new Cell(i+""+j, DieColor.CYAN)); break;
                        case "1": cells.add(new Cell(i+""+j, 1)); break;
                        case "2": cells.add(new Cell(i+""+j, 2)); break;
                        case "3": cells.add(new Cell(i+""+j, 3)); break;
                        case "4": cells.add(new Cell(i+""+j, 4)); break;
                        case "5": cells.add(new Cell(i+""+j, 5)); break;
                        case "6": cells.add(new Cell(i+""+j, 6)); break;
                    }
                }
            }
        }
        return new GlassWindow(name, difficulty, cells);

    }

    public static GlassWindowDeck getGlassWindowDeck() {
        return glassWindowDeck;
    }



    class GlassWindowCard{
        private GlassWindow d1;
        private GlassWindow d2;

        public GlassWindowCard(GlassWindow d1, GlassWindow d2){
            this.d1 = d1;
            this.d2 = d2;
        }

        public GlassWindow getD1() {
            return d1;
        }

        public GlassWindow getD2() {
            return d2;
        }
    }
}
