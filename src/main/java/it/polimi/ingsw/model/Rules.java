package it.polimi.ingsw.model;

import com.sun.xml.internal.bind.v2.TODO;
import it.polimi.ingsw.controller.ActionCommand;
import it.polimi.ingsw.controller.Game;

public class Rules {

    public static ActionCommand getDealPrivateObjectiveCommand(Game actionReceiver, int cardsToEach){
        return new ActionCommand("dealPrivateObjective", actionReceiver) {
            @Override
            public void execute() {
                final int toEach = cardsToEach;
                //TODO--dealing cards
            }
        };
    }
}