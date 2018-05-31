package it.polimi.ingsw.server.controller.deck;

import it.polimi.ingsw.server.controller.deck.GlassWindowDeck;
import it.polimi.ingsw.server.exception.DeckTooSmallException;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class GlassWindowDeckTest {


    @DisplayName("Draw 4 glassWindows")
    @Test
    void draw() {
        Assertions.assertEquals(2*2, GlassWindowDeck.getGlassWindowDeck().draw(2).size());
    }

    @DisplayName("Draw too many glassWindows")
    @Test
    void drawTooMany() {
        Assertions.assertThrows(DeckTooSmallException.class, () -> GlassWindowDeck.getGlassWindowDeck().draw(100));
    }

    @DisplayName("Parsing control")
    @Test
    void drawRealTest(){
        List<GlassWindow> cards = GlassWindowDeck.getGlassWindowDeck().draw(12);

        GlassWindow glassWindow = cards.stream().filter(d -> d.getName().equalsIgnoreCase("Industria")).findFirst().get();
        Assertions.assertEquals(glassWindow.getDifficulty(), 5);
        Assertions.assertEquals(glassWindow.getCell(0,0).toString(), "This cell:has a number restriction:1,is empty");
        Assertions.assertEquals(glassWindow.getCell(0,1).toString(), "This cell:has a color restriction:R,is empty");
        Assertions.assertEquals(glassWindow.getCell(0,2).toString(), "This cell:has a number restriction:3,is empty");
        Assertions.assertEquals(glassWindow.getCell(0,3).toString(), "This cell:is empty");
        Assertions.assertEquals(glassWindow.getCell(0,4).toString(), "This cell:has a number restriction:6,is empty");
        Assertions.assertEquals(glassWindow.getCell(1,0).toString(), "This cell:has a number restriction:5,is empty");
        Assertions.assertEquals(glassWindow.getCell(1,1).toString(), "This cell:has a number restriction:4,is empty");
        Assertions.assertEquals(glassWindow.getCell(1,2).toString(), "This cell:has a color restriction:R,is empty");
        Assertions.assertEquals(glassWindow.getCell(1,3).toString(), "This cell:has a number restriction:2,is empty");
        Assertions.assertEquals(glassWindow.getCell(1,4).toString(), "This cell:is empty");
        Assertions.assertEquals(glassWindow.getCell(2,0).toString(), "This cell:is empty");
        Assertions.assertEquals(glassWindow.getCell(2,1).toString(), "This cell:is empty");
        Assertions.assertEquals(glassWindow.getCell(2,2).toString(), "This cell:has a number restriction:5,is empty");
        Assertions.assertEquals(glassWindow.getCell(2,3).toString(), "This cell:has a color restriction:R,is empty");
        Assertions.assertEquals(glassWindow.getCell(2,4).toString(), "This cell:has a number restriction:1,is empty");
        Assertions.assertEquals(glassWindow.getCell(3,0).toString(), "This cell:is empty");
        Assertions.assertEquals(glassWindow.getCell(3,1).toString(), "This cell:is empty");
        Assertions.assertEquals(glassWindow.getCell(3,2).toString(), "This cell:is empty");
        Assertions.assertEquals(glassWindow.getCell(3,3).toString(), "This cell:has a number restriction:3,is empty");
        Assertions.assertEquals(glassWindow.getCell(3,4).toString(), "This cell:has a color restriction:R,is empty");
    }

}