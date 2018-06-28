package it.polimi.ingsw.server.controller.rules;

import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.controller.channels.CommunicationChannel;
import it.polimi.ingsw.server.controller.channels.MockCommunicationChannel;
import it.polimi.ingsw.server.exception.*;
import it.polimi.ingsw.server.model.objective.ColorPrivateObjective;
import it.polimi.ingsw.server.model.objective.PublicObjective;
import it.polimi.ingsw.server.model.objective.SetPublicObjective;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.dice.DieColor;
import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.List;

class DefaultRulesTest {

    Game game;

    @BeforeEach
    @Test
    void createGame(){
        List<CommunicationChannel> cc = new ArrayList<>();
        cc.add(new MockCommunicationChannel("player1"));
        cc.add(new MockCommunicationChannel("player2"));
        game = new Game(cc);
    }

    @DisplayName("Give window and token test")
    @Test
    void dealGlassWindow() throws DieNotAllowedException {
        Assertions.assertThrows(NoSuchElementException.class,()->DefaultRules.getDefaultRules().giveTokens().execute(game));
        DefaultRules.getDefaultRules().dealGlassWindow(2).execute(game);
        Assertions.assertTrue(()-> game.getTable().getPlayers().stream().allMatch(p->(p.hasGlassWindow())));
    }

    @DisplayName("Give private objective")
    @Test
    void dealPrivateObjective() throws DieNotAllowedException {
        DefaultRules.getDefaultRules().dealPrivateObjective(1).execute(game);
        Assertions.assertTrue(()-> game.getTable().getPlayers().stream().allMatch(p->(p.getPrivateObjective().size() == 1)));
    }

    @DisplayName("Give public objectives")
    @Test
    void dealPublicObjective() throws DieNotAllowedException {
        DefaultRules.getDefaultRules().dealPublicObjective(3).execute(game);
        Assertions.assertEquals(3, game.getTable().getPublicObjectives().size());
    }

    @DisplayName("Testing round actions")
    @Test
    void getRoundAction() throws DieNotAllowedException {
        Assertions.assertEquals(0,game.getTable().getPool().getDice().size());
        DefaultRules.getDefaultRules().getSetupRoundAction().execute(game);
        Assertions.assertEquals(5,game.getTable().getPool().getDice().size());
        DefaultRules.getDefaultRules().getEndRoundAction().execute(game);
        Assertions.assertEquals(0,game.getTable().getPool().getDice().size());
        Assertions.assertEquals(5,game.getTable().getRoundTrack().getDice(1).size());
    }

    @DisplayName("Testing end game action")
    @Test
    void getEndGameAction() throws DieNotAllowedException {
        DefaultRules.getDefaultRules().dealGlassWindow(2).execute(game);
        game.getTable().getPlayers().forEach(p->p.addPrivateObjective(new ColorPrivateObjective("testObj", DieColor.CYAN)));
        Collection<PublicObjective> po = new ArrayList<>();
        Collection<Integer> n = new ArrayList<>();
        Collection<DieColor> c = new ArrayList<>();
        n.add(1);
        n.add(2);
        po.add(new SetPublicObjective("testPub", 2, n, c));
        game.getTable().setPublicObjective(po);
        for(Player player: game.getTable().getPlayers())
            player.setTokens(player.getGlassWindow().getDifficulty());
        for(Player player: game.getTable().getPlayers())
            for(int i=0; i<15;i++)
                if(i%2==0)
                    player.getGlassWindow().getCellList().get(i).placeDie(new Die(DieColor.CYAN,1,10),true);
                else
                    player.getGlassWindow().getCellList().get(i).placeDie(new Die(DieColor.RED,2,10),true);
        for(int j=15;j<20;j++)
            if(j%2==0)
                game.getTable().getPlayer("player1").getGlassWindow().getCellList().get(j).placeDie(new Die(DieColor.CYAN,1,10),true);
            else
                game.getTable().getPlayer("player1").getGlassWindow().getCellList().get(j).placeDie(new Die(DieColor.RED,2,10),true);
        int tokenP1=game.getTable().getPlayer("player1").getTokens();
        int tokenP2=game.getTable().getPlayer("player2").getTokens();
        DefaultRules.getDefaultRules().getEndGameAction().execute(game);
        List<Pair<Player,Integer>> ranking = game.getRanking();
        Assertions.assertEquals(2,ranking.size());
        for(Pair pair:ranking) {
            Player player = (Player) pair.getKey();
            switch ((player.getNickname())) {
                case "player1" : Assertions.assertEquals(30+tokenP1,pair.getValue());
                break;
                case "player2" : Assertions.assertEquals(17+tokenP2,pair.getValue());
                    break;
            }
        }
        game.getChannel("player1").setOffline();
        DefaultRules.getDefaultRules().getEndGameAction().execute(game);
        ranking = game.getRanking();
        Assertions.assertEquals(1, ranking.size());
    }
}