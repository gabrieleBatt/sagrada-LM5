package it.polimi.ingsw.model.rules;


import it.polimi.ingsw.controller.ActionCommand;
import it.polimi.ingsw.controller.Game;

import java.util.List;
import java.util.Set;

/**
 * Contains all the actions involved in a game
 * but the special ones described in the tool cards
 */
public interface Rules {

    /**
     * sets up the action to do in the game and returns them
     * @param game the game getting the actions
     * @return the List of actions to execute during the game
     */
    List<ActionCommand> getGameActions(Game game);

    /**
     * @return the Set of possible actions in a round
     */
    Set<ActionCommand> getTurnActions();

}