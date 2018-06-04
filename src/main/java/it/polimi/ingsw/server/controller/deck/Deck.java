package it.polimi.ingsw.server.controller.deck;

import it.polimi.ingsw.shared.LogMaker;
import it.polimi.ingsw.server.exception.DeckTooSmallException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;


/**
 * Contains always all possible cards, if cards are drawn they do not disappear from deck
 */
abstract class Deck {

    private static final Logger logger = LogMaker.getLogger(Deck.class.getName(), Level.ALL);
    private List<Path> paths;

    protected Deck(Path path){
        paths = new ArrayList<>();
        try (Stream<Path> files = Files.list(path)){
            files.forEach(f -> paths.add(f));
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }

    }

    protected List<Path> getPaths() {
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
