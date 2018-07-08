package it.polimi.ingsw.server.controller.rules;

import it.polimi.ingsw.shared.Message;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.shared.identifiables.StdId;
import it.polimi.ingsw.server.controller.channels.CommunicationChannel;
import it.polimi.ingsw.shared.identifiables.Identifiable;
import it.polimi.ingsw.server.exception.*;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.tool.Tool;
import it.polimi.ingsw.server.model.tool.ToolConditions;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static it.polimi.ingsw.shared.identifiables.StdId.*;

public class TurnActionCommand implements ActionCommand{

    private static final int UNUSED_TOOL_PRICE = 1;
    private static final int USED_TOOL_PRICE = 2;
    private boolean reset;
    private boolean skip;
    private final boolean secondTurn;
    private final Player player;
    private CommunicationChannel cc;
    private static long turnTime;
    private boolean drafted;
    private Game actionReceiver;


    private static final String TURN_TIME = "turnTime";
    private static final int STD_TURN_TIME = 60;
    private static final String CONFIG_PATH = "resources/ServerResources/config.json";

    //Turn configuration
    static {
        JSONObject config;
        try {
            config = (JSONObject)new JSONParser().parse(new FileReader(new File(CONFIG_PATH)));
            turnTime = (long)config.get(TURN_TIME);
        } catch (ParseException | IOException e) {
            turnTime = STD_TURN_TIME;
        }
    }

    TurnActionCommand(Player player, boolean secondTurn){
        this.player = player;
        this.secondTurn = secondTurn;
    }

    /**
     * Execute all the turn actions on the specified game
     * @param ar receiver of the action
     */
    @Override
    public void execute(Game ar){
        this.actionReceiver = ar;
        Timer timer = new Timer();
        startTimer(timer);
        backUp();

        List<Identifiable> options;
        Identifiable actionChosen;
        actionReceiver.sendAll(Message.START_TURN.name(), player.getNickname(), Message.SEPARATOR.name(), Long.toString(turnTime), Message.TIMER.name());

        do {
            drafted = false;
            reset = false;
            skip = false;
            cc = actionReceiver.getChannel(player.getNickname());
            options = new ArrayList<>();
            options.add(DRAFT);
            options.add(USE_TOOL);

            //choose first action
            actionChosen = cc.chooseFrom(options, Message.NEXT_MOVE.name(), true, false);
            doActionChosen(actionChosen);
        }while(reset);
        options.remove(actionChosen);
        backUp();
        do{
            reset = false;
            //choose second action
            if(!skip) {
                actionChosen = cc.chooseFrom(options, Message.NEXT_MOVE.name(), true, true);
                doActionChosen(actionChosen);
            }
        }while(reset);
        timer.cancel();
    }

    private void startTimer(Timer timer) {
        Game.getLogger().log(Level.FINER, "Starting turn timer");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Game.getLogger().log(Level.FINER, "Time's up, turn ends" );
                cc.setOffline();
                timer.cancel();
            }
        }, turnTime * 1000);
    }

    private void doActionChosen(final Identifiable actionChosen){
        if(actionChosen.getId().equals(USE_TOOL.getId())) {
            doToolAction();

        }else if(actionChosen.getId().equals(DRAFT.getId())) {
            actionReceiver.getRules().getDraftAction("dieChosen"+actionChosen, null, null).execute(actionReceiver);
            if (!reset) {
                actionReceiver.getRules().getPlaceAction("dieChosen"+actionChosen, true, true, true, false).execute(actionReceiver);
                drafted = true;
            }
        }else if(actionChosen.equals(SKIP)) {
            skip = true;
        }else if(actionChosen.equals(UNDO)) {
            reset();
        }
    }

    private void doToolAction() {
        if(actionReceiver.getTable().getTools().isEmpty()){
            return;
        }
        Identifiable toolChosen = cc.selectObject(new ArrayList<>(availableTools()), StdId.TABLE, false, true);

        if (toolChosen.getId().equals(UNDO.getId()))
            this.reset();

        if (!reset) {
            Optional<Tool> optTool = actionReceiver.getTable().getTools()
                    .stream()
                    .filter(t -> t.getId().equals(toolChosen.getId()))
                    .findFirst();
            if(optTool.isPresent()) {
                Tool tool = optTool.get();
                if(tool.isUsed())
                    player.setTokens(player.getTokens() - USED_TOOL_PRICE);
                else
                    player.setTokens(player.getTokens() - UNUSED_TOOL_PRICE);
                tool.setUsed(true);
                for (ActionCommand actionCommand : tool.getActionCommandList()) {
                    if (!reset)
                        actionCommand.execute(actionReceiver);
                }
                if(!reset && optTool.get().getToolConditions().contains(ToolConditions.CANCEL_DRAFTING)){
                    skip = true;
                }
            }
        }

    }

    private Collection<Tool> availableTools(){
        if(player.getTokens() == 0)
            return new ArrayList<>();

        Collection<Tool> tools = actionReceiver.getTable().getTools();

        if(player.getTokens() == 1)
            tools = tools
                    .stream()
                    .filter(t -> !t.isUsed())
                    .collect(Collectors.toList());

        if(drafted) {
            tools = tools
                    .stream()
                    .filter(t -> !t.getToolConditions().contains(ToolConditions.BEFORE_DRAFTING))
                    .filter(t -> !t.getToolConditions().contains(ToolConditions.CANCEL_DRAFTING))
                    .collect(Collectors.toList());
        }


        if(secondTurn)
            tools = tools
                    .stream()
                    .filter(t -> !t.getToolConditions().contains(ToolConditions.FIRST_TURN))
                    .collect(Collectors.toList());
        else
            tools = tools
                    .stream()
                    .filter(t -> !t.getToolConditions().contains(ToolConditions.SECOND_TURN))
                    .collect(Collectors.toList());

        return tools;
    }

    /**
     * returns the player who is playing
     * @return he player who is playing
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Resets the turn to the start or to the last checkpoint action (es. reRolling)
     */
    public void reset() {
        reset = true;
        actionReceiver.getTable().getRoundTrack().getMemento();
        actionReceiver.getTable().getDiceBag().getMemento();
        actionReceiver.getTable().getPool().getMemento();
        player.getMemento();
        actionReceiver.getCommChannels().forEach(c -> c.updateView(player, !actionReceiver.getChannel(player.getNickname()).isOffline()));
        actionReceiver.getCommChannels().forEach(c -> c.updateView(actionReceiver.getTable().getPool()));
        actionReceiver.getCommChannels().forEach(c -> c.updateView(actionReceiver.getTable().getRoundTrack()));
    }

    /**
     * create a checkpoint
     */
    private void backUp()  {
        player.addMemento();
        actionReceiver.getTable().getPool().addMemento();
        actionReceiver.getTable().getDiceBag().addMemento();
        actionReceiver.getTable().getRoundTrack().addMemento();
    }
}
