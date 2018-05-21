package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.factory.GameScreen;
import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CliGameScreenTest {

    @Test
    public void print() throws IOException {
        String fileOut = "resources/testResources/cliOut.txt";
        String fileIn = "resources/testResources/cliIn.txt";
        String fileExpected = "resources/testResources/cliExpected.txt";

        GameScreen gameScreen = new CliGameScreen(new FileInputStream(fileIn), new PrintStream(new FileOutputStream(fileOut)));

        List<Pair<String, Boolean>> nicknames = new ArrayList<>();
        nicknames.add(new Pair<>("test1", true));
        nicknames.add(new Pair<>("test2", true));
        nicknames.add(new Pair<>("test3", true));
        gameScreen.setPlayers(nicknames);

        List<String> objective = new ArrayList<>();
        objective.add("test_obj1");
        objective.add("test_obj2");
        gameScreen.setPrivateObjectives(objective);
        gameScreen.setPublicObjective(objective);
        List<Pair<String, Boolean>> tools = new ArrayList<>();

        tools.add(new Pair("Tool1", true));
        tools.add(new Pair("Tool2", false));
        tools.add(new Pair("Tool3", true));
        gameScreen.setTools(tools);

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
        Assertions.assertTrue(Arrays.equals(Files.readAllBytes(Paths.get(fileOut)), Files.readAllBytes(Paths.get(fileExpected))));

    }

}