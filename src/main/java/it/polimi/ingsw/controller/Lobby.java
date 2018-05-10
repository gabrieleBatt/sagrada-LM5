package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commChannel.CommunicationChannel;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Lobby {

    private static final int timerSeconds = 10;
    private Set<CommunicationChannel> commChannelSet;
    private Timer timer;

    public Lobby(){
        commChannelSet = new HashSet<>();
    }

    public void addChannel(CommunicationChannel communicationChannel){
        commChannelSet.add(communicationChannel);
        if(commChannelSet.size() > 1){
            startTimer();
        }
        if(commChannelSet.size() == 4){
            startGame();
        }
    }

    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Time's up!");
                System.out.println("The game is starting");
                startGame();
                timer.cancel();
            }
        }, timerSeconds * 1000);
    }

    private void startGame(){
        commChannelSet = new HashSet<>();
    }

}
