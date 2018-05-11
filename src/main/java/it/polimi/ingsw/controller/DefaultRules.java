package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.exception.BagEmptyException;
import it.polimi.ingsw.model.exception.DeckTooSmallException;
import it.polimi.ingsw.model.exception.EndGameException;
import it.polimi.ingsw.model.exception.GlassWindowNotFoundException;
import it.polimi.ingsw.model.objective.PrivateObjective;
import it.polimi.ingsw.model.objective.PrivateObjectiveDeck;
import it.polimi.ingsw.model.objective.PublicObjective;
import it.polimi.ingsw.model.objective.PublicObjectiveDeck;
import it.polimi.ingsw.model.rules.ActionCommand;
import it.polimi.ingsw.model.table.Player;
import it.polimi.ingsw.model.table.dice.Die;
import it.polimi.ingsw.model.table.dice.DieColor;
import it.polimi.ingsw.model.table.glassWindow.GlassWindow;
import it.polimi.ingsw.model.table.glassWindow.GlassWindowDeck;
import it.polimi.ingsw.model.tool.Tool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DefaultRules implements Rules {

    private static DefaultRules defaultRules = new DefaultRules();

    private DefaultRules(){}

    public static DefaultRules getDefaultRules() {
        return defaultRules;
    }

    @Override
    public List<ActionCommand> getSetupGameActions() {
        List<ActionCommand> ret = new ArrayList<>();
        ret.add(dealPrivateObjective(1));
        ret.add(dealGlassWindow(2));
        ret.add(giveTokens());
        ret.add(dealPublicObjective(3));
        ret.add(dealTool(3));
        return ret;
    }

    ActionCommand dealTool(int toolPerGame) {
        return actionReceiver -> {
            //TODO
        };
    }

    ActionCommand dealPublicObjective(int pubObjPerGame) {
        return actionReceiver -> {
            List<PublicObjective> publicObjectives = null;
            publicObjectives = PublicObjectiveDeck.getPublicObjectiveDeck().draw(pubObjPerGame);
            actionReceiver.getTable().setPublicObjective(publicObjectives);
            actionReceiver.updateAll();
        };
    }

    protected ActionCommand giveTokens() {
        return actionReceiver -> {
            List<Player> players = actionReceiver.getTable().getPlayers();
            for(Player player:players) {
                    player.setTokens(player.getGlassWindow().getDifficulty());
            }
            actionReceiver.updateAll();
        };
    }


    protected ActionCommand dealGlassWindow(int perPlayer) {
        return actionReceiver -> {
            int playersSize = actionReceiver.getTable().getPlayers().size();
            List <GlassWindow> glassWindows = GlassWindowDeck.getGlassWindowDeck().draw(perPlayer*playersSize);
            for (CommunicationChannel communicationChannel : actionReceiver.getCommChannels()) {
                List<GlassWindow> glassWindowsOptions = new ArrayList<>();
                for (int i = 0; i < perPlayer*2; i++) {
                    glassWindowsOptions.add(glassWindows.get(0));
                    glassWindows.remove(0);
                }
                actionReceiver.getTable().getPlayer(communicationChannel.getNickname()).setGlassWindow(communicationChannel.chooseWindow(glassWindowsOptions));

            }
            actionReceiver.updateAll();
        };
    }

    protected ActionCommand dealPrivateObjective(int objPerPlayer){
        return actionReceiver -> {
            int players = actionReceiver.getTable().getPlayers().size();
            List<PrivateObjective> privateObjectives = PrivateObjectiveDeck.getPrivateObjectiveDeck().draw(objPerPlayer*players);
            for(int i=0;i<actionReceiver.getTable().getPlayers().size();i++)
                for(int j=0;j<objPerPlayer;j++)
                     actionReceiver.getTable().getPlayers().get(i).addPrivateObjective(privateObjectives.get(i*objPerPlayer + j));
            actionReceiver.updateAll();
        };
    }

    @Override
    public ActionCommand getSetupRoundAction() {
        return actionReceiver -> {
            int players = actionReceiver.getTable().getPlayers().size();
            Collection<Die> drawnDice = actionReceiver.getTable().getDiceBag().drawDice(players*2+1);
            actionReceiver.getTable().getPool().addDice(drawnDice);
            actionReceiver.updateAll();
        };
    }

    @Override
    public List<ActionCommand> getTurnActions(Player turnPlayer) {
        return null;
    }

    @Override
    public ActionCommand getEndRoundAction() {
        return actionReceiver -> {
          Collection<Die> diceLeft = actionReceiver.getTable().getPool().getDice();
          for(Die die:diceLeft){
              actionReceiver.getTable().getPool().takeDie(die);
          }
          actionReceiver.getTable().getRoundTrack().endRound(diceLeft);
        };
    }

    @Override
    public List<ActionCommand> getEndGameActions() {
        return null;
    }

    @Override
    public ActionCommand getDraftAction(String id, DieColor dieColor, int dieNumber) {
        return null;
    }

    @Override
    public ActionCommand getPlaceAction(String id, boolean adiacencyRestriction, boolean coloRestriction, boolean numberRestriction) {
        return null;
    }


}
