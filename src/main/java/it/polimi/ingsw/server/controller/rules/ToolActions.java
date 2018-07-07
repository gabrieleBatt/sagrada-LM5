package it.polimi.ingsw.server.controller.rules;

import it.polimi.ingsw.shared.Message;
import it.polimi.ingsw.shared.identifiables.Identifiable;
import it.polimi.ingsw.shared.identifiables.StdId;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.controller.channels.CommunicationChannel;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.dice.Die;
import it.polimi.ingsw.server.model.table.glasswindow.Cell;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Rules for tools actions
 */
public class ToolActions {

    private ToolActions(){}

    /**
     * Generates the action command used to let the player choose how to modify a die.
     * @param marker String representing object Die, die to modify.
     * @param options List of List of Identifiable to chose from.
     * @return ActionCommand used to et the player choose how to modify a die.
     */
    public static ActionCommand setActionCommand(String marker, List<List<Identifiable>> options) {
        return actionReceiver -> {
            Player player = actionReceiver.getTurnPlayer();
            CommunicationChannel cc = actionReceiver.getChannel(player.getNickname());
            Die die = actionReceiver.getMap().get(marker);
            List<Identifiable> optionList = options.get(die.getNumber() - 1);
            Identifiable actionChosen = cc.chooseFrom(optionList, Message.SET_DIE.toString(), false, false);
            die.setNumber(Integer.parseInt(actionChosen.getId()));

            Game.getLogger().log(Level.FINE, "New die value: " + die.getNumber(), die);

            actionReceiver.sendAll(die.getId().substring(0, 2), Message.BEEN_SET.name());

        };
    }


    /**
     * Generates the action command used to let the player roll a die.
     * @param marker String representing Object Die, die to roll.
     * @param optionList  List of possible value of die after rolling.
     * @return ActionCommand used to let the player roll a die.
     */
    public static ActionCommand randomActionCommand(String marker, List<List<Identifiable>> optionList){
        return actionReceiver -> {
            Die die = actionReceiver.getMap().get(marker);
            int index = ThreadLocalRandom.current().nextInt(0, optionList.get(die.getNumber()-1).size());
            die.setNumber(Integer.parseInt(optionList.get(die.getNumber()-1).get(index).getId()));
            actionReceiver.sendAll(die.getId().substring(0, 2), Message.RANDOM_NUMBER.name());
            Game.getLogger().log(Level.FINE,"Dice rolled: " + die.toString(),die);
        };
    }

    /**
     * Generates the action command used to select a die. It receives a StdId identifying the
     * dice container (Pool, RoundTrack, Window). It calls a method to do the actual selection
     * depending on the StdId received.
     * @param marker String, die marker.
     * @param stdId container identifier.
     * @return action command used to select a die.
     */
        public static ActionCommand selectActionCommand(String marker, StdId stdId){
            switch (stdId){
                case POOL:
                    return selectFromPool(marker);
                case ROUND_TRACK:
                    return  selectFromRoundTrack(marker);
                case GLASS_WINDOW:
                    return selectFromWindow(marker);
                default: throw new IllegalArgumentException();
            }
        }

    /**
     * Generates the action command used to select a die from Window.
     * @param marker String, die marker.
     * @return action command used to select a die from Window.
     */
        private static ActionCommand selectFromWindow(String marker){
            return actionReceiver -> {
                Player player = actionReceiver.getTurnPlayer();
                CommunicationChannel cc = actionReceiver.getChannel(player.getNickname());
                Identifiable cellChosen = getCellAmongProposed(player,cc);
                if (cellChosen.getId().equals(StdId.UNDO.getId())) {
                    actionReceiver.resetTurn();
                }else {
                    Die die = player.getGlassWindow()
                            .getCell(Character.getNumericValue(cellChosen.getId().charAt(0)),Character.getNumericValue(cellChosen.getId().charAt(1))).getDie();

                    actionReceiver.getMap().put(marker, die);

                    actionReceiver.sendAll(die.getId().substring(0, 2), Message.SELECT_DIE.name());

                    Game.getLogger().log(Level.FINE,"Added die in cell " + cellChosen + " to HashMap", player.getGlassWindow()
                        .getCell(Character.getNumericValue(cellChosen.getId().charAt(0)),Character.getNumericValue(cellChosen.getId().charAt(1))).getDie());
                }
            };
        }

