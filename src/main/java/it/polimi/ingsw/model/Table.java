package it.polimi.ingsw.model;

import javafx.scene.effect.Effect;

import java.util.List;

public class Table {
    private final List<Player> players;
    private List<PublicObjective> publicObjectives;
    private List<Tool> tools;
    private final DiceBag diceBag;
    private Pool pool;
    private RoundTrack roundTrack;
    private List<Effect> effects;

    /**
     * Creates table, setting players and dicebag
     * @param players list of players
     */
    public Table(List<Player> players){
        this.players = players;
        diceBag = new DiceBag();
    }

    /**
     * Sets public objectives
     * @param publicObjective list of public objectives
     */
    public void setPublicObjective(List<PublicObjective> publicObjective){
        this.publicObjectives = publicObjective;
    }

    /**
     * Sets tools
     * @param tools list of tools to be set
     */
    public void setTools(List<Tool> tools){
        this.tools = tools;
    }

    /**
     * Gets players
     * @return a list of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Gets public objectives
     * @return a list of public objectives
     */
    public List<PublicObjective> getPublicObjectives() {
        return publicObjectives;
    }

    /**
     * Gets tools
     * @return a list of tools
     */
    public List<Tool> getTools() {
        return tools;
    }

    /**
     * Gets the dice bag
     * @return object dice bag
     */
    public DiceBag getDiceBag(){
        return diceBag;
    }

    /**
     * Gets pool
     * @return object pool
     */
    public Pool getPool() {
        return pool;
    }

    /**
     * Gets round track
     * @return object roundTrack
     */
    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    /**
     * Gets effects active in this game
     * @return list of effects
     */
    public List<Effect> getActiveEffects() {
        return effects;
    }

    /**
     * Adds effects to the effect's list
     * @param effect to be added
     */
    public void addEffect(Effect effect){
        effects.add(effect);
    }
}
