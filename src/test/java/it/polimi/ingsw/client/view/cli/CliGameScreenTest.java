package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.factory.GameScreen;
import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CliGameScreenTest {

    @DisplayName("Set and show test")
    @Test
    public void print() throws IOException {
        String fileOut = "resources/testResources/cliOut.txt";
        String fileIn = "resources/testResources/cliIn.txt";
        String fileExpected = "resources/testResources/cliExpected.txt";

        GameScreen gameScreen = new CliGameScreen(new FileInputStream(fileIn), new PrintStream(new FileOutputStream(fileOut)));

        List<String> nicknames = new ArrayList<>();
        nicknames.add("test1");
        nicknames.add("test2");
        nicknames.add("test3");
        gameScreen.setPlayers(nicknames);

        List<String> objective = new ArrayList<>();
        objective.add("test_obj1");
        objective.add("test_obj2");
        gameScreen.setPrivateObjectives(objective);
        gameScreen.setPublicObjective(objective);
        List<String> tools = new ArrayList<>();

        tools.add("Tool1");
        tools.add("Tool2");
        tools.add("Tool3");
        gameScreen.setTools(tools);
        gameScreen.setToolUsed("Tool2", true);

        gameScreen.setCellContent("test3",3,2,"3R9");

        List<String> windowsToChoose = new ArrayList<>();
        windowsToChoose.add("w1");
        windowsToChoose.add("w2");
        windowsToChoose.add("w3");
        windowsToChoose.add("w4");

        gameScreen.getWindow(windowsToChoose);

        List<String> cells = new ArrayList<>();
        cells.add("00:w1");
        cells.add("02:w1");
        cells.add("31:w1");
        cells.add("22:w1");
        gameScreen.getInput(cells, "glassWindow");

        gameScreen.showAll();

        Scanner scanner1 = new Scanner(new FileInputStream(fileOut));
        Scanner scanner2 = new Scanner(new FileInputStream(fileExpected));
        while(scanner1.hasNext()){
            Assertions.assertEquals(scanner1.next(), scanner2.next());
        }

    }

}