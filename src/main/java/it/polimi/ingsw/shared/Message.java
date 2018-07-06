package it.polimi.ingsw.shared;

import it.polimi.ingsw.client.Client;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
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
    RECEIVED_GLASS_WINDOW(),
    NEW_POOL(),
    END_ROUND(),
    START_TURN(),
    RANDOM_NUMBER(),
    SELECT_DIE(),
    SWAPPED(),
    MOVED(),
    BEEN_SET(),
    SET_DIE(),
    FORCED_IN_POOL(),
    INCOMPLETE_FIELDS(),
    WAITING_OTHER_PLAYERS(),
    WELCOME(),
    START_GAME(),
    TIMER(),
    PLAY(),
    SHOW_MESSAGES(),
    SEPARATOR();

    private String value;

    /**
     * For each message gets its translation according to the language field in config
     */
    Message() {
        try {
            JSONObject config = (JSONObject) new JSONParser().parse(new InputStreamReader(new FileInputStream("resources/clientResources/config.json")));
            String language = ((String) config.get("language")).toLowerCase();
            JSONObject translator = (JSONObject) new JSONParser().parse(new InputStreamReader(new FileInputStream("resources/clientResources/lang/"+language+"/"+language+".json")));
            Optional.ofNullable((String)translator.get(this.name())).ifPresent(s -> this.value = s);
        } catch (ParseException e) {
            Client.getLogger().log(Level.WARNING, "Config file not found", e);
            this.value = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the translated message if present
     * @return the translated message if present
     */
    @Override
    public String toString() {
        if (value == null){
            value = this.name();
        }
        return value;
    }

    /**
     * Returns the translation if present, if not returns the defaultMessage unchanged
     * @param defaultMessage to convert
     * @return the translation if present, if not returns the defaultMessage unchanged
     */
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

    /**
     * Returns the standard message if present, if not returns the message unchanged
     * @param message to decode
     * @return the standard message if present, if not returns the message unchanged
     */
    public static String decodeMessage(String message) {
        Optional<Message> optional =  Arrays
                .stream(Message.values())
                .filter(m -> message.equals(m.value))
                .findFirst();
        if(optional.isPresent()){
            return optional.get().name();
        }else{
            return message;
        }
    }

    /**
     * Convert a name, placing a space before each upper case letter
     * @param name to convert
     * @return the converted name
     */
    public static String convertName(String name){
        Matcher matcher = Pattern.compile("[A-Z]").matcher(name);
        StringBuffer buff = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(buff," " + matcher.group());
        }
        return new String(matcher.appendTail(buff).replace(0,1, ""+Character.toUpperCase(buff.charAt(0))));
    }

    /**
     * Decode a name, removing the space before each upper case letter
     * @param name to decode
     * @return the decoded name
     */
    public static String decodeName(String name){
        Matcher matcher = Pattern.compile("\\s").matcher(name);
        StringBuffer buff = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(buff,"");
        }
        return new String(matcher.appendTail(buff).replace(0,1, ""+Character.toLowerCase(buff.charAt(0))));
    }

}
