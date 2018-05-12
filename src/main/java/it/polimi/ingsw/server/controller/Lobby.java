package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.controller.commChannel.CommunicationChannel;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The channels are added to the lobby witch when full or after a timer has elapsed starts the game
 */
public class Lobby {

    private static final Logger logger = LogMaker.getLogger(Lobby.class.getName(), Level.ALL);
    private static final int timerSeconds = 10;
    private Set<it.polimi.ingsw.server.controller.CommunicationChannel> commChannelSet;
    private Timer timer;

    public Lobby(){
        commChannelSet = new HashSet<>();
    }

    /**
     * adds a channel, a client, to the lobby
     * @param communicationChannel
     */
    public void addChannel(it.polimi.ingsw.server.controller.CommunicationChannel communicationChannel){
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
    public Collection<it.polimi.ingsw.server.controller.CommunicationChannel> getCommChannelSet() {
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
        //TODO
        commChannelSet = new HashSet<>();
    }

}
