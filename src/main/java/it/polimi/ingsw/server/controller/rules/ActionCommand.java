package it.polimi.ingsw.server.controller.rules;

import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.exception.*;

public interface ActionCommand {

    /**
     * Execute the action on the specified game
     * @param actionReceiver receiver of the action
     */
    void execute(Game actionReceiver);
}