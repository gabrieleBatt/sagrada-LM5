package it.polimi.ingsw.server.controller.deck;

import it.polimi.ingsw.shared.LogMaker;
import it.polimi.ingsw.shared.Message;
import it.polimi.ingsw.shared.identifiables.Identifiable;
import it.polimi.ingsw.shared.identifiables.StdId;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.exception.DeckTooSmallException;
import it.polimi.ingsw.server.controller.rules.ActionCommand;
import it.polimi.ingsw.server.controller.rules.DefaultRules;
import it.polimi.ingsw.server.controller.rules.ToolActions;
import it.polimi.ingsw.server.model.tool.Tool;
import it.polimi.ingsw.server.model.tool.ToolConditions;
import javafx.scene.shape.Mesh;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * ToolDeck is a concrete class representing the deck of tool cards.
 */
public class ToolDeck extends Deck {

    private static final Logger logger = LogMaker.getLogger(ToolDeck.class.getName(), Level.ALL);
    private static final String MOVE = "move";
    private static final String SELECT = "select";
    private static final String SET = "set";
    private static final String RANDOM = "random";
    private static final String SWAP = "swap";
    private static final String DRAFT = "draft";
    private static final String PLACE = "place";
    private static final String ROLL_POOL = "rollPool";
    private static final String ACTION_NUM = "actions";
    private static final String ACTION = "action";
    private static final String SKIP_TURN = "skipNextTurn";
    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String NUMBER_RESTRICTION = "NUMBER";
    private static final String COLOR_RESTRICTION = "COLOR";
    private static final String CLOSE_RESTRICTION = "CLOSE";
    private static final String COLOR = "color";
    private static final String NUMBER = "number";
    private static final String CONDITIONS = "conditions";
    private static final String RESTRICTION = "restriction";
    private static final String FORCED = "forced";
    private static final String CAN_SKIP = "canSkip";
    private static final String FROM = "from";
    private static final String MARKER = "marker";
    private static final String WITH = "with";
    private static final String MARKER_CONTAINER = "markerInContainer";
    private static final String MARKER_MAP = "markerInMap";
    private static ToolDeck toolDeck = new ToolDeck(Paths.get("resources/serverResources/tools"));

    /**
     * ToolDeck builder. Tool cards are generated form resources.
     */
    private ToolDeck(Path path){
        super(path);
    }

    /**
     * Gets tool deck.
     * @return tool deck.
     */
    public static ToolDeck getToolDeck() {
        return toolDeck;
    }

