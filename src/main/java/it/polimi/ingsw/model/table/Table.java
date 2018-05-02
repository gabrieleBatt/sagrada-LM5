package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.objective.PublicObjective;
import it.polimi.ingsw.model.table.dice.DiceBag;
import it.polimi.ingsw.model.tool.Tool;
import javafx.scene.effect.Effect;

import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Table {
    private final Set<Player> players;
    private Set<PublicObjective> publicObjectives;
    private Set<Tool> tools;
    private final DiceBag diceBag;
    private final Pool pool;
    private final RoundTrack roundTrack;
    private Set<Effect> effects;

    /**
     * Creates table with all the players
     * @param players Set of players
     */
    public Table(Collection<Player> players){
        this.players = new HashSet<>(players);
        this.diceBag = new DiceBag();
        this.roundTrack = new RoundTrack();
        this.effects = new HashSet<>();
        this.pool = new Pool();
        this.publicObjectives = new HashSet<>();
        this.tools = new HashSet<>();
    }

    /**
     * Sets public objective
     * @param publicObjective Set of public objective
     */
    public void setPublicObjective(Collection<PublicObjective> publicObjective){
        this.publicObjectives = new HashSet<>(publicObjective);
    }

    /**
     * Sets tools
     * @param tools Set of tools to be set
     */
    public void setTools(Collection<Tool> tools){
        this.tools = new HashSet<>(tools);
    }

    /**
     * Gets players
     * @return a Set of players
     */
    public Collection<Player> getPlayers() {
        return players;
    }

    /**
     * Gets public objective
     * @return a Set of public objective
     */
    public Collection<PublicObjective> getPublicObjectives() {
        return publicObjectives;
    }

    /**
     * Gets tools
     * @return a Set of tools
     */
    public Collection<Tool> getTools() {
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
    public Collection<Effect> getActiveEffects() {
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
