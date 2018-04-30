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
     * Creating table, setting players and dicebag
     * @param players list of players
     */
    public Table(List<Player> players){
        this.players = players;
        diceBag = new DiceBag();
    }

    /**
     * Setting public objectives
     * @param publicObjective list of public objectives
     */
    public void setPublicObjective(List<PublicObjective> publicObjective){
        this.publicObjectives = publicObjective;
    }

    /**
     * Setting tools
     * @param tools list of tools to be set
     */
    public void setTools(List<Tool> tools){
        this.tools = tools;
    }

    /**
     * Getting players
     * @return a list of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Getting public objectives
     * @return a list of public objectives
     */
    public List<PublicObjective> getPublicObjectives() {
        return publicObjectives;
    }

    /**
     * Getting tools
     * @return a list of tools
     */
    public List<Tool> getTools() {
        return tools;
    }

    /**
     * Getting the dice bag
     * @return object dice bag
     */
    public DiceBag getDiceBag(){
        return diceBag;
    }

    /**
     * Getting pool
     * @return object pool
     */
    public Pool getPool() {
        return pool;
    }

    /**
     * Getting round track
     * @return object roundTrack
     */
    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    /**
     * Getting effects active in this game
     * @return list of effects
     */
    public List<Effect> getActiveEffects() {
        return effects;
    }

    /**
     * Adding effects to the effect's list
     * @param effect to be added
     */
    public void addEffect(Effect effect){
        effects.add(effect);
    }
}
