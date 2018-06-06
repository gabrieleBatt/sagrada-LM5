package it.polimi.ingsw.server.controller.deck;

import it.polimi.ingsw.server.controller.deck.PrivateObjectiveDeck;
import it.polimi.ingsw.server.exception.DeckTooSmallException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PrivateObjectiveDeckTest {

    @DisplayName("Draw 4 PrivateObjective")
    @Test
    void draw() {
        Assertions.assertEquals(4, PrivateObjectiveDeck.getPrivateObjectiveDeck().draw(4).size());
    }

    @DisplayName("Draw too many PrivateObjective")
    @Test
    void drawTooMany() {
        Assertions.assertThrows(DeckTooSmallException.class, () -> PrivateObjectiveDeck.getPrivateObjectiveDeck().draw(100));
    }

}