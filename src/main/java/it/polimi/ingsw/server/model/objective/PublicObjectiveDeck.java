package it.polimi.ingsw.server.model.objective;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.exception.DeckTooSmallException;
import it.polimi.ingsw.server.model.table.Deck;
import it.polimi.ingsw.server.model.table.dice.DieColor;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class PublicObjectiveDeck implements Deck {

    private static final Logger logger = LogMaker.getLogger(PublicObjectiveDeck.class.getName(), Level.ALL);
    private static PublicObjectiveDeck publicObjectiveDeck = new PublicObjectiveDeck();
    private List<JSONObject> publicObjectives;

    private PublicObjectiveDeck(){
        publicObjectives = new ArrayList<>();
        try {
            Files.list(Paths.get("resources/ServerResources/objectives/public"))
                    .forEach((f) -> addCard(f.toFile()));
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }

    }

    private void addCard(File file) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject js = (JSONObject)parser.parse(new FileReader(file));
            publicObjectives.add(js);
            logger.log(Level.FINEST,  "This public objective "+ js.get("name") +" has been added to publicObjectives", this);
        } catch (IOException | ParseException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    public static PublicObjectiveDeck getPublicObjectiveDeck() {
        return publicObjectiveDeck;
    }

    @Override
    public List<PublicObjective> draw(int num){
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

    private PublicObjective readCard(JSONObject jsonObject){
        if ((jsonObject.get("type")).equals("area")) return readAreaCard(jsonObject);
        else return readSetCard(jsonObject);
    }

    private PublicObjective readSetCard(JSONObject jsonObject) {

        Iterator<String> iterator = ((JSONArray)jsonObject.get("set")).iterator();

        List<Integer> numbers = new ArrayList<>();
        List<DieColor> colors = new ArrayList<>();

        while(iterator.hasNext()){
            String s = iterator.next();
            switch (s){
                case "R":colors.add(DieColor.RED); break;
                case "G":colors.add(DieColor.GREEN); break;
                case "M":colors.add(DieColor.MAGENTA); break;
                case "Y":colors.add(DieColor.YELLOW); break;
                case "C":colors.add(DieColor.CYAN); break;
                default:numbers.add(Integer.decode(s));
            }
        }

        return new SetPublicObjective((String)jsonObject.get("name"),  Math.toIntExact((long)jsonObject.get("points")),
                numbers, colors);
    }

    private void addMult(String s, JSONObject multiplicity, List<List<Integer>> mult ){
        mult.add(new ArrayList<>());
        Iterator<Long> iterator2 = ((JSONArray)multiplicity.get(s)).iterator();
        while(iterator2.hasNext()){
            mult.get(mult.size()-1).add(Math.toIntExact((long)iterator2.next()));
        }
    }


    private PublicObjective readAreaCard(JSONObject jsonPublicObjective) {
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
        ret = new AreaPublicObjective(name, points, area, mult);


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

            ret.addArea(area, mult);
        }


        return ret;

    }

}
