package it.polimi.ingsw.server.model.rules;

import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.controller.commChannel.CommunicationChannel;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.glassWindow.Cell;
import it.polimi.ingsw.server.model.tool.Tool;

import java.util.*;
import java.util.stream.Collectors;

public class TurnActionCommand implements ActionCommand{

    private static final String useTool = "Usa un tool";
    private static final String drawDie = "Pesca un dado";
    private static boolean reset;
    private static CommunicationChannel cc;

    private Player player;

    public TurnActionCommand(Player player){
        this.player = player;
        this.reset = false;
    }

    @Override
    public void execute(Game actionReceiver) throws EndGameException, BagEmptyException, DeckTooSmallException, GlassWindowNotFoundException, PlayerNotFoundException {
        backUp(actionReceiver);
        do {
            reset = false;
            cc = actionReceiver.getCommChannels().stream().filter(c -> c.getNickname().equals(player.getNickname())).findFirst().get();
            List<String> options = new ArrayList<>();
            options.add(useTool);
            options.add(drawDie);
            String message1 = "Seleziona la tua prossima mossa";
            String message2 = "Cosa vuoi fare ora?";
            String actionChosen = cc.chooseFrom(options, message1, true, false);
            doActionChosen(actionChosen,actionReceiver);
            if(!reset) {
                options.remove(actionChosen);
                actionChosen = cc.chooseFrom(options, message2, true, true);
                doActionChosen(actionChosen, actionReceiver);
            }
        }while(!reset);
    }

    public void doActionChosen(String actionChosen, Game actionReceiver) throws BagEmptyException, GlassWindowNotFoundException, EndGameException, DeckTooSmallException, PlayerNotFoundException {
        switch (actionChosen) {
            case useTool:
                String toolChosen = cc.selectOption(actionReceiver.getTable().getTools().stream().map(t -> t.getName()).collect(Collectors.toList()), false, true);
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
                DefaultRules.getDefaultRules().getDraftAction("dieChosen", Optional.empty(), Optional.empty()).execute(actionReceiver);
                if(!reset)
                    //è l'azione a cambiare il reset se a get place action viene risposto undo
                    DefaultRules.getDefaultRules().getPlaceAction("dieChosen", true, true, true);
                break;

        }

    }

    public void reset(Game actionReceiver) {
        reset = true;
        actionReceiver.getTable().getRoundTrack().getMemento();
        actionReceiver.getTable().getDiceBag().getMemento();
        actionReceiver.getTable().getPool().getMemento();
        player.getMemento();
    }

    public void backUp(Game actionReceiver)  {
        player.addMemento();
        actionReceiver.getTable().getPool().addMemento();
        actionReceiver.getTable().getDiceBag().addMemento();
        actionReceiver.getTable().getRoundTrack().addMemento();
    }
}