    @Override
    public List<Tool> draw(int num){
        List<Tool> ret = new ArrayList<>();

        if(getPaths().size() < num) throw new DeckTooSmallException(num + " tools requested, " + getPaths());
        Set<Integer> integerSet = new HashSet<>();
        while(integerSet.size() < num) {
            integerSet.add(ThreadLocalRandom.current().nextInt(0, getPaths().size()));
        }

        for(Integer i: integerSet){
            Optional<JSONObject> optional = parse(getPaths().get(i).toFile());
            optional.ifPresent(jsonObject -> ret.add(readCard(jsonObject)));
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
        for (long i = 1; i <= (long) jsonObject.get(ACTION_NUM); i++) {
            JSONObject jo = (JSONObject) jsonObject.get(ACTION+i);
            Map<String, Function<JSONObject, ActionCommand>> operations = new HashMap<>();
            operations.put(MOVE, ToolDeck::getMove);
            operations.put(SELECT, ToolDeck::getSelect);
            operations.put(SET, ToolDeck::getSet);
            operations.put(RANDOM, ToolDeck::getRandom);
            operations.put(SWAP, ToolDeck::getSwap);
            operations.put(DRAFT, ToolDeck::getDraft);
            operations.put(PLACE, ToolDeck::getPlace);
            operations.put(ROLL_POOL, j -> ar -> {
                ar.getTable().getPool().roll();
                ar.getCommChannels().forEach(cc -> cc.updateView(ar.getTable().getPool()));
                ar.sendAll(Message.NEW_POOL.name());
            });
            operations.put(SKIP_TURN, j -> Game::skipNextTurn);

            actionCommands.add(operations.get(jo.get(TYPE).toString()).apply(jo));

        }

        List<String> strings = new ArrayList<>((JSONArray)jsonObject.get(CONDITIONS));
        List<ToolConditions> toolConditions = strings
                .stream()
                .map(ToolConditions::valueOf)
                .collect(Collectors.toList());

        return new Tool(actionCommands, jsonObject.get(NAME).toString(), toolConditions);
    }

    /**
     * Gets the actionCommand performing draft.
     * @param jsonObject Object read from json file.
     * @return ActionCommand performing draft.
     */
    private static ActionCommand getDraft(JSONObject jsonObject) {
        return DefaultRules.getDefaultRules().getDraftAction(
                getMarker(jsonObject),
                getColor(jsonObject),
                getNumber(jsonObject)
        );
    }

    /**
     * Gets the ActionCommand performing swap.
     * @param jsonObject Object read from json file.
     * @return ActionCommand performing swap.
     */
    private static ActionCommand getSwap(JSONObject jsonObject) {
        return ToolActions.swapActionCommand(
                getColor(jsonObject),
                getNumber(jsonObject),
                StdId.getStdId((String)jsonObject.get(WITH)),
                (String)jsonObject.get(MARKER_CONTAINER),
                (String)jsonObject.get(MARKER_MAP)
                );
    }

    /**
     * Gets the ActionCommand performing Random.
     * @param jsonObject Object read from json file.
     * @return ActionCommand performing Random.
     */
    private static ActionCommand getRandom(JSONObject jsonObject) {
        return ToolActions.randomActionCommand(
                getMarker(jsonObject),
                getFunction(jsonObject)

        );
    }

    /**
     * Gets the ActionCommand performing place.
     * @param jsonObject Object read from json file.
     * @return ActionCommand performing place.
     */
    private static ActionCommand getPlace(JSONObject jsonObject) {
        List<String> restrictions = new ArrayList<>((JSONArray)jsonObject.get(RESTRICTION));
        return DefaultRules.getDefaultRules().getPlaceAction(
                        getMarker(jsonObject),
                        restrictions.contains(CLOSE_RESTRICTION),
                        restrictions.contains(COLOR_RESTRICTION),
                        restrictions.contains(NUMBER_RESTRICTION),
                        jsonObject.containsKey(FORCED) && (Boolean)jsonObject.get(FORCED)

                );

    }

    /**
     * Gets the ActionCommand performing set.
     * @param jsonObject Object read from json file.
     * @return ActionCommand performing set.
     */
    private static ActionCommand getSet(JSONObject jsonObject) {
        return ToolActions
                .setActionCommand(
                        getMarker(jsonObject),
                        getFunction(jsonObject));
    }

    /**
     * Reads the jsonObject obtaining the List of List of identifiable, options.
     * @param jsonObject Object read from json file.
     * @return A List of List of identifiable, options.
     */
    private static List<List<Identifiable>> getFunction(JSONObject jsonObject) {
        List<List<Identifiable>> identifiableList = new ArrayList<>();
        for (long i = 1; i <=6; i++) {
            List<Long> longs = new ArrayList<>((JSONArray)jsonObject.get(SET+i));
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
    private static ActionCommand getMove(JSONObject jsonObject){
        List<String> restrictions = new ArrayList<>((JSONArray)jsonObject.get(RESTRICTION));
        return ToolActions
                .moveActionCommand(
                        getColor(jsonObject),
                        getNumber(jsonObject),
                        !restrictions.contains(COLOR_RESTRICTION),
                        !restrictions.contains(NUMBER_RESTRICTION),
                        !restrictions.contains(CLOSE_RESTRICTION),
                        jsonObject.containsKey(CAN_SKIP) && (Boolean)jsonObject.get(CAN_SKIP)

                );
    }

    /**
     * Gets the ActionCommand performing select.
     * @param jsonObject Object read from json file.
     * @return ActionCommand performing select.
     */
    private static ActionCommand getSelect(JSONObject jsonObject){
        return ToolActions.selectActionCommand(
                getMarker(jsonObject),
                StdId.getStdId((String)jsonObject.get(FROM))
        );
    }

    /**
     * Gets the String representing die color.
     * @param jo Object read from json file.
     * @return String, color.
     */
    private static String getColor(JSONObject jo){
        return (String)jo.get(COLOR);
    }

    /**
     * Gets the String representing die number.
     * @param jo Object read from json file.
     * @return String, number.
     */
    private static String getNumber(JSONObject jo){
        return (String)jo.get(NUMBER);
    }

    /**
     * Gets the String representing die marker.
     * @param jo Object read from json file.
     * @return String, marker.
     */
    private static String getMarker(JSONObject jo){
        return (String)jo.get(MARKER);
    }
}
