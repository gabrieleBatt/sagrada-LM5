package it.polimi.ingsw.server.model.rules;

import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.controller.commChannel.StdId;
import it.polimi.ingsw.server.controller.commChannel.CommunicationChannel;
import it.polimi.ingsw.server.controller.commChannel.Identifiable;
import it.polimi.ingsw.server.exception.*;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.tool.Tool;

import java.util.*;

import static it.polimi.ingsw.server.controller.commChannel.StdId.*;

public class TurnActionCommand implements ActionCommand{

    private static final String message1 = "SelezionaLaTuaProssimaMossa";
    private static final String message2 = "CosaVuoiFareOra?";
    private static boolean reset;
    private static boolean skip;
    private static CommunicationChannel cc;

    private Player player;

    public TurnActionCommand(Player player){
        this.player = player;
        reset = false;
        skip = false;
    }

    @Override
    public void execute(Game actionReceiver) throws DieNotAllowedException {
        backUp(actionReceiver);
        do {
            reset = false;
            skip = false;
            cc = actionReceiver.getCommChannels().stream().filter(c -> c.getNickname().equals(player.getNickname())).findFirst().get();
            List<Identifiable> options = new ArrayList<>();
            options.add(DRAFT);
            options.add(USE_TOOL);

            //choose first action
            Identifiable actionChosen = cc.chooseFrom(options, message1, true, false);
            doActionChosen(actionChosen,actionReceiver);

            //choose second action
            if(!reset && !skip) {
                options.remove(actionChosen);
                actionChosen = cc.chooseFrom(options, message2, true, true);
                doActionChosen(actionChosen, actionReceiver);
            }
        }while(reset);
    }

    private void doActionChosen(final Identifiable actionChosen, Game actionReceiver) throws DieNotAllowedException {
        if(actionChosen.getId().equals(USE_TOOL.getId())) {
            Identifiable toolChosen = cc.selectObject(new ArrayList<>(actionReceiver.getTable().getTools()), StdId.TABLE, false, true);
            if (toolChosen.getId().equals(UNDO.getId()))
                this.reset(actionReceiver);
            if (!reset) {
                Tool tool = actionReceiver.getTable().getTools().stream().filter(t -> t.getId().equals(toolChosen.getId())).findFirst().get();
                tool.setUsed(true);
                for (ActionCommand actionCommand : tool.getActionCommandList()) {
                    if (!reset)
                        actionCommand.execute(actionReceiver);
                }
            }

        }else if(actionChosen.getId().equals(DRAFT.getId())) {
            actionReceiver.getRules().getDraftAction("dieChosen", Optional.empty(), Optional.empty(), player).execute(actionReceiver);
            if (!reset)
                actionReceiver.getRules().getPlaceAction("dieChosen", true, true, true, player).execute(actionReceiver);

        }else if(actionChosen.getId().equals(SKIP.getId())) {
            skip = true;

        }else if(actionChosen.getId().equals(UNDO.getId())) {
            reset(actionReceiver);
            actionReceiver.getCommChannels().forEach(c -> c.updateView(player));
            actionReceiver.getCommChannels().forEach(c -> c.updateView(actionReceiver.getTable().getPool()));
            actionReceiver.getCommChannels().forEach(c -> c.updateView(actionReceiver.getTable().getRoundTrack()));
        }
    }

    /**
     * Resets the turn to the start or to the last checkpoint action (es. reRolling)
     * @param actionReceiver
     */
    public void reset(Game actionReceiver) {
        reset = true;
        actionReceiver.getTable().getRoundTrack().getMemento();
        actionReceiver.getTable().getDiceBag().getMemento();
        actionReceiver.getTable().getPool().getMemento();
        player.getMemento();
    }

    private void backUp(Game actionReceiver)  {
        player.addMemento();
        actionReceiver.getTable().getPool().addMemento();
        actionReceiver.getTable().getDiceBag().addMemento();
        actionReceiver.getTable().getRoundTrack().addMemento();
    }
}
