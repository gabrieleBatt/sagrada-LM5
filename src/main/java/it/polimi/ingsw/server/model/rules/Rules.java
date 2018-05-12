package it.polimi.ingsw.server.model.rules;


import it.polimi.ingsw.server.model.rules.ActionCommand;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.dice.DieColor;

import java.util.List;
import java.util.Optional;

public interface Rules {
    /**
     * Gets the list of setup game actions.
     * @return list of setup game actions.
     */
    List<ActionCommand> getSetupGameActions();

    /**
     * Gets the setup round action.
     * @return setup round action.
     */
    ActionCommand getSetupRoundAction();

    /**
     * Gets the list of turn actions of a player.
     * @param turnPlayer player whose turn is.
     * @return list of turn actions of a player.
     */
    ActionCommand getTurnAction(Player turnPlayer);

    /**
     * Gets the list of end round actions.
     * @return list of end round actions.
     */
    ActionCommand getEndRoundAction();

    /**
     * Gets the list of end game actions.
     * @return list of end game actions.
     */
    List<ActionCommand> getEndGameActions();

    /**
     * Gets the list of draft actions.
     * @param marker String, marker of the die drafted.
     * @param dieColor DieColor, color of the die to draft.
     * @param dieNumber Integer, numeric value of the die to draft.
     * @return list of draft actions.
     */
    ActionCommand getDraftAction(String marker, Optional<DieColor> dieColor, Optional<Integer> dieNumber);

    /**
     * Gets the list of place actions.
     * @param marker String, marker of the die placed.
     * @param adjacencyRestriction boolean, true if there are adjacency restrictions.
     * @param coloRestriction boolean, true if there are color restrictions.
     * @param numberRestriction boolean, true if there are numeric restrictions.
     * @return list of place actions.
     */
    ActionCommand getPlaceAction(String marker, boolean adjacencyRestriction, boolean coloRestriction, boolean numberRestriction);

}