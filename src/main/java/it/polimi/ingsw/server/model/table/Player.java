package it.polimi.ingsw.server.model.table;

import it.polimi.ingsw.shared.LogMaker;
import it.polimi.ingsw.server.model.objective.PrivateObjective;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.glasswindow.Cell;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Player is a concrete class representing a game's player. Its' main attributes are nickname, tokens, glassWindow,
 * privateObjective.
 */
public class Player implements Memento {

    private static final Logger logger = LogMaker.getLogger(Player.class.getName(), Level.ALL);
    private final String nickname;
    private int tokens;
    private GlassWindow glassWindow;
    private HashSet<PrivateObjective> privateObjective;
    private Deque<List<Optional<Die>>> glassWindowMemento;
    private Deque<Integer> tokensMemento;

    /**
     * Creates a player, setting the nickname
     * @param nickname player's nickname
     */
    public Player(String nickname)
    {
        this.privateObjective = new HashSet<>();
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
     * Gets the glassWindow
     * @return: glassWindow
     * @throws NoSuchElementException exception thrown if there's no glassWindow
     */
    public GlassWindow getGlassWindow(){
        if(hasGlassWindow())
            return glassWindow;
        else throw new NoSuchElementException("The player"+ this.nickname +" doesn't have a glassWindow");
    }

    /**
     * Return true if there's a glass window, otherwise false
     * @return true if there's a glass window, otherwise false
     */
    public boolean hasGlassWindow(){
        return glassWindow != null;
    }

    /**
     * Sets the glassWindow
     * @param glassWindow to set
     */
    public void setGlassWindow(GlassWindow glassWindow){
        this.glassWindow = glassWindow;
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
        return new ArrayList<>(privateObjective);
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
        return nickname;
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
        Optional<Integer> optionalInteger = Optional.ofNullable(tokensMemento.peek());
        optionalInteger.ifPresent(integer -> this.tokens = integer);

        Optional<List<Optional<Die>>> optionalList = Optional.ofNullable(glassWindowMemento.peek());
        if(optionalList.isPresent()) {
            List<Optional<Die>> dieList = new ArrayList<>(optionalList.get());
            for (int i = 0; i < GlassWindow.CELLS; i++) {
                this.getGlassWindow().getCellList().get(i).placeOptionalDie(dieList.get(i));
            }
        }
    }
}