    /**
     * Generates the action command used to select a die from Pool.
     * @param marker String, die marker.
     * @return action command used to select a die from Pool.
     */
    private static ActionCommand selectFromPool(String marker){
            return actionReceiver -> {
                Player player = actionReceiver.getTurnPlayer();
                CommunicationChannel cc = actionReceiver.getChannel(player.getNickname());
                Collection<Die> dieOptions = actionReceiver.getTable().getPool().getDice();
                Identifiable dieChosen = cc.selectObject(new ArrayList<>(dieOptions), StdId.POOL, false, true);
                if (dieChosen.getId().equals(StdId.UNDO.getId())) {
                    actionReceiver.resetTurn();
                }else {
                    Die die = getDieChosen(dieOptions,dieChosen);
                    actionReceiver.getMap().put(marker, die);
                    actionReceiver.sendAll(die.getId().substring(0, 2), Message.SELECT_DIE.name());

                    Game.getLogger().log(Level.FINE,"Added die form Pool" + dieChosen + " to HashMap",dieChosen);

                }

            };
        }

    /**
     * Generates the action command used to select a die from RoundTrack.
     * @param marker String, die marker.
     * @return action command used to select a die from RoundTrack.
     */
    private static ActionCommand selectFromRoundTrack(String marker){
            return actionReceiver -> {
                Player player = actionReceiver.getTurnPlayer();
                CommunicationChannel cc = actionReceiver.getChannel(player.getNickname());
                List<Die> dieOptions = getDiceOnRoundTrack(actionReceiver);
                Identifiable dieChosen = cc.selectObject(new ArrayList<>(dieOptions), StdId.ROUND_TRACK, false, true);
                if (dieChosen.getId().equals(StdId.UNDO.getId())) {
                    actionReceiver.resetTurn();
                } else {
                    Die die = getDieChosen(dieOptions,dieChosen);
                    actionReceiver.getMap().put(marker, die);
                    actionReceiver.sendAll(die.getId().substring(0, 2), Message.SELECT_DIE.name());

                    Game.getLogger().log(Level.FINE, "Added die form RoundTrack" + dieChosen + " to HashMap", dieChosen);
                }
            };
        }

    /**
     * Generates the action command used to swap a die. It receives a StdId identifying the
     * dice container (Pool, RoundTrack, Window). It calls a method to do the actual swapping
     * depending on the StdId received.
     * @param stdId container identifier.
     * @param markerDieToTake String, marker of die to take from HashMap.
     * @param dieColor DieColor, color of the die to draft.
     * @param dieNumber Integer, numeric value of the die to draft.
     * @param markerDieToSet String, marker of die to set.
     * @return action command used to swap a die.
     */
        public static ActionCommand swapActionCommand(String dieColor, String dieNumber, StdId stdId, String markerDieToTake, String markerDieToSet){
            switch (stdId){
                case POOL:
                    return swapFromPool(dieColor,dieNumber,markerDieToTake,markerDieToSet);
                case ROUND_TRACK:
                    return  swapFromRoundTrack(dieColor,dieNumber,markerDieToTake,markerDieToSet);
                case DICE_BAG:
                    return swapFromDiceBag(markerDieToSet,markerDieToTake);
                    default: throw new IllegalArgumentException();
            }
        }

    /**
     * Generates the action command used to swap a die with another one
     * in dice bag.
     * @param markerDieToTake String, marker of die to take from HashMap.
     * @param markerDieToSet String, marker of die to set.
     * @return action command used to swap a die with another one
     * in dice bag.
     */
    private static ActionCommand swapFromDiceBag(String markerDieToSet, String markerDieToTake) {
            return actionReceiver -> {
                Die dieToSet = actionReceiver.getMap().get(markerDieToSet);
                actionReceiver.getTable().getDiceBag().placeDie(dieToSet);
                Die dieToTake = actionReceiver.getTable().getDiceBag().drawDice(1).iterator().next();
                actionReceiver.getMap().put(markerDieToTake,dieToTake);

                actionReceiver.sendAll(dieToSet.getId().substring(0, 2),Message.SWAPPED.name(), dieToTake.getId().substring(0, 2));
            };
    }

