package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.EndGameInfo;
import it.polimi.ingsw.client.view.factory.CliViewFactory;
import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class CliEndScreenTest {



    @DisplayName("Testing end game cli view")
    @Test
    void endGame() throws FileNotFoundException {
        String fileOut = "resources/testResources/endCliOut.txt";
        String fileIn = "resources/testResources/endCliIn.txt";
        String fileExpected = "resources/testResources/endCliExpected.txt";

        CliEndScreen cliEndScreen = new CliEndScreen(new FileInputStream(fileIn), new PrintStream(new FileOutputStream(fileOut)));

        List<Pair<String,Integer>> ranking = new ArrayList<>();
        ranking.add(new Pair<>("player1",42));
        ranking.add(new Pair<>("player3",39));
        ranking.add(new Pair<>("player2",32));
        ranking.add(new Pair<>("player4",30));
        EndGameInfo endGameInfo = new EndGameInfo(ranking);
        cliEndScreen.showRanking(endGameInfo);
        cliEndScreen.playAgain();

        Scanner scanner1 = new Scanner(new FileInputStream(fileOut));
        Scanner scanner2 = new Scanner(new FileInputStream(fileExpected));
        while(scanner1.hasNext()){
            Assertions.assertEquals(scanner1.next(), scanner2.next());
        }
    }
}