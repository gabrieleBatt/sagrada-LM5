package it.polimi.ingsw.model.table;


import it.polimi.ingsw.model.table.dice.DiceBag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;


class TableTest {

    //TODO
    @Test
    void setPublicObjective() {
    }

    @Test
    void setTools() {

    }

    @Test
    void getPlayers() {
        Player player1 = new Player("p1");
        Player player2 = new Player("p2");
        Player player3 = new Player("p3");
        Collection<Player> players = new HashSet<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);

        Table table = new Table(players);

        Assertions.assertEquals(players, table.getPlayers());
    }

    @Test
    void getPublicObjectives() {
    }

    @Test
    void getTools() {
    }

    @Test
    void getDiceBag() {
        Player player1 = new Player("p1");
        Player player2 = new Player("p2");
        Player player3 = new Player("p3");
        Collection<Player> players = new HashSet<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);

        Table table = new Table(players);
        DiceBag diceBagT = new DiceBag();
        Assertions.assertEquals(diceBagT,table.getDiceBag());

    }

    @Test
    void getPool() {
    }

    @Test
    void getRoundTrack() {
    }

    @Test
    void getActiveEffects() {
    }

    @Test
    void addEffect() {
    }
}