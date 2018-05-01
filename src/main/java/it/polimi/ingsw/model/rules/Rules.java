package it.polimi.ingsw.model.rules;


import it.polimi.ingsw.controller.ActionCommand;

import java.util.List;

public interface Rules {

    List<ActionCommand> getGameActions();
    List<ActionCommand> getRoundActions();
    List<ActionCommand> getTurnActions();

}