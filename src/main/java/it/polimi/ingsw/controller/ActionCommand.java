package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.exception.BagEmptyException;
import it.polimi.ingsw.model.exception.EndGameException;

public interface ActionCommand {

    void execute(Game actionReceiver) throws EndGameException, BagEmptyException;
}
