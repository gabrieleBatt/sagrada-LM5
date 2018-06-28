package it.polimi.ingsw.server.controller.deck;

import it.polimi.ingsw.shared.LogMaker;
import it.polimi.ingsw.server.exception.DeckTooSmallException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;


/**
 * Contains always all possible cards, if cards are drawn they do not disappear from deck
 */
abstract class Deck {

    private static final Logger logger = LogMaker.getLogger(Deck.class.getName(), Level.ALL);
    private List<Path> paths;
    static final String NAME = "name";

    Deck(Path path){
        paths = new ArrayList<>();
        try (Stream<Path> files = Files.list(path)){
            files.forEach(f -> paths.add(f));
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }

    }

    protected Optional<JSONObject> parse(File file){
        JSONParser parser = new JSONParser();
        JSONObject js = null;
        try {
            js = (JSONObject)parser.parse(new FileReader(file));
        } catch (IOException | ParseException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return Optional.ofNullable(js);
    }

    List<Path> getPaths() {
        return paths;
    }

    /**
     * draws the specified amount of different cards from deck
     * @param num - number of cards in deck to draw
     * @return cards drawn
     * @throws DeckTooSmallException is thrown if there are not enough cards in the deck
     */
    public abstract Object draw(int num);

}
