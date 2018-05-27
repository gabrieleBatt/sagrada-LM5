package it.polimi.ingsw.server.model.tool;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.net.identifiables.Identifiable;
import it.polimi.ingsw.net.identifiables.StdId;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.exception.DeckTooSmallException;
import it.polimi.ingsw.server.exception.DieNotAllowedException;
import it.polimi.ingsw.server.model.rules.ActionCommand;
import it.polimi.ingsw.server.model.rules.DefaultRules;
import it.polimi.ingsw.server.model.rules.ToolRules;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ToolDeck is a concrete class representing the deck of tool cards.
 */
public class ToolDeck implements Deck {

    private static final Logger logger = LogMaker.getLogger(ToolDeck.class.getName(), Level.ALL);
    private static ToolDeck toolDeck = new ToolDeck();
    private List<JSONObject> tools;

    /**
     * ToolDeck builder. Tool cards are generated form resources.
     */
    private ToolDeck(){
        tools = new ArrayList<>();
        Path path = Paths.get("resources/serverResources/tools");
        try (Stream<Path> files = Files.list(path)){
            files.forEach((f) -> addCard(f.toFile()));
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }

    }

    /**
     * Gets tool deck.
     * @return tool deck.
     */
    public static ToolDeck getToolDeck() {
        return toolDeck;
    }

