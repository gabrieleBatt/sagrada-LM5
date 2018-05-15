package it.polimi.ingsw.server.model.rules;

import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.controller.commChannel.CommunicationChannel;
import it.polimi.ingsw.server.exception.*;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.tool.Tool;

import java.util.*;
import java.util.stream.Collectors;

public class TurnActionCommand implements ActionCommand{

    private static final String useTool = "UsaUnTool";
    private static final String drawDie = "PescaUnDado";
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
            List<String> options = new ArrayList<>();
            options.add(useTool);
            options.add(drawDie);
            String actionChosen = cc.chooseFrom(options, message1, true, false);
            doActionChosen(actionChosen,actionReceiver);
            if(!reset && !skip) {
                options.remove(actionChosen);
                actionChosen = cc.chooseFrom(options, message2, true, true);
                doActionChosen(actionChosen, actionReceiver);
            }
        }while(reset);
    }

    private void doActionChosen(final String actionChosen, Game actionReceiver) throws DieNotAllowedException {
        switch (actionChosen) {
            case useTool:
                String toolChosen = cc.selectOption(actionReceiver.getTable().getTools().stream().map(Tool::getName).collect(Collectors.toList()), "table", false, true);
                if(toolChosen.equals("undo"))
                    this.reset(actionReceiver);
                if(!reset){
                    Tool tool = actionReceiver.getTable().getTools().stream().filter(t -> t.getName().equals(toolChosen)).findFirst().get();
                    tool.setUsed(true);
                    for (ActionCommand actionCommand : tool.getActionCommandList()) {
                        if(!reset)
                            actionCommand.execute(actionReceiver);
                    }
                }
                break;

            case drawDie:
                actionReceiver.getRules().getDraftAction("dieChosen", Optional.empty(), Optional.empty(), player).execute(actionReceiver);
                if(!reset)
                    actionReceiver.getRules().getPlaceAction("dieChosen", true, true, true, player).execute(actionReceiver);
                break;
            case "skip":
                skip = true;
                break;
            case "undo":
                reset(actionReceiver);
                actionReceiver.getCommChannels().forEach(c -> c.updateView(player));
                actionReceiver.getCommChannels().forEach(c -> c.updateView(actionReceiver.getTable().getPool()));
                actionReceiver.getCommChannels().forEach(c -> c.updateView(actionReceiver.getTable().getRoundTrack()));
                break;
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
