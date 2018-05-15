package it.polimi.ingsw.server.model.rules;

import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.exception.*;

public interface ActionCommand {

    void execute(Game actionReceiver) throws DieNotAllowedException;
}