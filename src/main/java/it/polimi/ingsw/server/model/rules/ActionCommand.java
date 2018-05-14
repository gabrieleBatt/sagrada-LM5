package it.polimi.ingsw.server.model.rules;

import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.model.exception.*;

public interface ActionCommand {

    void execute(Game actionReceiver) throws EndGameException, BagEmptyException, DeckTooSmallException, GlassWindowNotFoundException, CellNotFoundException, DieNotAllowedException;
}