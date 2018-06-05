package it.polimi.ingsw.shared;

import it.polimi.ingsw.client.Client;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Message {

    YES(),
    NO(),
    END_GAME_MESSAGE(),
    RE_CONNECT(),
    MESSAGES(),
    REPLAY(),
    WINS(),
    LEADER_BOARD,
    CHOOSE_CONNECTION(),
    CHOOSE_NICKNAME(),
    INSERT_PASSWORD(),
    CREDENTIAL_POLICY(),
    PORT_NUMBER(),
    IP_NUMBER(),
    CHOOSE_WINDOW(),
    INVALID_CHOICE(),
    PRIVATE_OBJ(),
    PUBLIC_OBJ(),
    TOOL(),
    TOKENS(),
    ONLINE(),
    OFFLINE(),
    ROUND(),
    DRAFT_POOL(),
    POINTS(),
    SKIP(),
    UNDO(),
    ROUND_TRACK(),
    NEXT_MOVE(),
    USE_TOOL(),
    DRAFT(),
    DRAFT_DONE(),
    PLACE_DONE(),
    DEALT_TOOLS(),
    DEALT_PUB_OBJ(),
    RECEIVED_GLASS_WINDOW(),
    NEW_POOL(),
    END_ROUND(),
    END_TURN(),
    START_TURN(),
    RANDOM_NUMBER(),
    SELECT_DIE(),
    SWAPPED(),
    MOVED(),
    BEEN_SET(),
    SET_DIE();

    private String value;

    Message() {
        try {
            JSONObject config = (JSONObject) new JSONParser().parse(new InputStreamReader(Message.class.getClassLoader().getResourceAsStream("clientResources/config.json")));
            String language = ((String) config.get("language")).toLowerCase();
            JSONObject translator = (JSONObject) new JSONParser().parse(new InputStreamReader(Message.class.getClassLoader().getResourceAsStream("clientResources/lang/"+language+"/"+language+".json")));
            Optional.ofNullable((String)translator.get(this.name())).ifPresent(s -> this.value = s);
        } catch (IOException | ParseException e) {
            Client.getLogger().log(Level.WARNING, "Config file not found", e);
            this.value = null;
        }
    }

    @Override
    public String toString() {
        if (value == null){
            value = this.name();
        }
        return value;
    }

    public static String  convertMessage(String defaultMessage){
        Optional<Message> optional =  Arrays
                .stream(Message.values())
                .filter(m -> m.name().equals(defaultMessage))
                .findFirst();
        if(optional.isPresent()){
            return optional.get().toString();
        }else{
            return defaultMessage;
        }
    }

    public static String decodeMessage(String choice) {
        Optional<Message> optional =  Arrays
                .stream(Message.values())
                .filter(m -> choice.equals(m.value))
                .findFirst();
        if(optional.isPresent()){
            return optional.get().name();
        }else{
            return choice;
        }
    }

    public static String convertName(String name){
        Matcher matcher = Pattern.compile("[A-Z]").matcher(name);
        StringBuffer buff = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(buff," " + matcher.group());
        }
        return new String(matcher.appendTail(buff).replace(0,1, ""+Character.toUpperCase(buff.charAt(0))));
    }

    public static String decodeName(String name){
        Matcher matcher = Pattern.compile("\\s").matcher(name);
        StringBuffer buff = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(buff,"");
        }
        return new String(matcher.appendTail(buff).replace(0,1, ""+Character.toLowerCase(buff.charAt(0))));
    }

}
