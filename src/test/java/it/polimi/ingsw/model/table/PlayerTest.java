package it.polimi.ingsw.model.table;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PlayerTest {

    @DisplayName("Player nickname")
    @Test
    void getNickname() {
        Player player = new Player("player");
        Assertions.assertEquals(player.getNickname(),"player");
    }

    @DisplayName("Player tokens")
    @Test
    void getTokens() {
        Player player = new Player("player");
        for(int i = 3; i<7; i++) {
            player.setTokens(i);
            Assertions.assertEquals(player.getTokens(), i);
        }

    }

    @DisplayName("Player connection")
    @Test
    void setConnected() {
        Player player = new Player("player");
        player.setConnected(true);
        Assertions.assertTrue(player.isConnected());
        player.setConnected(false);
        Assertions.assertFalse(player.isConnected());
    }
}

