package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.shared.LogMaker;
import it.polimi.ingsw.server.controller.channels.CommunicationChannel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The channels are added to the lobby witch when full or after a timer has elapsed starts the game
 */
class Lobby {

    private static final Logger logger = LogMaker.getLogger(Lobby.class.getName(), Level.ALL);
    private static final int MAX_PLAYERS = 4;
    private static long timerSeconds;
    private Set<CommunicationChannel> commChannelSet;
    private Timer timer;


    private static final String TIMER_SECONDS = "timerSeconds";
    private static final long STD_TIMER_SECONDS = 60;
    private static final String CONFIG_PATH = "resources/ServerResources/config.json";

    /*
     * Lobby configuration
     */
    static{
        JSONObject config;
        try {
            JSONParser parser = new JSONParser();
            config = (JSONObject)parser.parse(new FileReader(new File(CONFIG_PATH)));
            timerSeconds = (long)config.get(TIMER_SECONDS);
        } catch (ParseException | IOException e) {
            timerSeconds = STD_TIMER_SECONDS;
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    Lobby(){
        commChannelSet = new HashSet<>();
    }

    /**
     * adds a channel, a client, to the lobby
     * @param communicationChannel to add
     */
    synchronized void addChannel(CommunicationChannel communicationChannel){
        logger.log(Level.FINER, "Adding channel to lobby", communicationChannel);
        commChannelSet.add(communicationChannel);
        if(commChannelSet.size() == 2){
            startTimer();
        }
        if(commChannelSet.size() == MAX_PLAYERS){
            logger.log(Level.FINER, MAX_PLAYERS + " players, game starts");
            timer.cancel();
            startGame();
        }
    }


    /**
     * Returns a copied set og the channels in the lobby
     * @return the collection of channels
     */
    synchronized Collection<CommunicationChannel> getCommChannelSet() {
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

    private synchronized void startGame(){
        if(commChannelSet.stream().filter(cc -> !cc.isOffline()).count() > 1) {
            Game game = new Game(commChannelSet);
            Server.addGame(game);
            commChannelSet = new HashSet<>();
        }else{
            timer.cancel();
            commChannelSet = commChannelSet.stream().filter(cc -> !cc.isOffline()).collect(Collectors.toSet());
        }
    }

    /**
     * Add a new communicationChannel removing, if present, one with the same nickname
     * @param newCc communicationChannel to add
     */
    synchronized void changeChannel(CommunicationChannel newCc){
        commChannelSet = getCommChannelSet().stream()
                .filter(cc -> !cc.getNickname().equals(newCc.getNickname()))
                .collect(Collectors.toSet());
        commChannelSet.add(newCc);
    }
}
