package it.polimi.ingsw.server.model.rules;

import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.controller.commChannel.CommunicationChannel;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.objective.*;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.dice.DieColor;
import it.polimi.ingsw.server.model.table.glassWindow.Cell;
import it.polimi.ingsw.server.model.table.glassWindow.GlassWindow;
import it.polimi.ingsw.server.model.table.glassWindow.GlassWindowDeck;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class DefaultRules implements Rules {

    private static DefaultRules defaultRules = new DefaultRules();

    private DefaultRules(){}

    public static DefaultRules getDefaultRules() {
        return defaultRules;
    }

    /**
     * Gets the list of setup game actions.
     * @return list of setup game actions.
     */
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

    /**
     * Gives an action command that deals the public objective on table.
     * @param pubObjPerGame int, amount of public objective to set on table.
     * @return ActionCommand that deals the public objective on table.
     */
    ActionCommand dealPublicObjective(int pubObjPerGame) {
        return actionReceiver -> {
            List<PublicObjective> publicObjectives = null;
            publicObjectives = PublicObjectiveDeck.getPublicObjectiveDeck().draw(pubObjPerGame);
            actionReceiver.getTable().setPublicObjective(publicObjectives);
            actionReceiver.updateAll();
            Game.getLogger().log(Level.FINE, publicObjectives.toString(), actionReceiver);
        };
    }

    /**
     * Gives an action command that deals tokens to every player.
     * @return ActionCommand that deals tokens to every player.
     */
    protected ActionCommand giveTokens() {
        return actionReceiver -> {
            List<Player> players = actionReceiver.getTable().getPlayers();
            for(Player player:players) {
                    player.setTokens(player.getGlassWindow().getDifficulty());
            }
            actionReceiver.updateAll();
            Game.getLogger().log(Level.FINE, "Tokens given", actionReceiver);
        };
    }


    /**
     * Gives an action command that deal some glass windows to every player
     * and catch the chosen one.
     * @param perPlayer int, amount of glass window given to every player.
     * @return ActionCommand that deal some glass windows to every player
     * and catch the chosen one.
     */
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
            Game.getLogger().log(Level.FINE, "Glass windows dealt", actionReceiver);
        };
    }

    /**
     * Gives an action command that deals some private objective to every player.
     * @param objPerPlayer amount of private objective to give to every player.
     * @return ActionCommand that deals some private objective to every player.
     */
    protected ActionCommand dealPrivateObjective(int objPerPlayer){
        return actionReceiver -> {
            int players = actionReceiver.getTable().getPlayers().size();
            List<PrivateObjective> privateObjectives = PrivateObjectiveDeck.getPrivateObjectiveDeck().draw(objPerPlayer*players);
            for(int i=0;i<actionReceiver.getTable().getPlayers().size();i++)
                for(int j=0;j<objPerPlayer;j++)
                     actionReceiver.getTable().getPlayers().get(i).addPrivateObjective(privateObjectives.get(i*objPerPlayer + j));
            actionReceiver.updateAll();
            Game.getLogger().log(Level.FINE, "Private objectives dealt\n" + privateObjectives.toString(), actionReceiver);
        };
    }

    /**
     * Gets the setup round action.
     * @return setup round action.
     */
    @Override
    public ActionCommand getSetupRoundAction() {
        return actionReceiver -> {
            int players = actionReceiver.getTable().getPlayers().size();
            Collection<Die> drawnDice = actionReceiver.getTable().getDiceBag().drawDice(players*2+1);
            actionReceiver.getTable().getPool().setDice(drawnDice);
            actionReceiver.updateAll();
            Game.getLogger().log(Level.FINE, "dice drawn " + drawnDice, actionReceiver);
        };
    }

    /**
     * Gets the list of turn actions of a player.
     * @param turnPlayer player whose turn is.
     * @return list of turn actions of a player.
     */
    @Override
    public ActionCommand getTurnAction(Player turnPlayer) {
        return new TurnActionCommand(turnPlayer);
    }

    /**
     * Gets the list of end round actions.
     * @return list of end round actions.
     */
    @Override
    public ActionCommand getEndRoundAction() {
        return actionReceiver -> {
          Collection<Die> diceLeft = actionReceiver.getTable().getPool().getDice();
          for(Die die:diceLeft){
              actionReceiver.getTable().getPool().takeDie(die);
          }
          actionReceiver.getTable().getRoundTrack().endRound(diceLeft);
          actionReceiver.updateAll();
          Game.getLogger().log(Level.FINE, "dice left set in the round rack\n"+diceLeft.toString(), actionReceiver);
        };
    }

    /**
     * Gets the list of end game actions.
     * @return list of end game actions.
     */
    @Override
    public List<ActionCommand> getEndGameActions() {
        List<ActionCommand> ret = new ArrayList<>();
        ret.add(scorePublicObjectivePoints());
        return ret;
    }

    private ActionCommand scorePublicObjectivePoints() {
        return actionReceiver -> {
        };
    }

    /**
     * Gets the list of draft actions.
     * @param marker String, marker of the die drafted.
     * @param dieColor DieColor, color of the die to draft.
     * @param dieNumber Integer, numeric value of the die to draft.
     * @param player Player, the one who drafts a die.
     * @return list of draft actions.
     */
    @Override
    public ActionCommand getDraftAction(String marker, Optional<DieColor> dieColor, Optional<Integer> dieNumber, Player player) {
        return (Game actionReceiver) -> {
            CommunicationChannel cc = actionReceiver.getCommChannels().stream().filter(c -> c.getNickname().equals(player.getNickname())).findFirst().get();
            List<Die> dieOptions = new ArrayList<>(actionReceiver.getTable().getPool().getDice());
            if(dieColor.isPresent())
                dieOptions = dieOptions.stream().filter(d -> Optional.of(d.getColor()).equals(dieColor)).collect(Collectors.toList());
            if(dieNumber.isPresent())
                dieOptions = dieOptions.stream().filter(d->Optional.of(d.getNumber()) == dieNumber).collect(Collectors.toList());
            List<String> options = dieOptions.stream().map(Die::getId).collect(Collectors.toList());
            String dieChosen = cc.selectOption(options, actionReceiver.getTable().getPool(), false, true);
            if (dieChosen.equals("undo")) {
                actionReceiver.resetTurn();
            }else {
                Die die = dieOptions.stream().filter(d -> d.getId().equals(dieChosen)).findFirst().get();
                actionReceiver.getTable().getPool().takeDie(die);
                actionReceiver.getMap().put(marker, die);
            }
            Game.getLogger().log(Level.FINE,"Drafted die "+ dieChosen, this);
        };
    }

    /**
     * Gets the list of place actions.
     * @param marker String, marker of the die placed.
     * @param adjacencyRestriction boolean, true if there are adjacency restrictions.
     * @param coloRestriction boolean, true if there are color restrictions.
     * @param numberRestriction boolean, true if there are numeric restrictions.
     * @param player Player, the one who places a die.
     * @return list of place actions.
     */
    @Override
    public ActionCommand getPlaceAction(String marker, boolean adjacencyRestriction, boolean coloRestriction, boolean numberRestriction, Player player) {
        return actionReceiver -> {
            CommunicationChannel cc = actionReceiver.getCommChannels().stream().filter(c -> c.getNickname().equals(player.getNickname())).findFirst().get();
            Die die = actionReceiver.getMap().get(marker);
            List<String> cells = new ArrayList<>(player.getGlassWindow().availableCells(die, !adjacencyRestriction))
                    .stream().filter(c -> c.isAllowed(die.getColor()) || !coloRestriction)
                    .filter(c -> c.isAllowed(die.getNumber()) || !numberRestriction)
                    .map(Cell::getId).collect(Collectors.toList());
            String positionChosen = cc.selectOption(cells, player.getGlassWindow(), false,true);
            if (positionChosen.equals("undo")) {
                actionReceiver.resetTurn();
            }else {
                player.getGlassWindow().getCellList().stream()
                        .filter(c -> c.getId().equals(positionChosen)).findFirst().get()
                        .placeDie(die, (coloRestriction||numberRestriction));
            }
            Game.getLogger().log(Level.FINE,"Placed die "+ die.toString() + " from "+ positionChosen, this);

        };
    }


}