    /**
     * Generates the action command used to swap a die with another one
     * on the pool.
     * @param markerDieToTake String, marker of die to take from HashMap.
     * @param markerDieToSet String, marker of die to set.
     * @param dieColor marker of die deciding the color.
     * @param dieNumber marker of die deciding the number.
     * @return action command used to swap a die with another one
     * on the pool.
     */
    private static ActionCommand swapFromPool( String dieColor, String dieNumber,String markerDieToTake, String markerDieToSet){
        return actionReceiver -> {
            Player player = actionReceiver.getTurnPlayer();
            CommunicationChannel cc = actionReceiver.getChannel(player.getNickname());
            List<Die> dieOptions = new ArrayList<>(actionReceiver.getTable().getPool().getDice());
            dieOptions = filter(actionReceiver, dieColor,dieNumber,dieOptions);
            Identifiable idDieChosen = cc.selectObject(new ArrayList<>(dieOptions), StdId.POOL, false, true);
            if (idDieChosen.getId().equals(StdId.UNDO.getId())) {
                actionReceiver.resetTurn();
            }else {
                Die dieChosen = getDieChosen(dieOptions, idDieChosen);
                Die dieFromMap = actionReceiver.getMap().get(markerDieToTake);
                actionReceiver.getTable().getPool().swapDice(dieFromMap, dieChosen);
                actionReceiver.getMap().put(markerDieToSet, dieChosen);
                Game.getLogger().log(Level.FINE,
                        "Switched die "+ dieFromMap +" with die "+dieChosen+" from Pool");
            }
        };
    }

    /**
     * Generates the action command used to swap a die with another one
     * on the roundTrack.
     * @param markerDieToTake String, marker of die to take from HashMap.
     * @param markerDieToSet String, marker of die to set.
     * @param dieColor marker of die deciding the color.
     * @param dieNumber marker of die deciding the number.
     * @return action command used to swap a die with another one
     * on the roundTrack.
     */
    private static ActionCommand swapFromRoundTrack( String dieColor, String dieNumber,String markerDieToTake, String markerDieToSet){
        return actionReceiver -> {
            Player player = actionReceiver.getTurnPlayer();
            CommunicationChannel cc = actionReceiver.getChannel(player.getNickname());
            List<Die> dieOptions = getDiceOnRoundTrack(actionReceiver);
            dieOptions = filter(actionReceiver, dieColor,dieNumber,dieOptions);
            Identifiable dieChosen = cc.selectObject(new ArrayList<>(dieOptions), StdId.ROUND_TRACK, false, true);
            if (dieChosen.getId().equals(StdId.UNDO.getId())) {
                actionReceiver.resetTurn();
            }else {
                Die die = getDieChosen(dieOptions, dieChosen);
                Die dieToSwap = actionReceiver.getMap().get(markerDieToTake);
                actionReceiver.getMap().put(markerDieToSet,die);
                actionReceiver.getTable().getRoundTrack().switchDie(dieToSwap, die);
                Game.getLogger().log(Level.FINE,
                        "Switched die "+ die +" with die "+dieToSwap+" from Window");

                actionReceiver.sendAll(die.getId().substring(0, 2), Message.SWAPPED.name(), dieToSwap.getId().substring(0, 2));

            }
        };
    }

    /**
     * Gets the cell chosen by player among those proposed.
     * @param player Object Player, player.
     * @return cell chosen by player among those proposed.
     */
    private static Identifiable getCellAmongProposed (Player player, CommunicationChannel cc){
        List<Cell> cellsOptions = player.getGlassWindow().getCellList()
                .stream()
                .filter(Cell::isOccupied)
                .collect(Collectors.toList());
        return cc.selectObject(new ArrayList<>(cellsOptions), StdId.GLASS_WINDOW, false, true);

    }

    /**
     * Gets the list of dice on roundTrack.
     * @param actionReceiver object Game.
     * @return list of dice on roundTrack.
     */
    private static List<Die> getDiceOnRoundTrack (Game actionReceiver){
        List<Die> dieOptions = new ArrayList<>();
        actionReceiver.getTable().getRoundTrack().getCollections().forEach(dieOptions::addAll);
        return dieOptions;
    }

