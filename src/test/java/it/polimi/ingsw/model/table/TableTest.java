package it.polimi.ingsw.model.table;


import it.polimi.ingsw.model.table.dice.DiceBag;
import it.polimi.ingsw.model.tool.Effect;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;


class TableTest {

    Player player1;
    Player player2;
    Player player3;
    Collection<Player> players;

    Table table;

    @Test
    @BeforeEach
    void setup(){
        player1 = new Player("p1");
        player2 = new Player("p2");
        player3 = new Player("p3");
        players = new HashSet<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);

        table = new Table(players);
    }

    @Test
    void setPublicObjective() {
    }

    @Test
    void setTools() {

    }

    @Test
    void getPlayers() {
        Assertions.assertEquals(players, table.getPlayers());
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