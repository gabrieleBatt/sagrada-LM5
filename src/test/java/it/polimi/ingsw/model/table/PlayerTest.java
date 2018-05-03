package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.exception.CellNotFoundException;
import it.polimi.ingsw.model.exception.IllegalDashboardException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class PlayerTest {

    @Test
    void getNickname() {
        Player player = new Player("player");
        Assertions.assertEquals(player.getNickname(),"player");
    }

    @Test
    void getTokens() {
        Player player = new Player("player");
        for(int i = 3; i<7; i++) {
            player.setTokens(i);
            Assertions.assertEquals(player.getTokens(), i);
        }

    }

    @Test
    void setConnected() {
        Player player = new Player("player");
        player.setConnected(true);
        Assertions.assertTrue(player.isConnected());
        player.setConnected(false);
        Assertions.assertFalse(player.isConnected());
    }
}

