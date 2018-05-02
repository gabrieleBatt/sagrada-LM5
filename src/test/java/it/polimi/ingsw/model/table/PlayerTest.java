package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.exception.DashBoardException;
import it.polimi.ingsw.model.objective.PrivateObjective;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void getNickname() {
        Player player = new Player("player");
        Assertions.assertEquals(player.getNickname(),"player");
    }

    @Test
    void getDashBoard() throws DashBoardException {
        String nome = "nome";
        Player player = new Player(nome);
        Assertions.assertThrows(DashBoardException.class, () -> player.getDashBoard());
        DashBoard dashBoard = new DashBoard("name",'d');
        player.setDashBoard(dashBoard);
        Assertions.assertEquals(dashBoard, player.getDashBoard());
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
    void getPrivateObjective() {
    }

    @Test
    void addPrivateObjective() {
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

