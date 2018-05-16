package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.controller.commChannel.CommunicationChannel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The channels are added to the lobby witch when full or after a timer has elapsed starts the game
 */
public class Lobby {

    private static final Logger logger = LogMaker.getLogger(Lobby.class.getName(), Level.ALL);
    private static int timerSeconds;
    private Set<CommunicationChannel> commChannelSet;
    private Timer timer;

    static{
        JSONObject config = null;
        try {
            JSONParser parser = new JSONParser();
            config = (JSONObject)parser.parse(new FileReader(new File("resources/ServerResources/config.json")));
            timerSeconds = Math.toIntExact((long)config.get("timerSeconds"));
        } catch (ParseException | IOException e) {
            timerSeconds = 60;
        }
    }

    public Lobby(){
        commChannelSet = new HashSet<>();
    }

    /**
     * adds a channel, a client, to the lobby
     * @param communicationChannel
     */
    public void addChannel(CommunicationChannel communicationChannel){
        logger.log(Level.FINER, "Adding channel to lobby", communicationChannel);
        commChannelSet.add(communicationChannel);
        if(commChannelSet.size() > 1){
            startTimer();
        }
        if(commChannelSet.size() == 4){
            logger.log(Level.FINER, "4 players, game starts");
            timer.cancel();
            startGame();
        }
    }

    /**
     * Returns a copied set og the channels in the lobby
     * @return the collection of channels
     */
    public Collection<CommunicationChannel> getCommChannelSet() {
        return new HashSet<>(commChannelSet);
    }

    private void startTimer() {
        logger.log(Level.FINER, "Starting lobby timer");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                logger.log(Level.FINER, "Time's up, game starts" );
                startGame();
                timer.cancel();
            }
        }, timerSeconds * 1000);
    }

    private void startGame(){
        Game game = new Game(commChannelSet);
        Server.addGame(game);
        commChannelSet = new HashSet<>();
    }

    public Collection<CommunicationChannel> getCommChannels() {
        return new HashSet<>(commChannelSet);
    }

    public void changeChannel(CommunicationChannel newCc){
        commChannelSet = getCommChannels().stream()
                .filter(cc -> !cc.getNickname().equals(newCc.getNickname()))
                .collect(Collectors.toSet());
        commChannelSet.add(newCc);
    }
}
