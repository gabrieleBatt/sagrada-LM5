package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.shared.LogMaker;
import it.polimi.ingsw.server.controller.channels.CommunicationChannel;
import it.polimi.ingsw.server.controller.rules.ActionCommand;
import it.polimi.ingsw.server.controller.rules.DefaultRules;
import it.polimi.ingsw.server.controller.rules.Rules;
import it.polimi.ingsw.server.controller.rules.TurnActionCommand;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.RoundTrack;
import it.polimi.ingsw.server.model.table.Table;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.shared.Message;
import javafx.util.Pair;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The handler of a single game, using the class
 * Rules gets the actions it has to perform
 */
public class Game implements Runnable {

    private static final Logger logger = LogMaker.getLogger(Game.class.getName(), Level.ALL);
    private Table table;
    private List<CommunicationChannel> commChannels;
    private HashMap<String, Die> dice;
    private final Rules rules;
    private List<Pair<Player,Integer>> ranking;
    private ActionList actionCommandList;


    public Game(Collection<CommunicationChannel> commChannels){
        this.rules = DefaultRules.getDefaultRules();
        this.commChannels = new ArrayList<>(commChannels);
        this.table = new Table(commChannels.stream()
                .map(cc -> new Player(cc.getNickname()))
                .collect(Collectors.toList()));
        this.dice = new HashMap<>();
        this.actionCommandList = new ActionList();

        //setup the game actions to play
        actionCommandList.addAll(rules.getSetupGameActions());
        Iterator<Player> players = getTable()
                .getPlayersIterator(getTable().getPlayers().get(0), true, false);
        //setup rounds
        for (int i = 0; i < RoundTrack.ROUND_NUM; i++) {
            actionCommandList.add(rules.getSetupRoundAction());

            Player firstOfRound = players.next();
            Iterator<Player> roundIterator = getTable().getPlayersIterator(firstOfRound, false, false);
            while (roundIterator.hasNext()){
                actionCommandList.add(rules.getTurnAction(roundIterator.next(), false));
            }
            roundIterator = getTable().getPlayersIterator(firstOfRound, false, true);
            while (roundIterator.hasNext()){
                actionCommandList.add(rules.getTurnAction(roundIterator.next(), true));
            }
            actionCommandList.add(rules.getEndRoundAction());
        }
    }

    /**
     * gets the rules of the game
     * @return the rules of the game
     */
    public Rules getRules() {
        return rules;
    }

    /**
     * Returns the player who is playing the current turn
     * @return the player who is playing the current turn
     */
    public Player getTurnPlayer(){
        Optional<TurnActionCommand> actionCommand = actionCommandList.getTurn(0);
        if(actionCommand.isPresent()){
            return actionCommand.get().getPlayer();
        }
        else throw new NoSuchElementException();
    }

    /**
     * Adds an action to the list of actions
     * @param actionCommand to add
     */
    public void addAction(ActionCommand actionCommand){
        actionCommandList.add(0, actionCommand);
    }

    /**
     * Adds a turn to the list of actions
     * @param actionCommand to add
     */
    public void addAction(TurnActionCommand actionCommand){
        actionCommandList.add(0, actionCommand);
    }


    /**
     * Executes all action in list until empty
     */
    @Override
    public void run() {
        while(!actionCommandList.isEmpty() && commChannels.stream().filter(cc -> !cc.isOffline()).count() > 1){
            this.updateAll();
            ActionCommand actionCommand = actionCommandList.get(0);
            actionCommand.execute(this);
            actionCommandList.remove(actionCommand);
        }
        rules.getEndGameAction().execute(this);
    }

    private void updateAll(){
        for (CommunicationChannel commChannel : commChannels) {
            commChannel.updateView(getTable());
            commChannel.updateView(getTable().getPool());
            commChannel.updateView(getTable().getRoundTrack());
            for (Player player : getTable().getPlayers()) {
                commChannel.updateView(player, !getChannel(player.getNickname()).isOffline());
            }
        }
    }

    /**
     * Reset the turn when called if the current action is a TurnActionCommand
     */
    public void resetTurn(){
        actionCommandList.getTurn(0).ifPresent(TurnActionCommand::reset);
    }

    /**
     * Ends game giving to every player the ranking's game.
     * @param ranking Pair of Player and Integer, the points scored.
     */
    public synchronized void endGame(List<Pair<Player,Integer>> ranking){
        logger.log(Level.FINE, "Game over");
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
    synchronized void changeChannel(CommunicationChannel newCc){
        List<CommunicationChannel> newList = getCommChannels().stream()
                .filter(cc -> !cc.getNickname().equals(newCc.getNickname()))
                .collect(Collectors.toList());
        newList.add(newCc);
        commChannels = newList;
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


    /**
     * Cancels next turn of current player.
     */
    public void skipNextTurn() {
        Optional<TurnActionCommand> optionalTurnActionCommand= actionCommandList
                .subList(1, actionCommandList.size())
                .stream()
                .filter(a-> actionCommandList.isTurn(a))
                .map(a -> (TurnActionCommand)a)
                .filter(tac->tac
                        .getPlayer()
                        .equals(getTurnPlayer()))
                .findFirst();
        optionalTurnActionCommand
                .ifPresent(turnActionCommand -> actionCommandList.remove(turnActionCommand));
    }

    /**
     * Sends a message to all clients
     * @param messages messages to send
     */
    public void sendAll(String... messages){
        Arrays.stream(messages).forEach(s -> getCommChannels().forEach(c -> c.sendMessage(s)));
        getCommChannels().forEach(c -> c.sendMessage(Message.SEPARATOR.name()));
    }

    /**
     * Sends a message to all clients
     * @param messagesList list of messages to send
     * @param messages messages to send
     */
    public void sendAll(List<String> messagesList, String... messages){
        messagesList.forEach(s -> getCommChannels().forEach(c -> c.sendMessage(s)));
        sendAll(messages);
    }

    private class ActionList extends ArrayList<ActionCommand>{
        transient private List<ActionCommand> turnCommands = new ArrayList<>();

        public boolean add(TurnActionCommand actionCommand) {
            turnCommands.add(actionCommand);
            return super.add(actionCommand);
        }

        public void add(int index, TurnActionCommand actionCommand) {
            turnCommands.add(actionCommand);
            super.add(index, actionCommand);
        }

        Optional<TurnActionCommand> getTurn(int index){
            if(turnCommands.contains(this.get(index)))
                return Optional.of((TurnActionCommand) this.get(index));
            return Optional.empty();
        }

        boolean isTurn(ActionCommand actionCommand){
            return turnCommands.contains(actionCommand);
        }

        @Override
        public boolean remove(Object o) {
            turnCommands.remove(o);
            return super.remove(o);
        }
    }
}
