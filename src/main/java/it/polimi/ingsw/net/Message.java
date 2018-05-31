package it.polimi.ingsw.net;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
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
    PLACE_DONE();

   private String value;

    Message() {
        try {
            JSONObject config = (JSONObject) new JSONParser().parse(new FileReader("resources/clientResources/config.json"));
            String language = ((String) config.get("language")).toLowerCase();
            JSONObject translator = (JSONObject) new JSONParser().parse(new FileReader("resources/clientResources/lang/"+language+"/"+language+".json"));
            Optional.ofNullable((String)translator.get(this.name())).ifPresent(s -> this.value = s);
        } catch (IOException | ParseException e) {
            this.value = this.toString();
        }
    }

    public static String  convertMessage(String defaultMessage){
        Optional<Message> optional =  Arrays
                .stream(Message.values())
                .filter(m -> m.name().equals(defaultMessage))
                .findFirst();
        if(optional.isPresent()){
            return optional.get().toString();
        }else{
            throw new IllegalArgumentException();
        }
    }

    public static String decodeMessage(String choice) {
        Optional<Message> optional =  Arrays
                .stream(Message.values())
                .filter(m -> m.value.equals(choice))
                .findFirst();
        if(optional.isPresent()){
            return optional.get().name();
        }else{
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return value;
    }

    public static String convertWindowName(String name){
        Matcher matcher = Pattern.compile("[A-Z]").matcher(name);
        StringBuffer buff = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(buff," " + matcher.group());
        }
        return new String(matcher.appendTail(buff).replace(0,1, ""+Character.toUpperCase(buff.charAt(0))));
    }

    public static String decodeWindowName(String name){
        Matcher matcher = Pattern.compile("\\s").matcher(name);
        StringBuffer buff = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(buff,"");
        }
        return new String(matcher.appendTail(buff).replace(0,1, ""+Character.toLowerCase(buff.charAt(0))));
    }

}
