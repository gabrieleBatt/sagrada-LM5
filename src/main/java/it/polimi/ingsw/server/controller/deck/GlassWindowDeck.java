package it.polimi.ingsw.server.controller.deck;

import it.polimi.ingsw.shared.LogMaker;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Contains always all possible GlassWindows, if GlassWindows are drawn they do not disappear from deck
 */
public class GlassWindowDeck extends Deck {

    private static final Logger logger = LogMaker.getLogger(GlassWindowDeck.class.getName(), Level.ALL);
    private static final String EMPTY_CELL = "";
    private static final String DIFFICULTY = "difficulty";
    private static final String CELLS = "cells";
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

    private GlassWindow readCard(JSONObject jsonGlassWindow, int x) {
        String name = (String) jsonGlassWindow.get(NAME+x);
        int difficulty = Math.toIntExact((long)jsonGlassWindow.get(DIFFICULTY+x));
        List<Cell> cells = new ArrayList<>();

        Iterator<String> iterator = ((JSONArray)jsonGlassWindow.get(CELLS+x)).iterator();

        for (int i = 0; i < GlassWindow.ROWS; i++) {
            for (int j = 0; j < GlassWindow.COLUMNS; j++) {
                if (iterator.hasNext()) {
                    String id = i+""+j+":"+name;
                    Cell cell;
                    String restriction = iterator.next();
                    if (restriction.equalsIgnoreCase(EMPTY_CELL))
                        cell = new Cell(id);
                    else if(Arrays.stream(DieColor.values())
                            .map(DieColor::name).collect(Collectors.toList()).contains(restriction))
                        cell = new Cell(id, DieColor.valueOf(restriction));
                    else
                        cell = new Cell(id, Integer.parseInt(restriction));
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
