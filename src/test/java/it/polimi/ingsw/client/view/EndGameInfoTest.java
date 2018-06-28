package it.polimi.ingsw.client.view;

import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class EndGameInfoTest {

    @DisplayName("End game info creation")
    @Test
    void getRanking() {
        List<Pair<String,Integer>> ranking = new ArrayList<>();
        Assertions.assertEquals(new EndGameInfo(ranking).getRanking(), ranking);
    }
}