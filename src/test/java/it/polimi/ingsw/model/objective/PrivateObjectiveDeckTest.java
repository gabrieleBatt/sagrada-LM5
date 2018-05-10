package it.polimi.ingsw.model.objective;

import it.polimi.ingsw.model.exception.DeckTooSmallException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrivateObjectiveDeckTest {

    @DisplayName("Draw 4 PrivateObjective")
    @Test
    void draw() throws DeckTooSmallException {
        Assertions.assertEquals(4, PrivateObjectiveDeck.getPrivateObjectiveDeck().draw(4).size());
    }

    @DisplayName("Draw too many PrivateObjective")
    @Test
    void drawTooMany() throws DeckTooSmallException {
        Assertions.assertThrows(DeckTooSmallException.class, () -> PrivateObjectiveDeck.getPrivateObjectiveDeck().draw(100));
    }

}