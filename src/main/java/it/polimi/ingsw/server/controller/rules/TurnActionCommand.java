package it.polimi.ingsw.server.controller.rules;

import it.polimi.ingsw.net.Message;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.net.identifiables.StdId;
import it.polimi.ingsw.server.controller.channels.CommunicationChannel;
import it.polimi.ingsw.net.identifiables.Identifiable;
import it.polimi.ingsw.server.exception.*;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.tool.Tool;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

import static it.polimi.ingsw.net.identifiables.StdId.*;

public class TurnActionCommand implements ActionCommand{

    private boolean reset;
    private boolean skip;
    private CommunicationChannel cc;
    private static long turnTime;

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

    private Player player;

    TurnActionCommand(Player player){
        this.player = player;
    }

    /**
     * Execute all the turn actions on the specified game
     * @param actionReceiver receiver of the action
     */
    @Override
    public void execute(Game actionReceiver){
        Timer timer = new Timer();
        startTimer(timer);
        backUp(actionReceiver);
        List<Identifiable> options;
        Identifiable actionChosen;
        do {
            reset = false;
            skip = false;
            actionReceiver.getCommChannels()
                    .stream()
                    .filter(c -> c.getNickname().equals(player.getNickname()))
                    .findFirst()
                    .ifPresent(communicationChannel -> cc = communicationChannel);
            options = new ArrayList<>();
            options.add(DRAFT);
            options.add(USE_TOOL);

            //choose first action
            actionChosen = cc.chooseFrom(options, Message.NEXT_MOVE.name(), true, false);
            doActionChosen(actionChosen, actionReceiver);
        }while(reset);
        backUp(actionReceiver);
        do{
            reset = false;
            //choose second action
            if(!skip) {
                options.remove(actionChosen);
                actionChosen = cc.chooseFrom(options, Message.NEXT_MOVE.name(), true, true);
                doActionChosen(actionChosen, actionReceiver);
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

    private void doActionChosen(final Identifiable actionChosen, Game actionReceiver) throws DieNotAllowedException {
        if(actionChosen.getId().equals(USE_TOOL.getId())) {
            doToolAction(actionReceiver);

        }else if(actionChosen.getId().equals(DRAFT.getId())) {
            actionReceiver.getRules().getDraftAction("dieChosen", null, null).execute(actionReceiver);
            if (!reset)
                actionReceiver.getRules().getPlaceAction("dieChosen", true, true, true,false).execute(actionReceiver);

        }else if(actionChosen.getId().equals(SKIP.getId())) {
            skip = true;

        }else if(actionChosen.getId().equals(UNDO.getId())) {
            reset(actionReceiver);
        }
    }

    private void doToolAction(Game actionReceiver) throws DieNotAllowedException {
        if(actionReceiver.getTable().getTools().isEmpty()){
            return;
        }
        Identifiable toolChosen = cc.selectObject(new ArrayList<>(actionReceiver.getTable().getTools()), StdId.TABLE, false, true);
        if (toolChosen.getId().equals(UNDO.getId()))
            this.reset(actionReceiver);
        if (!reset) {
            Optional<Tool> optTool = actionReceiver.getTable().getTools()
                    .stream()
                    .filter(t -> t.getId().equals(toolChosen.getId()))
                    .findFirst();
            if(optTool.isPresent()) {
                Tool tool = optTool.get();
                tool.setUsed(true);
                for (ActionCommand actionCommand : tool.getActionCommandList()) {
                    if (!reset)
                        actionCommand.execute(actionReceiver);
                }
            }
        }
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
     * @param actionReceiver to reset
     */
    public void reset(Game actionReceiver) {
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
     * @param actionReceiver to backup
     */
    private void backUp(Game actionReceiver)  {
        player.addMemento();
        actionReceiver.getTable().getPool().addMemento();
        actionReceiver.getTable().getDiceBag().addMemento();
        actionReceiver.getTable().getRoundTrack().addMemento();
    }
}
