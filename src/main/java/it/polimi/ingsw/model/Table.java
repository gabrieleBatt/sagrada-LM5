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

    public Table(List<Player> players){
        this.players = players;
        diceBag = new DiceBag();
    }
    public void setPublicObjective(List<PublicObjective> publicObjective){
        this.publicObjectives = publicObjective;
    }
    public void setTools(List<Tool> tools){
        this.tools = tools;
    }
    public List<Player> getPlayers() {
        return players;
    }
    public List<PublicObjective> getPublicObjectives() {
        return publicObjectives;
    }
    public List<Tool> getTools() {
        return tools;
    }
    public DiceBag getDiceBag(){
        return diceBag;
    }
    public Pool getPool() {
        return pool;
    }
    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    public List<Effect> getActiveEffects() {
        return effects;
    }
    public void addEffect(Effect effect){
        effects.add(effect);
    }
}
