package it.polimi.ingsw.model.objectives;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PrivateObjectiveDeck {

    public static List<PrivateObjective> draw(){
        List<PrivateObjective> ret = new ArrayList<>();
        Stream<String> stream;
        try (BufferedReader reader = new BufferedReader(new FileReader("PrivateObjectiveDeck.txt")))
        {
            stream = reader.lines();
            //TODO--builder pattern
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

}
