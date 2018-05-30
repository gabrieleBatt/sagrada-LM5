package it.polimi.ingsw.server.model.table;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


class TableTest {

    Player player1;
    Player player2;
    Player player3;
    List<Player> players;

    Table table;

    @Test
    @BeforeEach
    void setup(){
        player1 = new Player("p1");
        player2 = new Player("p2");
        player3 = new Player("p3");
        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);

        table = new Table(players);
    }

    @DisplayName("Get players in table")
    @Test
    void getPlayers() {
        Assertions.assertEquals(players, table.getPlayers());
    }

    @DisplayName("Iterate players")
    @Test
    void getItPlayers() {
        Iterator<Player> iterator = table.getPlayersIterator(player2, false, false);
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals(player2, iterator.next());
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals(player3, iterator.next());
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals(player1, iterator.next());
        Assertions.assertFalse(iterator.hasNext());

        iterator = table.getPlayersIterator(player2, false, true);
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals(player1, iterator.next());
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals(player3, iterator.next());
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals(player2, iterator.next());
        Assertions.assertFalse(iterator.hasNext());
    }

    @DisplayName("Get players by name")
    @Test
    void getPlayer() {
        Assertions.assertEquals(player1, table.getPlayer("p1"));
        Assertions.assertThrows(NoSuchElementException.class, ()->table.getPlayer("p4"));
    }
}