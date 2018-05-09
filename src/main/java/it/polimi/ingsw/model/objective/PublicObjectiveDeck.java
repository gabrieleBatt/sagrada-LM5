package it.polimi.ingsw.model.objective;

import it.polimi.ingsw.model.exception.DeckTooSmallException;
import it.polimi.ingsw.model.exception.IllegalGlassWindowException;
import it.polimi.ingsw.model.exception.IllegalObjectiveException;
import it.polimi.ingsw.model.table.Deck;
import it.polimi.ingsw.model.table.dice.DieColor;
import it.polimi.ingsw.model.table.glassWindow.Cell;
import it.polimi.ingsw.model.table.glassWindow.GlassWindow;
import it.polimi.ingsw.model.table.glassWindow.GlassWindowDeck;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class PublicObjectiveDeck implements Deck {

    private static PublicObjectiveDeck publicObjectiveDeck = new PublicObjectiveDeck();
    private List<JSONObject> publicObjectives;

    private PublicObjectiveDeck(){
        publicObjectives = new ArrayList<>();
        try {
            Files.list(Paths.get("resources/ServerResources/objectives/public/area"))
                    .forEach((f) -> addCard(f.toFile()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    private void addCard(File file) {
        JSONParser parser = new JSONParser();
        try {
            publicObjectives.add((JSONObject)parser.parse(new FileReader(file)));
        } catch (IOException | ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    public static PublicObjectiveDeck getPublicObjectiveDeck() {
        return publicObjectiveDeck;
    }

    @Override
    public List<PublicObjective> draw(int num) throws DeckTooSmallException {
        List<PublicObjective> ret = new ArrayList<>();

        if(publicObjectives.size() < num) throw new DeckTooSmallException(num + " cards requested, " + publicObjectives + " in deck");

        Set<Integer> integerSet = new HashSet<>();
        while(integerSet.size() < num) {
            integerSet.add(ThreadLocalRandom.current().nextInt(0, publicObjectives.size()));
        }

        for(Integer i: integerSet){
            ret.add(readCard(publicObjectives.get(i)));

        }


        return ret;
    }

    private void addMult(String s, JSONObject multiplicity, List<List<Integer>> mult ){
        mult.add(new ArrayList<>());
        Iterator<Long> iterator2 = ((JSONArray)multiplicity.get(s)).iterator();
        while(iterator2.hasNext()){
            mult.get(mult.size()-1).add(Math.toIntExact((long)iterator2.next()));
        }
    }

    private PublicObjective readCard(JSONObject jsonPublicObjective) {
        String name = (String) jsonPublicObjective.get("name");
        int points = Math.toIntExact((long)jsonPublicObjective.get("points"));


        Iterator<JSONObject> iterator = ((JSONArray)jsonPublicObjective.get("obj")).iterator();
        JSONObject obj = iterator.next();

        List<Coordinate> area = new ArrayList<>();
        Iterator<JSONArray> iterator1 = ((JSONArray) obj.get("area")).iterator();
        while(iterator1.hasNext()){
            JSONArray coordinate = iterator1.next();
            area.add(new Coordinate((Math.toIntExact((long)coordinate.get(0))), Math.toIntExact((long)coordinate.get(1))));
        }

        List<List<Integer>> mult = new ArrayList<>();
        JSONObject multiplicity = (JSONObject)obj.get("multiplicity");


        addMult("One" , multiplicity, mult);
        addMult("Two", multiplicity, mult);
        addMult("Three" , multiplicity, mult);
        addMult("Four" , multiplicity, mult);
        addMult("Five" , multiplicity, mult);
        addMult("Six" , multiplicity, mult);
        addMult("Cyan" , multiplicity, mult);
        addMult("Green", multiplicity, mult);
        addMult("Magenta" , multiplicity, mult);
        addMult("Red",  multiplicity, mult);
        addMult("Yellow",  multiplicity, mult);


        AreaPublicObjective ret = null;
        try {
            ret = new AreaPublicObjective(name, points, area, mult);
        } catch (IllegalObjectiveException e) {
            System.out.println(e.getMessage());
        }


        while(iterator.hasNext()){
            obj = iterator.next();
            area = new ArrayList<>();
            iterator1 = ((JSONArray) obj.get("area")).iterator();
            while(iterator1.hasNext()){
                JSONArray coordinate = iterator1.next();
                area.add(new Coordinate((Math.toIntExact((long)coordinate.get(0))), Math.toIntExact((long)coordinate.get(1))));
            }

            mult = new ArrayList<>();
            multiplicity = (JSONObject)obj.get("multiplicity");

            addMult("One" , multiplicity, mult);
            addMult("Two", multiplicity, mult);
            addMult("Three" , multiplicity, mult);
            addMult("Four" , multiplicity, mult);
            addMult("Five" , multiplicity, mult);
            addMult("Six" , multiplicity, mult);
            addMult("Cyan" , multiplicity, mult);
            addMult("Green", multiplicity, mult);
            addMult("Magenta" , multiplicity, mult);
            addMult("Red",  multiplicity, mult);
            addMult("Yellow",  multiplicity, mult);

            try {
                ret.addArea(area, mult);
            } catch (IllegalObjectiveException e) {
                System.out.println(e.getMessage());
            }
        }


        return ret;

    }

}
