package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.objective.PublicObjective;
import it.polimi.ingsw.model.tool.Tool;
import javafx.scene.effect.Effect;

import java.util.Set;

public class Table {
    private final Set<Player> players;
    private Set<PublicObjective> publicObjectives;
    private Set<Tool> tools;
    private final DiceBag diceBag;
    private Pool pool;
    private RoundTrack roundTrack;
    private Set<Effect> effects;

    /**
     * Creates table, setting players and dicebag
     * @param players Set of players
     */
    public Table(Set<Player> players){
        this.players = players;
        diceBag = new DiceBag();
    }

    /**
     * Sets public objective
     * @param publicObjective Set of public objective
     */
    public void setPublicObjective(Set<PublicObjective> publicObjective){
        this.publicObjectives = publicObjective;
    }

    /**
     * Sets tools
     * @param tools Set of tools to be set
     */
    public void setTools(Set<Tool> tools){
        this.tools = tools;
    }

    /**
     * Gets players
     * @return a Set of players
     */
    public Set<Player> getPlayers() {
        return players;
    }

    /**
     * Gets public objective
     * @return a Set of public objective
     */
    public Set<PublicObjective> getPublicObjectives() {
        return publicObjectives;
    }

    /**
     * Gets tools
     * @return a Set of tools
     */
    public Set<Tool> getTools() {
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
     * @return Set of effects
     */
    public Set<Effect> getActiveEffects() {
        return effects;
    }

    /**
     * Adds effects to the effect's Set
     * @param effect to be added
     */
    public void addEffect(Effect effect){
        effects.add(effect);
    }
}