    /**
     *Generates the action command used to move a die on the window.
     * @param dieColor marker of die deciding the color.
     * @param dieNumber marker of die deciding the number.
     * @param ignoreColorRestriction Boolean, true if color restriction has to be ignored.
     * @param ignoreNumberRestriction Boolean, true if number restriction has to be ignored.
     * @param ignoreSurroundingRestriction Boolean, true if surrounding restriction has to be ignored.
     * @return action command used to move a die on the window.
     */
    public static ActionCommand moveActionCommand(String dieColor, String dieNumber, Boolean ignoreColorRestriction, Boolean ignoreNumberRestriction, Boolean ignoreSurroundingRestriction, Boolean canSkip){
        return actionReceiver -> {
            Player player = actionReceiver.getTurnPlayer();
            CommunicationChannel cc = actionReceiver.getChannel(player.getNickname());

            //get available cells
            List<Cell> cellOptions = availableCells(actionReceiver, dieColor, dieNumber, player.getGlassWindow().getCellList());

            //choose cell
            Identifiable cellDieChosen = cc.selectObject(new ArrayList<>(cellOptions), StdId.GLASS_WINDOW, canSkip, true);

            if (cellDieChosen.getId().equals(StdId.UNDO.getId())) {
                actionReceiver.resetTurn();
            }else if(!cellDieChosen.getId().equals(StdId.SKIP.getId())){
                Cell cellChosen = getCellChosen(cellOptions, cellDieChosen);
                Die die = cellChosen.getDie();
                cellChosen.removeDie();
                List<Cell> cellList = (List<Cell>)player.getGlassWindow().availableCells(die, ignoreSurroundingRestriction);
                cellList.remove(cellChosen);

                cellList = cellList.stream()
                        .filter(c -> c.isAllowed(die.getColor()) || ignoreColorRestriction)
                        .filter(c -> c.isAllowed(die.getNumber()) || ignoreNumberRestriction)
                        .collect(Collectors.toList());


                Identifiable cellChosenId = cc.selectObject(new ArrayList<>(cellList), StdId.GLASS_WINDOW, false, true);
                if(cellChosenId.getId().equals(StdId.UNDO.getId())) {
                    actionReceiver.resetTurn();
                }else {
                    Cell cell = getCellChosen(cellList, cellChosenId);
                    cell.placeDie(die, true);

                    actionReceiver.getCommChannels().forEach(c -> c.updateView(player, !cc.isOffline()));
                    actionReceiver.sendAll(cell.getId(), Message.MOVED.name(), die.getId().substring(0, 2));
                    Game.getLogger().log(Level.FINE, "Die moved");
                }
            }
        };
    }

    private static List<Cell> availableCells(Game actionReceiver, String dieColor, String dieNumber, List<Cell> cellOptions){
        cellOptions = cellOptions
                .stream()
                .filter(Cell::isOccupied)
                .collect(Collectors.toList());
        if(actionReceiver.getMap().containsKey(dieColor))
            cellOptions = cellOptions
                    .stream()
                    .filter(d -> d.getDie().getColor()
                            .equals(actionReceiver
                                    .getMap()
                                    .get(dieColor)
                                    .getColor()))
                    .collect(Collectors.toList());
        if(actionReceiver.getMap().containsKey(dieNumber))
            cellOptions = cellOptions
                    .stream()
                    .filter(d -> d.getDie().getNumber() ==
                            (actionReceiver
                                    .getMap()
                                    .get(dieNumber)
                                    .getNumber()))
                    .collect(Collectors.toList());
        return cellOptions;
    }

    private static List<Die> filter(Game actionReceiver, String dieColor, String dieNumber, List<Die> dieOptions){
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
        return dieOptions;
    }

    /**
     * Gets Cell having the same id as the one give.
     * @param cellOptions List df cells.
     * @param cellChosen Id cell given.
     * @return Cell having the same id as the one given.
     */
    private static Cell getCellChosen(Collection<Cell> cellOptions, Identifiable cellChosen){
        Optional<Cell> optionalCell = cellOptions
                .stream()
                .filter(c -> c.getId().equals(cellChosen.getId()))
                .findFirst();
        if(optionalCell.isPresent()){
            return optionalCell.get();
        }
        else
            throw new NoSuchElementException();
    }

    /**
     * Gets Die having the same id as the one give.
     * @param dieOptions List of dice.
     * @param dieChosen Id die given.
     * @return Die having the same id as the one given.
     */
    private static Die getDieChosen(Collection<Die> dieOptions, Identifiable dieChosen){
        Optional<Die> optionalCell = dieOptions
                .stream()
                .filter(c -> c.getId().equals(dieChosen.getId()))
                .findFirst();
        if(optionalCell.isPresent()){
            return optionalCell.get();
        }
        else
            throw new NoSuchElementException();
    }
}
