package it.polimi.ingsw.model.table.glassWindow;

import it.polimi.ingsw.model.exception.DeckTooSmallException;
import it.polimi.ingsw.model.exception.IllegalGlassWindowException;
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

public class GlassWindowDeck implements Deck {

    private static GlassWindowDeck glassWindowDeck = new GlassWindowDeck();
    private List<GlassWindowCard> glassWindowCards;

    private GlassWindowDeck(){
        glassWindowCards = new ArrayList<>();
        try {
            Files.list(Paths.get("resources/ServerResources/glassWindow"))
                    .forEach((f) -> addCard(f.toFile()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public List<GlassWindow> draw(int num) throws DeckTooSmallException {
        List<GlassWindow> ret = new ArrayList<>();

        if(glassWindowCards.size() < num) throw new DeckTooSmallException(num + " cards requested, " + glassWindowCards + " in deck");

        Set<Integer> integerSet = new HashSet<>();
        while(integerSet.size() < num) {
            integerSet.add(ThreadLocalRandom.current().nextInt(0, glassWindowCards.size()));
        }

        for(Integer i: integerSet){
            ret.add(glassWindowCards.get(i).getD1());
            ret.add(glassWindowCards.get(i).getD2());
        }

        return ret;
    }

    private void addCard(File file){
        JSONParser parser = new JSONParser();
        try {

            JSONObject jsonGlassWindow = (JSONObject)parser.parse(new FileReader(file));

            glassWindowCards.add(new GlassWindowCard(this.readCard(jsonGlassWindow, 1), this.readCard(jsonGlassWindow, 2)));

        } catch (ParseException | IOException | IllegalGlassWindowException e) {
            System.out.println(e.getMessage());
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
