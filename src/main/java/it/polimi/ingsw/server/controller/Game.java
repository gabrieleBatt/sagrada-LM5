package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.controller.channels.CommunicationChannel;
import it.polimi.ingsw.server.exception.*;
import it.polimi.ingsw.server.model.rules.ActionCommand;
import it.polimi.ingsw.server.model.rules.DefaultRules;
import it.polimi.ingsw.server.model.rules.Rules;
import it.polimi.ingsw.server.model.rules.TurnActionCommand;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.Table;
import it.polimi.ingsw.server.model.table.dice.Die;
import javafx.util.Pair;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Game implements Runnable {

    private static final Logger logger = LogMaker.getLogger(Game.class.getName(), Level.ALL);
    private Table table;
    private List<CommunicationChannel> commChannels;
    private HashMap<String, Die> dice;
    private List<ActionCommand> actionCommandList;
    private final Rules rules;
    private List<Pair<Player,Integer>> ranking;

    //TODO--missing effects

    public Game(Collection<CommunicationChannel> commChannels){
        this.rules = DefaultRules.getDefaultRules();
        this.commChannels = new ArrayList<>(commChannels);
        this.table = new Table(commChannels.stream()
                .map(cc -> new Player(cc.getNickname()))
                .collect(Collectors.toList()));
        this.dice = new HashMap<>();
        this.actionCommandList = new ArrayList<>();

        actionCommandList.addAll(rules.getSetupGameActions());

        Iterator<Player> players = getTable()
                .getPlayersIterator(getTable().getPlayers().get(0), true, false);
        for (int i = 0; i < 10; i++) {
            actionCommandList.add(rules.getSetupRoundAction());
            Player firstOfRound = players.next();
            Iterator<Player> roundIterator = getTable().getPlayersIterator(firstOfRound, false, false);
            while (roundIterator.hasNext()){
                actionCommandList.add(rules.getTurnAction(roundIterator.next()));
            }
            roundIterator = getTable().getPlayersIterator(firstOfRound, false, true);
            while (roundIterator.hasNext()){
                actionCommandList.add(rules.getTurnAction(roundIterator.next()));
            }
            actionCommandList.add(rules.getEndRoundAction());
        }
        actionCommandList.add(rules.getEndGameAction());
    }

    public Rules getRules() {
        return rules;
    }

    /**
     * Executes all action in list until empty
     */
    @Override
    public void run() {
        for (CommunicationChannel commChannel : commChannels) {
            commChannel.updateView(table);
        }
        while(!actionCommandList.isEmpty()){
            try {
                actionCommandList.get(0).execute(this);
                actionCommandList.remove(0);
            } catch (DieNotAllowedException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
        }
    }

    /**
     * Reset the turn when called if the current action is a TurnActionCommand
     */
    public void resetTurn(){
        if(actionCommandList.get(0) instanceof TurnActionCommand){
            ((TurnActionCommand) actionCommandList.get(0)).reset(this);
        }
    }

    /**
     * Ends game giving to every player the ranking's game.
     * @param ranking Pair of Player and Integer, the points scored.
     */
    public synchronized void endGame(List<Pair<Player,Integer>> ranking){
        this.ranking = ranking;
        commChannels.forEach(c -> c.endGame(ranking));
        Server.endGame(this);
    }

    /**
     * Gets a ranking copy.
     * @return a List of players and respective score.
     */
    public List<Pair<Player,Integer>> getRanking(){
        return new ArrayList<>(this.ranking);
    }
    /**
     * Gets the HashMap of chosen die.
     * @return HashMap of chosen die.
     */
    public Map<String,Die> getMap(){
        return this.dice;
    }

    /**
     * Gets the logger.
     * @return logger.
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * Gets table.
     * @return Object, table.
     */
    public Table getTable(){
        return this.table;
    }

    /**
     * Gets the collection of communication channel.
     * @return Collection og communication channel.
     */
    public synchronized Collection<CommunicationChannel> getCommChannels() {
        return new ArrayList<>(commChannels);
    }


    /**
     * Add a new communicationChannel removing, if present, one with the same nickname
     * @param newCc communicationChannel to add
     */
    public synchronized void changeChannel(CommunicationChannel newCc){
        commChannels = getCommChannels().stream()
                .filter(cc -> !cc.getNickname().equals(newCc.getNickname()))
                .collect(Collectors.toList());
        commChannels.add(newCc);
    }

    /**
     * returns the channel with the nickname specified
     * @param nickname of the channel to get
     * @return the channel with the nickname specified
     */
    public CommunicationChannel getChannel(String nickname){
        Optional<CommunicationChannel> ret = getCommChannels()
                .stream()
                .filter(cc -> cc.getNickname().equals(nickname))
                .findFirst();
        if(ret.isPresent()){
            return ret.get();
        }
        throw new NoSuchElementException();
    }


}
