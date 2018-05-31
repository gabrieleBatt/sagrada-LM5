package it.polimi.ingsw.server.controller.rules;


import it.polimi.ingsw.server.model.table.Player;

import java.util.List;

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
     * Gets the turn action of a player.
     * @param turnPlayer player whose turn is.
     * @param secondTurn boolean, true if is second turn.
     * @return Turn action of a of a player.
     */
    TurnActionCommand getTurnAction(Player turnPlayer, boolean secondTurn);

    /**
     * Gets the end round action.
     * @return End round action.
     */
    ActionCommand getEndRoundAction();

    /**
     * Gets the end game action.
     * @return end game action.
     */
    ActionCommand getEndGameAction();

    /**
     * Gets the draft action.
     * @param marker String, marker of the die drafted.
     * @param dieColor marker of die deciding the color.
     * @param dieNumber marker of die deciding the color.
     * @return draft action.
     */
    ActionCommand getDraftAction(String marker, String dieColor, String dieNumber);

    /**
     * Gets place action.
     * @param marker String, marker of the die placed.
     * @param adjacencyRestriction boolean, true if there are adjacency restrictions.
     * @param coloRestriction boolean, true if there are color restrictions.
     * @param numberRestriction boolean, true if there are numeric restrictions.
     * @return place action.
     */
    ActionCommand getPlaceAction(String marker, boolean adjacencyRestriction, boolean coloRestriction, boolean numberRestriction, boolean forced);
}