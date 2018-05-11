package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.glassWindow.GlassWindow;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultRulesTest {

    Game game;

    @BeforeEach
    @Test
    void createGame(){
        List<CommunicationChannel> cc = new ArrayList<>();
        cc.add(new MockComunicationChannell("player1"));
        cc.add(new MockComunicationChannell("player2"));
        game = new Game(cc);
    }

    @DisplayName("Give window and token test")
    @Test
    void dealGlassWindow() throws BagEmptyException, GlassWindowNotFoundException, EndGameException, DeckTooSmallException, PlayerNotFoundException {
        Assertions.assertThrows(GlassWindowNotFoundException.class,()->DefaultRules.getDefaultRules().giveTokens().execute(game));
        DefaultRules.getDefaultRules().dealGlassWindow(2).execute(game);
        Assertions.assertTrue(()-> game.getTable().getPlayers().stream().allMatch(p->(p.hasGlassWindow())));
    }

    @DisplayName("Give private objective")
    @Test
    void dealPrivateObjective() throws BagEmptyException, GlassWindowNotFoundException, EndGameException, DeckTooSmallException, PlayerNotFoundException {
        DefaultRules.getDefaultRules().dealPrivateObjective(1).execute(game);
        Assertions.assertTrue(()-> game.getTable().getPlayers().stream().allMatch(p->(p.getPrivateObjective().size() == 1)));
    }

    @DisplayName("Give public objectives")
    @Test
    void dealPublicObjective() throws BagEmptyException, GlassWindowNotFoundException, EndGameException, DeckTooSmallException, PlayerNotFoundException {
        DefaultRules.getDefaultRules().dealPublicObjective(3).execute(game);
        Assertions.assertEquals(3, game.getTable().getPublicObjectives().size());
    }

    @DisplayName("Testing round actions")
    @Test
    void getRoundAction() throws BagEmptyException, GlassWindowNotFoundException, EndGameException, DeckTooSmallException, PlayerNotFoundException {
        Assertions.assertEquals(0,game.getTable().getPool().getDice().size());
        DefaultRules.getDefaultRules().getSetupRoundAction().execute(game);
        Assertions.assertEquals(5,game.getTable().getPool().getDice().size());
        DefaultRules.getDefaultRules().getEndRoundAction().execute(game);
        Assertions.assertEquals(0,game.getTable().getPool().getDice().size());
        Assertions.assertEquals(5,game.getTable().getRoundTrack().getDice(1).size());
    }

}