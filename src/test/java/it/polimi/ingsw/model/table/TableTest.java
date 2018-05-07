package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.tool.Effect;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


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


    @Test
    void getPlayers() {
        Assertions.assertEquals(players, table.getPlayers());
    }

    @Test
    void getItPlayers() {
        Iterator<Player> iterator = table.getPlayersIterator(player2);
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals(player2, iterator.next());
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals(player3, iterator.next());
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals(player1, iterator.next());
        Assertions.assertFalse(iterator.hasNext());
    }

    @Test
    void getEffects() {
        Effect e = new Effect();
        Assertions.assertEquals(0, table.getEffects().size());
        table.addEffect(e);
        Assertions.assertEquals(1, table.getEffects().size());
        Assertions.assertTrue(table.getEffects().contains(e));
    }
}