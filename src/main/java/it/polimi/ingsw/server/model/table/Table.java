package it.polimi.ingsw.server.model.table;

import it.polimi.ingsw.shared.LogMaker;
import it.polimi.ingsw.server.model.objective.PublicObjective;
import it.polimi.ingsw.server.model.table.dice.DiceBag;
import it.polimi.ingsw.server.model.tool.Tool;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Table is a concrete class representing the state of the game and offering a general prospecting on the game status.
 * It has as attributes the list of players, public objectives and tools; the dice bag, the pool, the round track and
 * the effects.
 */
public class Table {

    private static final Logger logger = LogMaker.getLogger(Table.class.getName(), Level.ALL);
    private final List<Player> players;
    private Set<PublicObjective> publicObjectives;
    private Set<Tool> tools;
    private final DiceBag diceBag;
    private final Pool pool;
    private final RoundTrack roundTrack;

    /**
     * Creates table with all the players
     * @param players Set of players
     */
    public Table(List<Player> players){
        this.players = new ArrayList<>(players);
        this.diceBag = new DiceBag();
        this.roundTrack = new RoundTrack();
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
        logger.log(Level.FINEST, "These public objectives: " + publicObjective+ " have been set", this);

    }

    /**
     * Sets tools
     * @param tools Set of tools to be set
     */
    public void setTools(Collection<Tool> tools){
        this.tools = new HashSet<>(tools);
        logger.log(Level.FINEST, "These tools: " + tools + " have been set", this);

    }

    /**
     * Gets players
     * @return a Collection of players
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Gets the player having -nickName- as a nickname from the players' list.
     * @param nickName
     * @return Object Player if is present
     *      having -nickName- as nickname
     */
    public Player getPlayer(String nickName){
        if(getPlayers().stream().anyMatch(p -> p.getNickname().equals(nickName))) {
            Optional<Player> optionalPlayer = getPlayers().stream().
                    filter(p -> p.getNickname().equals(nickName))
                    .findAny();
            if (optionalPlayer.isPresent()){
                return optionalPlayer.get();
            }
        }
        throw new NoSuchElementException("Player "+nickName +" can't be found");
    }

    /**
     * Cycles the players form the first
     * @param first player to start from
     * @param infinite true for an iterator that never stops
     * @param reverse true for an iterator that ends with first player
     * @return the iterator
     */
    public Iterator<Player> getPlayersIterator(Player first, boolean infinite, boolean reverse){
        return new Iterator<Player>() {

            int i = 0;
            Player next = first;

            @Override
            public boolean hasNext() {
                return infinite || i < players.size();
            }

            @Override
            public Player next() {
                if(!hasNext()) throw new NoSuchElementException();
                if (!reverse)
                    next = players.get((players.indexOf(first) + i)%players.size());
                else
                    next = players.get((players.indexOf(first) + (i+1)*players.size() - i - 1)%players.size());
                i++;
                return next;
            }
        };
    }

    /**
     * Gets public objective
     * @return a Set of public objective
     */
    public Collection<PublicObjective> getPublicObjectives() {
        return
                new HashSet<>(publicObjectives);
    }

    /**
     * Gets tools
     * @return a Set of tools
     */
    public Collection<Tool> getTools() {
        return new HashSet<>(tools);
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


}
