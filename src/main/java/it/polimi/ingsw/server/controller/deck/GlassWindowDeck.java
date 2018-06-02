package it.polimi.ingsw.server.controller.deck;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.exception.DeckTooSmallException;
import it.polimi.ingsw.server.model.table.dice.DieColor;
import it.polimi.ingsw.server.model.table.glasswindow.Cell;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;
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
public class GlassWindowDeck extends Deck {

    private static final Logger logger = LogMaker.getLogger(GlassWindowDeck.class.getName(), Level.ALL);
    private static GlassWindowDeck glassWindowDeck = new GlassWindowDeck(Paths.get("resources/serverResources/glassWindows"));

    private GlassWindowDeck(Path path){
        super(path);
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

        if(getPaths().size() < num) throw new DeckTooSmallException(num + " cards requested, " + getPaths() + " in deck");

        Set<Integer> integerSet = new HashSet<>();
        while(integerSet.size() < num) {
            integerSet.add(ThreadLocalRandom.current().nextInt(0, getPaths().size()));
        }

        for(Integer i: integerSet){
            Optional<JSONObject> optional = parse(getPaths().get(i).toFile());
            optional.ifPresent(jsonObject -> ret.add(readCard(jsonObject, 1)));
            optional.ifPresent(jsonObject -> ret.add(readCard(jsonObject, 2)));
        }
        logger.log(Level.FINEST, num + "GlassWindows have been drawn ", this);

        return ret;
    }

    private Optional<JSONObject> parse(File file){
        JSONParser parser = new JSONParser();
        JSONObject js = null;
        try {
            js = (JSONObject)parser.parse(new FileReader(file));
            logger.log(Level.FINEST,  "GlassWindow "+ js.get("name1") + "/" + js.get("name2") +" has been added to getPaths()", this);
        } catch (IOException | ParseException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return Optional.ofNullable(js);
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
