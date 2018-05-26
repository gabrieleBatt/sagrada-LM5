package it.polimi.ingsw.server.model.table;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.server.model.objective.PrivateObjective;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.glasswindow.Cell;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Player is a concrete class representing a game's player. Its' main attributes are nickname, tokens, glasswindow,
 * privateObjective.
 */
public class Player implements Memento {

    private static final Logger logger = LogMaker.getLogger(Player.class.getName(), Level.ALL);
    private final String nickname;
    private int tokens;
    private Optional<GlassWindow> glassWindow;
    private HashSet<PrivateObjective> privateObjective;
    private boolean connected;
    private Deque<List<Optional<Die>>> glassWindowMemento;
    private Deque<Integer> tokensMemento;

    /**
     * Creates a player, setting the nickname
     * @param nickname player's nickname
     */
    public Player(String nickname)
    {
        this.privateObjective = new HashSet<>();
        glassWindow = Optional.empty();
        this.nickname = nickname;
        tokensMemento = new ArrayDeque<>();
        glassWindowMemento = new ArrayDeque<>();
    }

    /**
     * Gets the player's nickname
     * @return: player's nickname
     */
    public String getNickname(){
        return nickname;
    }

    /**
     * Gets the glasswindow
     * @return: glasswindow
     * @throws NoSuchElementException exception thrown if there's no glasswindow
     */
    public GlassWindow getGlassWindow(){
        if(glassWindow.isPresent())
            return glassWindow.get();
        else throw new NoSuchElementException("The player"+ this.nickname +" doesn't have a glasswindow");
    }

    /**
     * Return true if there's a glass window, otherwise false
     * @return true if there's a glass window, otherwise false
     */
    public boolean hasGlassWindow(){
        return glassWindow.isPresent();
    }

    /**
     * Sets the glasswindow
     * @param glassWindow
     */
    public void setGlassWindow(GlassWindow glassWindow){
        this.glassWindow = Optional.of(glassWindow);
        logger.log(Level.FINEST, "GlassWindow" + glassWindow.getName() + "have been set", this);

    }

    /**
     * Gets tokens
     * @return tokens
     */
    public int getTokens(){
        return tokens;
    }

    /**
     * Sets tokens
     * @param tokens to be set
     */
    public void setTokens(int tokens){
        this.tokens = tokens;
        logger.log(Level.FINEST, tokens + "tokens have been set", this);

    }

    /**
     * Gets private objective
     * @return list of private objective
     */
    public Collection<PrivateObjective> getPrivateObjective(){
        return (Collection<PrivateObjective>)privateObjective.clone();
    }

    /**
     * Adds private objective
     * @param newPrivateObjective private objective to be added
     */
    public void addPrivateObjective (PrivateObjective newPrivateObjective){
        this.privateObjective.add(newPrivateObjective);
        logger.log(Level.FINEST, "This private objective:" + newPrivateObjective.toString()+ "has been added", this);

    }

    /**
     * Adds private objective
     * @param newPrivateObjective list of private objective to be added
     */
    public void addPrivateObjective(Collection<PrivateObjective> newPrivateObjective){
        this.privateObjective.addAll(newPrivateObjective);
        logger.log(Level.FINEST, "These private objectives:"+ newPrivateObjective.toString() + "have been added", this);

    }

    /**
     * Method used for testing
     * @return String representing a player
     */
    @Override
    public String toString(){
        String ret = "Player ";
        ret = ret + this.nickname + "has:\n" + this.tokens + "number of tokens\n"+ this.glassWindow.toString() + "\n" +
        "private objective:" + this.privateObjective.toString() + "\n";
        return ret;
    }

    /**
     * Prints the override toString of an object Player
     */
    public void dump(){
        System.console().writer().println(this);
    }

    /**
     * Adds the current glass window state and the current
     * number of tokens at the stack.
     */
    @Override
    public void addMemento() {
        List<Optional<Die>> newMemento = new ArrayList<>();
        tokensMemento.push(this.tokens);
        for(Cell cell : this.getGlassWindow().getCellList())
            if (cell.isOccupied()){
            newMemento.add(Optional.of(cell.getDie()));
            }else{
            newMemento.add(Optional.empty());
        }
        glassWindowMemento.push(newMemento);
    }

    /**
     * Gets the last  glass window state and current
     * number of tokens saved from the stack.
     */
    @Override
    public void getMemento() {
        this.tokens = tokensMemento.peek();
        List<Optional<Die>> dieList = new ArrayList<>(glassWindowMemento.peek());
        for(int i = 0; i<20; i++) {
            this.getGlassWindow().getCellList().get(i).placeOptionalDie(dieList.get(i));
        }
    }
}
