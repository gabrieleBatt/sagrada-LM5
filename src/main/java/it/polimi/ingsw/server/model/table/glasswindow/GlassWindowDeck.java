package it.polimi.ingsw.server.model.table.glasswindow;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.exception.DeckTooSmallException;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Contains always all possible GlassWindows, if GlassWindows are drawn they do not disappear from deck
 */
public class GlassWindowDeck implements Deck {

    private static final Logger logger = LogMaker.getLogger(GlassWindowDeck.class.getName(), Level.ALL);
    private static GlassWindowDeck glassWindowDeck = new GlassWindowDeck();
    private List<JSONObject> glassWindowCards;

    private GlassWindowDeck(){
        glassWindowCards = new ArrayList<>();
        Path path = Paths.get("resources/serverResources/glassWindows");
        try (Stream<Path> files = Files.list(path)){
            files.forEach(f -> addCard(f.toFile()));
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }

    }

    /**
     * draws twice the specified amount of different GlassWindow from deck, paired correctly
     * @param num - number of cards in deck to draw, half of GlassWindows drawn
     * @return GlassWindows drawn
     * @throws DeckTooSmallException is thrown if there are not enough GlassWindows in the deck
     */
    @Override
    public List<GlassWindow> draw(int num) {
        List<GlassWindow> ret = new ArrayList<>();

        if(glassWindowCards.size() < num) throw new DeckTooSmallException(num + " cards requested, " + glassWindowCards + " in deck");

        Set<Integer> integerSet = new HashSet<>();
        while(integerSet.size() < num) {
            integerSet.add(ThreadLocalRandom.current().nextInt(0, glassWindowCards.size()));
        }

        for(Integer i: integerSet){
            ret.add(readCard(glassWindowCards.get(i), 1));
            ret.add(readCard(glassWindowCards.get(i), 2));

        }
        logger.log(Level.FINEST, num + "GlassWindows have been drawn ", this);

        return ret;
    }

    private void addCard(File file){
        JSONParser parser = new JSONParser();
        try {
            JSONObject js = (JSONObject)parser.parse(new FileReader(file));
            glassWindowCards.add(js);
            logger.log(Level.FINEST,  "GlassWindow "+ js.get("name1") + "/" + js.get("name2") +" has been added to glassWindowCards", this);

        } catch (IOException | ParseException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    private GlassWindow readCard(JSONObject jsonGlassWindow, int x) {
        String name = (String) jsonGlassWindow.get("name"+x);
        int difficulty = Math.toIntExact((long)jsonGlassWindow.get("difficulty"+x));
        List<Cell> cells = new ArrayList<>();

        Iterator<String> iterator = ((JSONArray)jsonGlassWindow.get("cells"+x)).iterator();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (iterator.hasNext()) {
                    String id = i+""+j+":"+name;
                    Cell cell;
                    switch (iterator.next()){
                        case "R": cell = new Cell(id, DieColor.RED); break;
                        case "G": cell = new Cell(id, DieColor.GREEN); break;
                        case "Y": cell = new Cell(id, DieColor.YELLOW); break;
                        case "M": cell = new Cell(id, DieColor.MAGENTA); break;
                        case "C": cell = new Cell(id, DieColor.CYAN); break;
                        case "1": cell = new Cell(id, 1); break;
                        case "2": cell = new Cell(id, 2); break;
                        case "3": cell = new Cell(id, 3); break;
                        case "4": cell = new Cell(id, 4); break;
                        case "5": cell = new Cell(id, 5); break;
                        case "6": cell = new Cell(id, 6); break;
                        default: cell = new Cell(id); break;
                    }
                    cells.add(cell);
                }
            }
        }
        return new GlassWindow(name, difficulty, cells);

    }

    public static GlassWindowDeck getGlassWindowDeck() {
        return glassWindowDeck;
    }
}
