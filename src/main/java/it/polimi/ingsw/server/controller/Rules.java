package it.polimi.ingsw.server.controller;


import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.model.rules.ActionCommand;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.RoundTrack;
import it.polimi.ingsw.server.model.table.dice.DiceBag;
import it.polimi.ingsw.server.model.table.dice.DieColor;

import java.util.List;

public interface Rules {

    public List<ActionCommand> getSetupGameActions();

    public ActionCommand getSetupRoundAction();

    public List<ActionCommand> getTurnActions(Player turnPlayer);

    public ActionCommand getEndRoundAction();

    public List<ActionCommand> getEndGameActions();

    public ActionCommand getDraftAction(String id, DieColor dieColor, int dieNumber);

    public ActionCommand getPlaceAction(String id, boolean adiacencyRestriction, boolean coloRestriction, boolean numberRestriction);

}