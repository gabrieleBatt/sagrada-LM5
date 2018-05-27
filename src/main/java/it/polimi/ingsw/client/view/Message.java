package it.polimi.ingsw.client.view;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public enum Message {

    CHOOSE_CONNECTION("ChooseConnectionType"),
    CHOOSE_NICKNAME("ChooseNickname"),
    INSERT_PASSWORD("InsertPassword"),
    CREDENTIAL_POLICY("NoCredentialPolicy"),
    PORT_NUMBER("InsertPortNumber"),
    IP_NUMBER("InsertIp"),
    CHOOSE_WINDOW("Choose your window"),
    INVALID_CHOICE("InvalidChoice"),
    PRIVATE_OBJ("PrivateObjective"),
    PUBLIC_OBJ("PublicObjective"),
    TOOL("Tools"),
    TOKENS("Favors"),
    ONLINE("online"),
    OFFLINE("offline"),
    ROUND("Round"),
    DRAFT_POOL("Pool"),
    POINTS("Points"),
    SKIP("skip"),
    UNDO("undo"),
    ROUND_TRACK("RoundTrack"),
    NEXT_MOVE("chooseNextMove"),
    USE_TOOL("useTool"),
    DRAFT("draftDice");

    private Optional<String> message;
    private final String defaultMessage;

    Message(String defaultMessage) {
        this.defaultMessage = defaultMessage;
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader("resources/clientResources/cnst.json"));
            this.message = Optional.ofNullable((String)jsonObject.get(defaultMessage));

        } catch (IOException | ParseException e) {
            this.message = Optional.empty();
        }
    }

    public static String  convertMessage(String defaultMessage){
        Optional<Message> optional =  Arrays
                .stream(Message.values())
                .filter(m -> m.defaultMessage.equals(defaultMessage))
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
                .filter(m -> m.message.isPresent())
                .filter(m -> m.message.get().equals(choice))
                .findFirst();
        if(optional.isPresent()){
            return optional.get().defaultMessage;
        }else{
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return message.orElse(defaultMessage);
    }
}
