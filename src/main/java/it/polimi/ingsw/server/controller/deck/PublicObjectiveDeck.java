package it.polimi.ingsw.server.controller.deck;

import it.polimi.ingsw.shared.LogMaker;
import it.polimi.ingsw.server.exception.DeckTooSmallException;
import it.polimi.ingsw.server.model.objective.AreaPublicObjective;
import it.polimi.ingsw.server.model.objective.Coordinate;
import it.polimi.ingsw.server.model.objective.PublicObjective;
import it.polimi.ingsw.server.model.objective.SetPublicObjective;
import it.polimi.ingsw.server.model.table.dice.DieColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PublicObjectiveDeck extends Deck {

    private static final Logger logger = LogMaker.getLogger(PublicObjectiveDeck.class.getName(), Level.ALL);
    private static final String TYPE = "type";
    private static final String AREA = "area";
    private static final String MULTIPLICITY = "multiplicity";
    private static final String OBJECTIVE = "obj";
    private static final String SET = "set";
    private static final String POINTS = "points";
    private static PublicObjectiveDeck publicObjectiveDeck = new PublicObjectiveDeck(Paths.get("resources/serverResources/objectives/public"));

    private PublicObjectiveDeck(Path path){
        super(path);
    }

    public static PublicObjectiveDeck getPublicObjectiveDeck() {
        return publicObjectiveDeck;
    }

    @Override
    public List<PublicObjective> draw(int num){
        List<PublicObjective> ret = new ArrayList<>();

        if(getPaths().size() < num) throw new DeckTooSmallException(num + " cards requested, " + getPaths() + " in deck");

        Set<Integer> integerSet = new HashSet<>();
        while(integerSet.size() < num) {
            integerSet.add(ThreadLocalRandom.current().nextInt(0, getPaths().size()));
        }

        for(Integer i: integerSet){
            Optional<JSONObject> optional = parse(getPaths().get(i).toFile());
            optional.ifPresent(jsonObject -> ret.add(readCard(jsonObject)));
        }


        return ret;
    }


    private PublicObjective readCard(JSONObject jsonObject){
        if ((jsonObject.get(TYPE)).equals(AREA)) return readAreaCard(jsonObject);
        else return readSetCard(jsonObject);
    }

    private PublicObjective readSetCard(JSONObject jsonObject) {

        Iterator<String> iterator = ((JSONArray)jsonObject.get(SET)).iterator();

        List<Integer> numbers = new ArrayList<>();
        List<DieColor> colors = new ArrayList<>();

        while(iterator.hasNext()){
            String s = iterator.next();
            if(Arrays.stream(DieColor.values()).map(DieColor::name).collect(Collectors.toList()).contains(s))
                colors.add(DieColor.valueOf(s));
            else
                numbers.add(Integer.decode(s));
        }

        return new SetPublicObjective((String)jsonObject.get(NAME),  Math.toIntExact((long)jsonObject.get(POINTS)),
                numbers, colors);
    }

    private void addMult(String s, JSONObject multiplicity, List<List<Integer>> mult ){
        mult.add(new ArrayList<>());
        for (Long aLong : (Iterable<Long>) (multiplicity.get(s))) {
            mult.get(mult.size() - 1).add(Math.toIntExact(aLong));
        }
    }

    private void addAllMult(JSONObject multiplicity, List<List<Integer>> mult){
        for (Integer i = 1; i <= 6; i++) {
            addMult(i.toString(), multiplicity, mult);
        }
        for (DieColor dieColor : DieColor.values()) {
            addMult(dieColor.name(), multiplicity, mult);
        }
    }


    private PublicObjective readAreaCard(JSONObject jsonPublicObjective) {
        String name = (String) jsonPublicObjective.get(NAME);
        int points = Math.toIntExact((long)jsonPublicObjective.get(POINTS));


        Iterator<JSONObject> iterator = ((JSONArray)jsonPublicObjective.get(OBJECTIVE)).iterator();
        JSONObject obj = iterator.next();

        List<Coordinate> area = new ArrayList<>();
        for (JSONArray coordinate : (Iterable<JSONArray>) (obj.get(AREA))) {
            area.add(new Coordinate((Math.toIntExact((long) coordinate.get(0))), Math.toIntExact((long) coordinate.get(1))));
        }

        List<List<Integer>> mult = new ArrayList<>();
        JSONObject multiplicity = (JSONObject)obj.get(MULTIPLICITY);

        addAllMult(multiplicity, mult);

        AreaPublicObjective ret = null;
        ret = new AreaPublicObjective(name, points, area, mult);


        while(iterator.hasNext()){
            obj = iterator.next();
            area = new ArrayList<>();
            for (JSONArray coordinate : (Iterable<JSONArray>) ((JSONArray) obj.get(AREA))) {
                area.add(new Coordinate((Math.toIntExact((long) coordinate.get(0))), Math.toIntExact((long) coordinate.get(1))));
            }

            mult = new ArrayList<>();
            multiplicity = (JSONObject)obj.get(MULTIPLICITY);

            addAllMult(multiplicity, mult);

            ret.addArea(area, mult);
        }


        return ret;

    }

}
