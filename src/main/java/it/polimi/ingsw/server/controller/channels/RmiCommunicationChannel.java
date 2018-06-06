package it.polimi.ingsw.server.controller.channels;

import it.polimi.ingsw.shared.LogMaker;
import it.polimi.ingsw.shared.identifiables.Identifiable;
import it.polimi.ingsw.shared.identifiables.StdId;
import it.polimi.ingsw.shared.interfaces.RemoteChannel;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.Pool;
import it.polimi.ingsw.server.model.table.RoundTrack;
import it.polimi.ingsw.server.model.table.Table;
import it.polimi.ingsw.server.model.table.glasswindow.Cell;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;
import it.polimi.ingsw.shared.interfaces.RemoteGameScreen;
import javafx.util.Pair;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class RmiCommunicationChannel extends CommunicationChannel implements RemoteChannel {
    private static final Logger logger = LogMaker.getLogger(RmiCommunicationChannel.class.getName(), Level.ALL);

    private final RemoteGameScreen gameScreen;
    private boolean isOffline;
    private List<Pair<Player,Integer>> scores;

    public RmiCommunicationChannel(RemoteGameScreen gameScreen, String nickname) {
        super(nickname);
        this.gameScreen = gameScreen;
        this.scores = new ArrayList<>();
    }

    /**
     * Returns true if player is connected.
     * @return true if is connected, false otherwise.
     */
    @Override
    public boolean isOffline() {
        return isOffline;
    }

    /**
     * Sends a message to visualize
     */
    @Override
    public void sendMessage(String message) {
        try {
            gameScreen.addMessage(message);
            gameScreen.showAll();
        } catch (RemoteException e) {
            this.setOffline();
        }
    }

    /**
     * Updates any change in the pool.
     */
    @Override
    public void updateView(Pool pool) {
        try{
            gameScreen.setPool(pool.getDice().stream().map(Identifiable::getId).collect(Collectors.toList()));
            gameScreen.showAll();
        } catch (RemoteException e) {
            this.setOffline();
        }
    }

    /**
     * Updates any change in the roundTrack.
     */
    @Override
    public void updateView(RoundTrack roundTrack) {
        List<List<String>> completeRoundTrack = new ArrayList<>();
        for(int i= 1;  i<roundTrack.getRound()-1; i++){
            completeRoundTrack.add(roundTrack.getDice(i).stream().map(Identifiable::getId).collect(Collectors.toList()));
        }
        try{
            gameScreen.setRoundTrack(completeRoundTrack);
            gameScreen.showAll();
        } catch (RemoteException e) {
            this.setOffline();
        }
    }

    /**
     * Updates any change in the public cards(objectives and tools) and the name of the players.
     */
    @Override
    public void updateView(Table table) {
        try {
            gameScreen.setPublicObjective(table.getPublicObjectives().stream().map(Identifiable::getId).collect(Collectors.toList()));
            gameScreen.setTools(table.getTools().stream().map(Identifiable::getId).collect(Collectors.toList()));
            List<String> players = table.getPlayers().stream().map(Player::getNickname).collect(Collectors.toList());
            players.remove(this.getNickname());
            players.add(0, this.getNickname());
            gameScreen.setPlayers(players);
            gameScreen.showAll();
        } catch (RemoteException e) {
        this.setOffline();
        }
    }

    /**
     * Updates any change in the player and their glassWindow.
     * @param connected true if player is still connected
     * @param player player to update
     */
    @Override
    public void updateView(Player player, boolean connected) {
        try {
            gameScreen.setPlayerConnection(player.getNickname(), connected);
            if (player.getNickname().equals(this.getNickname())) {
                gameScreen.setPrivateObjectives(player.getPrivateObjective().stream().map(Identifiable::getId).collect(Collectors.toList()));
            }
            if (player.hasGlassWindow()) {
                gameScreen.setPlayerWindow(player.getNickname(), player.getGlassWindow().getId());
                for (int i = 0; i < GlassWindow.ROWS; i++) {
                    for (int j = 0; j < GlassWindow.COLUMNS; j++) {
                        Cell c = player.getGlassWindow().getCell(i, j);
                        if (c.isOccupied())
                            gameScreen.setCellContent(player.getNickname(), i, j, c.getDie().getId());
                    }
                }
            }
            gameScreen.setPlayerToken(player.getNickname(), player.getTokens());
            gameScreen.showAll();
        } catch (RemoteException e) {
            this.setOffline();
        }
    }

    /**
     * tells the client the game has ended and the results
     * @param scores - list of players and their scores
     */
    @Override
    public void endGame(List<Pair<Player, Integer>> scores) {
        this.scores = scores;
    }

    /**
     * Return the scores
     * @return the scores
     */
    @Override
    public List<Pair<String, Integer>> getScores() throws RemoteException {
        return scores
                .stream()
                .map(p -> new Pair<>(p.getKey().getNickname(), p.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Returns the chosen glassWindow among the given.
     * @param glassWindows List of glassWindows given.
     * @return Object glassWindow, the one chosen.
     */
    @Override
    public GlassWindow chooseWindow(List<GlassWindow> glassWindows) {
        try {
            Timer timer = new Timer();
            startTimer(timer, this);
            String use = gameScreen.getWindow(glassWindows.stream().map(Identifiable::getId).collect(Collectors.toList()));
            Optional<GlassWindow> optionalGlassWindow = glassWindows.stream().filter(g -> g.getId().equals(use)).findAny();
            if (optionalGlassWindow.isPresent()) {
                endTimer(timer);
                return optionalGlassWindow.get();
            }
        } catch (RemoteException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        setOffline();
        return glassWindows.get(ThreadLocalRandom.current().nextInt(0, glassWindows.size()));
    }

    /**
     * Returns the chosen option among the given.
     * @param options List of options given.
     * @param container it's the object that contains the options
     * @param canSkip tells if the button canSkip is available for that player in that move
     * @param undoEnabled tells if the button undo is available for that player in that move
     * @return The option chosen.
     */
    @Override
    public Identifiable selectObject(List<Identifiable> options, Identifiable container, boolean canSkip, boolean undoEnabled) {
        return askClient(options, canSkip, undoEnabled, gameScreen::getInput, container.getId());
    }

    /**
     * Returns the chosen option among the given.
     * @param options List of options given.
     * @return The option chosen.
     */
    @Override
    public Identifiable chooseFrom(List<Identifiable> options, String message, boolean canSkip, boolean undoEnabled) {
        return askClient(options, canSkip, undoEnabled, gameScreen::getInputFrom, message);
    }


    private Identifiable askClient(List<Identifiable> options, boolean canSkip, boolean undoEnabled, BiFunctionRemExc<List, String, String> function, String string){
        if(isOffline) return CommunicationChannel.fakeResponse(canSkip, undoEnabled, options);

        //Builds options
        List<String> use = options.stream().map(Identifiable::getId).collect(Collectors.toList());
        if(canSkip)
            use.add(StdId.SKIP.getId());
        if(undoEnabled)
            use.add(StdId.UNDO.getId());

        //Ask input
        Timer timer = new Timer();
        startTimer(timer, this);
        String ret;
        try {
            ret = function.apply(use, string);
        } catch (RemoteException e) {
            setOffline();
            return CommunicationChannel.fakeResponse(canSkip, undoEnabled, options);
        }
        endTimer(timer);

        //Act on response
        if(canSkip && ret.equals(StdId.SKIP.getId()))
            return StdId.SKIP;
        if(undoEnabled&& ret.equals((StdId.UNDO.getId())))
            return StdId.UNDO;

        Optional<Identifiable> selection = options
                .stream()
                .filter(op -> op.getId().equals(ret))
                .findFirst();
        if (selection.isPresent()) {
            return selection.get();
        }

        setOffline();
        return CommunicationChannel.fakeResponse(canSkip, undoEnabled, options);
    }

    /**
     * Used to set a channel as it went offline
     */
    @Override
    public void setOffline() {
        try {
            UnicastRemoteObject.unexportObject(this, true);
            logger.log(Level.FINE, getNickname() + " is offline");
            super.setOffline();
            isOffline = true;
        } catch (NoSuchObjectException e) {
            logger.log(Level.WARNING, "UnExport failed");
        }
    }

    @FunctionalInterface
    private interface BiFunctionRemExc<T, U, R>{
        R apply(T t, U u) throws RemoteException;
    }
}
