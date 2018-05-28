package it.polimi.ingsw.server.controller.rules;

import it.polimi.ingsw.net.identifiables.Identifiable;
import it.polimi.ingsw.net.identifiables.StdId;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.controller.channels.CommunicationChannel;
import it.polimi.ingsw.server.model.objective.PrivateObjective;
import it.polimi.ingsw.server.model.objective.PrivateObjectiveDeck;
import it.polimi.ingsw.server.model.objective.PublicObjective;
import it.polimi.ingsw.server.model.objective.PublicObjectiveDeck;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.glasswindow.Cell;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindowDeck;
import javafx.util.Pair;

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
            actionReceiver.getCommChannels().forEach(cc -> cc.updateView(actionReceiver.getTable()));
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
            for (Player player : actionReceiver.getTable().getPlayers()) {
                actionReceiver
                        .getCommChannels()
                        .forEach(cc -> cc.updateView(player, !actionReceiver
                                                                .getChannel(player.getNickname())
                                                                .isOffline()));
            }
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
            for (Player player : actionReceiver.getTable().getPlayers()) {
                actionReceiver
                        .getCommChannels()
                        .forEach(cc -> cc.updateView(player, !actionReceiver
                                                                    .getChannel(player.getNickname())
                                                                    .isOffline()));
            }
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
            actionReceiver
                    .getCommChannels()
                    .forEach(cc -> cc
                            .updateView(actionReceiver
                                    .getTable()
                                    .getPlayer(cc.getNickname()), !actionReceiver
                                            .getChannel(cc.getNickname())
                                            .isOffline()));
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
            actionReceiver.getCommChannels().forEach(cc -> cc.updateView(actionReceiver.getTable().getPool()));
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
          actionReceiver.getCommChannels()
                  .forEach(cc -> cc.updateView(actionReceiver.getTable().getPool()));
          actionReceiver.getCommChannels()
                  .forEach(cc -> cc.updateView(actionReceiver.getTable().getRoundTrack()));
          Game.getLogger().log(Level.FINE, "dice left set in the round rack\n"+diceLeft.toString(), actionReceiver);
        };
    }

    /**
     * Gets the end game action.
     * @return end game action.
     */
    @Override
    public ActionCommand getEndGameAction() {
        return actionReceiver -> {
            List<Pair<Player,Integer>> ranking = new ArrayList<>();
            for(Player player : actionReceiver.getTable().getPlayers()) {
                Integer points = player.getPrivateObjective()
                        .stream()
                        .mapToInt(p -> p.scorePoints(player.getGlassWindow()))
                        .sum();
                points += actionReceiver.getTable().getPublicObjectives()
                        .stream()
                        .mapToInt(p -> p.scorePoints(player.getGlassWindow()))
                        .sum();
                points += player.getTokens();
                points -= Math.toIntExact(player.getGlassWindow().getCellList()
                        .stream()
                        .filter(c -> !c.isOccupied())
                        .count());
                ranking.add(new Pair<>(player,points));
            }
            actionReceiver.endGame(ranking);
        };

    }

    /**
     * Gets the list of draft actions.
     * @param marker String, marker of the die drafted.
     * @param dieColor marker of die deciding the color.
     * @param dieNumber marker of die deciding the number.
     * @return list of draft actions.
     */
    @Override
    public ActionCommand getDraftAction(String marker, String dieColor, String dieNumber) {
        return actionReceiver -> {
            Player player = actionReceiver.getTurnPlayer();
            //finds channel to communicate with
            CommunicationChannel cc = actionReceiver.getCommChannels()
                    .stream()
                    .filter(c -> c.getNickname().equals(player.getNickname()))
                    .findFirst().get();

            //make options
            List<Die> dieOptions = new ArrayList<>(actionReceiver.getTable().getPool().getDice());
            if(actionReceiver.getMap().containsKey(dieColor))
                dieOptions = dieOptions
                        .stream()
                        .filter(d -> d.getColor()
                                .equals(actionReceiver
                                        .getMap()
                                        .get(dieColor)
                                        .getColor()))
                        .collect(Collectors.toList());
            if(actionReceiver.getMap().containsKey(dieNumber))
                dieOptions = dieOptions
                        .stream()
                        .filter(d -> d.getNumber() ==
                                (actionReceiver
                                        .getMap()
                                        .get(dieNumber)
                                        .getNumber()))
                        .collect(Collectors.toList());
            //act on answer
            Optional<Die> optionalDie = Optional.empty();
            Identifiable dieChosen = cc.selectObject(new ArrayList<>(dieOptions), StdId.POOL, false, true);
            if (dieChosen.getId().equals(StdId.UNDO.getId())) {
                actionReceiver.resetTurn();
            }else {
                optionalDie = dieOptions.stream().filter(d -> d.getId().equals(dieChosen.getId())).findAny();
                if(optionalDie.isPresent()) {
                    actionReceiver.getTable().getPool().takeDie(optionalDie.get());
                    actionReceiver.getMap().put(marker, optionalDie.get());
                }
            }

            //update view
            actionReceiver
                    .getCommChannels()
                    .forEach(c -> c
                            .updateView(player, !actionReceiver
                                    .getChannel(player.getNickname())
                                    .isOffline()));
            actionReceiver
                    .getCommChannels()
                    .forEach(c -> c.updateView(actionReceiver.getTable().getPool()));

            Game.getLogger().log(Level.FINE,
                    "Drafted die "+ optionalDie
                    , this);
        };
    }

    /**
     * Gets the list of place actions.
     * @param marker String, marker of the die placed.
     * @param adjacencyRestriction boolean, true if there are adjacency restrictions.
     * @param coloRestriction boolean, true if there are color restrictions.
     * @param numberRestriction boolean, true if there are numeric restrictions
     * @return list of place actions.
     */
    @Override
    public ActionCommand getPlaceAction(String marker, boolean adjacencyRestriction, boolean coloRestriction, boolean numberRestriction, boolean forced) {
        return actionReceiver -> {
            Player player = actionReceiver.getTurnPlayer();
            //finds channel to communicate with
            CommunicationChannel cc = actionReceiver.getCommChannels()
                    .stream()
                    .filter(c -> c.getNickname().equals(player.getNickname()))
                    .findFirst().get();

            //get die
            Die die = actionReceiver.getMap().get(marker);

            //filter options
            List<Cell> cells = new ArrayList<>(player.getGlassWindow().availableCells(die, !adjacencyRestriction))
                    .stream()
                    .filter(c -> c.isAllowed(die.getColor()) || !coloRestriction)
                    .filter(c -> c.isAllowed(die.getNumber()) || !numberRestriction)
                    .collect(Collectors.toList());

            if(forced && cells.isEmpty())
                actionReceiver.getTable().getPool().addDie(die);
            else {
                //act on answer
                Identifiable positionChosen = cc.selectObject(new ArrayList<>(cells), StdId.GLASS_WINDOW, false, !forced);
                if (positionChosen.getId().equals(StdId.UNDO.getId())) {
                    actionReceiver.resetTurn();
                } else {
                    player.getGlassWindow().getCellList().stream()
                            .filter(c -> c.getId().equals(positionChosen.getId())).findFirst().get()
                            .placeDie(die, (coloRestriction || numberRestriction));
                }
                actionReceiver
                        .getCommChannels()
                        .forEach(c -> c
                                .updateView(player, !actionReceiver
                                        .getChannel(player.getNickname())
                                        .isOffline()));
                actionReceiver.getCommChannels().forEach(c -> c.updateView(actionReceiver.getTable().getPool()));
                Game.getLogger().log(Level.FINE, "Placed die " + die.toString() + " from " + positionChosen, this);
            }
        };
    }
}
