package it.polimi.ingsw.model.rules;


import it.polimi.ingsw.controller.ActionCommand;

import java.util.List;
import java.util.Set;

/**
 * Contains all the actions involved in a game
 * but the special ones described in the tool cards
 */
public interface Rules {

    /**
     * @return the List of actions to execute during the game
     */
    List<ActionCommand> getGameActions();

    /**
     * @return the List of actions in a round
     */
    List<ActionCommand> getRoundActions();

    /**
     * @return the Set of possible actions in a round
     */
    Set<ActionCommand> getTurnActions();

}