    private void addCard(File file) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject js = (JSONObject) parser.parse(new FileReader(file));
            tools.add(js);
            logger.log(Level.FINEST, "This tool " + js.get("name") + " has been added to tools", this);

        } catch (IOException | ParseException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }


    @Override
    public List<Tool> draw(int num){
        List<Tool> ret = new ArrayList<>();

        if(tools.size() < num) throw new DeckTooSmallException(num + " tools requested, " + tools );
        Set<Integer> integerSet = new HashSet<>();
        while(integerSet.size() < num) {
            integerSet.add(ThreadLocalRandom.current().nextInt(0, tools.size()));
        }

        for(Integer i: integerSet){
            ret.add(readCard(tools.get(i)));
        }
        logger.log(Level.FINEST, num + " tools have been drawn ", this);
        logger.log(Level.FINEST, " These tools have been added: " + ret, this);

        return ret;
    }

    /**
     * Creates a List of actionCommands depending on action's type.
     * @param jsonObject Object read from json file.
     * @return List of actionCommands depending on action's type.
     */
    private Tool readCard(JSONObject jsonObject) {
        List<ActionCommand> actionCommands = new ArrayList<>();
        for (long i = 1; i <= (long) jsonObject.get("actions"); i++) {
            JSONObject jo = (JSONObject) jsonObject.get("action"+i);
            switch (jo.get("type").toString()){
                case "move":
                    actionCommands.add(getMove(jo));
                    break;
                case "select":
                    actionCommands.add(getSelect(jo));
                    break;
                case "set":
                    actionCommands.add(getSet(jo));
                    break;
                case "random":
                    actionCommands.add(getRandom(jo));
                    break;
                case "swap":
                    actionCommands.add(getSwap(jo));
                    break;
                case "draft":
                    actionCommands.add(getDraft(jo));
                    break;
                case "place":
                    actionCommands.add(getPlace(jo));
                    break;
                case "rollPool":
                    actionCommands.add(ar -> ar.getTable().getPool().roll());
                    break;
            }
        }

        if(jsonObject.containsKey("skipTurn") && (boolean)jsonObject.get("skipTurn")) {
            actionCommands.add(Game::skipNextTurn);
        }
        List<String> strings = new ArrayList<>((JSONArray)jsonObject.get("conditions"));
        List<ToolConditions> toolConditions = strings
                .stream()
                .map(ToolConditions::getCondition)
                .collect(Collectors.toList());

        return new Tool(actionCommands, jsonObject.get("name").toString(), toolConditions);
    }

    /**
     * Gets the actionCommand performing draft.
     * @param jsonObject Object read from json file.
     * @return ActionCommand performing draft.
     */
    private ActionCommand getDraft(JSONObject jsonObject) {
        return DefaultRules.getDefaultRules().getDraftAction(
                getMarker(jsonObject),
                getColor(jsonObject),
                (String)jsonObject.get("number")
        );
    }

    /**
     * Gets the ActionCommand performing swap.
     * @param jsonObject Object read from json file.
     * @return ActionCommand performing swap.
     */
    private ActionCommand getSwap(JSONObject jsonObject) {
        return ToolRules.swapActionCommand(
                getColor(jsonObject),
                getNumber(jsonObject),
                StdId.getStdId((String)jsonObject.get("with")),
                (String)jsonObject.get("markerInContainer"),
                (String)jsonObject.get("markerInMap")
                );
    }

    /**
     * Gets the ActionCommand performing Random.
     * @param jsonObject Object read from json file.
     * @return ActionCommand performing Random.
     */
    private ActionCommand getRandom(JSONObject jsonObject) {
        return ToolRules.randomActionCommand(
                getMarker(jsonObject),
                getFunction(jsonObject)

        );
    }

    /**
     * Gets the ActionCommand performing place.
     * @param jsonObject Object read from json file.
     * @return ActionCommand performing place.
     */
    private ActionCommand getPlace(JSONObject jsonObject) {
        List<String> restrictions = new ArrayList<>((JSONArray)jsonObject.get("restriction"));
        return DefaultRules.getDefaultRules().getPlaceAction(
                        getMarker(jsonObject),
                        restrictions.contains("COLOR"),
                        restrictions.contains("NUMBER"),
                        restrictions.contains("CLOSE"),
                        jsonObject.containsKey("forced") && (Boolean)jsonObject.get("forced")

                );

    }

    /**
     * Gets the ActionCommand performing set.
     * @param jsonObject Object read from json file.
     * @return ActionCommand performing set.
     */
    private ActionCommand getSet(JSONObject jsonObject) {
        return ToolRules
                .setActionCommand(
                        getMarker(jsonObject),
                        getFunction(jsonObject));
    }

    /**
     * Reads the jsonObject obtaining the List of List of identifiable, options.
     * @param jsonObject Object read from json file.
     * @return A List of List of identifiable, options.
     */
    private List<List<Identifiable>> getFunction(JSONObject jsonObject) {
        List<List<Identifiable>> identifiableList = new ArrayList<>();
        for (long i = 1; i <=6; i++) {
            List<Long> longs = new ArrayList<>((JSONArray)jsonObject.get("set"+i));
            identifiableList.add(longs
                    .stream()
                    .map(Object::toString)
                    .map(StdId::getStdId)
                    .collect(Collectors.toList()));
        }
        return identifiableList;
    }

    /**
     * Gets the ActionCommand performing move.
     * @param jsonObject Object read from json file.
     * @return ActionCommand performing move.
     */
    private ActionCommand getMove(JSONObject jsonObject){
        List<String> restrictions = new ArrayList<>((JSONArray)jsonObject.get("restriction"));
        return ToolRules
                .moveActionCommand(
                        getColor(jsonObject),
                        getNumber(jsonObject),
                        restrictions.contains("COLOR"),
                        restrictions.contains("NUMBER"),
                        restrictions.contains("CLOSE"),
                        jsonObject.containsKey("canSkip") && (Boolean)jsonObject.get("canSkip")

                );
    }

    /**
     * Gets the ActionCommand performing select.
     * @param jsonObject Object read from json file.
     * @return ActionCommand performing select.
     */
    private ActionCommand getSelect(JSONObject jsonObject){
        return ToolRules.selectActionCommand(
                getMarker(jsonObject),
                StdId.getStdId((String)jsonObject.get("from"))
        );
    }

    /**
     * Gets the String representing die color.
     * @param jo Object read from json file.
     * @return String, color.
     */
    private String getColor(JSONObject jo){
        return (String)jo.get("color");
    }

    /**
     * Gets the String representing die number.
     * @param jo Object read from json file.
     * @return String, number.
     */
    private String getNumber(JSONObject jo){
        return (String)jo.get("number");
    }

    /**
     * Gets the String representing die marker.
     * @param jo Object read from json file.
     * @return String, marker.
     */
    private String getMarker(JSONObject jo){
        return (String)jo.get("marker");
    }
}
