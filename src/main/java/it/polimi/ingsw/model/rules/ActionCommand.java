package it.polimi.ingsw.model.rules;

import it.polimi.ingsw.controller.Game;
import it.polimi.ingsw.model.exception.BagEmptyException;
import it.polimi.ingsw.model.exception.EndGameException;

public interface ActionCommand {

    void execute(Game actionReceiver) throws EndGameException, BagEmptyException;
}
