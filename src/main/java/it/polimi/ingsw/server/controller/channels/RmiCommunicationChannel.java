package it.polimi.ingsw.server.controller.channels;

import it.polimi.ingsw.LogMaker;
import it.polimi.ingsw.net.identifiables.Identifiable;
import it.polimi.ingsw.net.identifiables.StdId;
import it.polimi.ingsw.net.interfaces.RemoteChannel;
import it.polimi.ingsw.server.model.table.Player;
import it.polimi.ingsw.server.model.table.Pool;
import it.polimi.ingsw.server.model.table.RoundTrack;
import it.polimi.ingsw.server.model.table.Table;
import it.polimi.ingsw.server.model.table.glasswindow.Cell;
import it.polimi.ingsw.server.model.table.glasswindow.GlassWindow;
import it.polimi.ingsw.net.interfaces.RemoteGameScreen;
import javafx.util.Pair;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RmiCommunicationChannel extends CommunicationChannel implements RemoteChannel {
    private static final Logger logger = LogMaker.getLogger(RmiCommunicationChannel.class.getName(), Level.ALL);

    private final String nickname;
    private final RemoteGameScreen gameScreen;
    private boolean isOffline;
    private List<Pair<Player,Integer>> scores;

    public RmiCommunicationChannel(RemoteGameScreen gameScreen, String nickname) {
        this.nickname = nickname;
        this.gameScreen = gameScreen;
        this.scores = new ArrayList<>();
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public boolean isOffline() {
        return isOffline;
    }

    @Override
    public void sendMessage(String message) {
        try {
            gameScreen.addMessage(message);
        } catch (RemoteException e) {
            this.setOffline();
        }
    }

    @Override
    public void updateView(Pool pool) {
        try{
            gameScreen.setPool(pool.getDice().stream().map(Identifiable::getId).collect(Collectors.toList()));
            gameScreen.showAll();
        } catch (RemoteException e) {
            this.setOffline();
        }
    }

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

    @Override
    public void updateView(Table table) {
        try {
            gameScreen.setPublicObjective(table.getPublicObjectives().stream().map(Identifiable::getId).collect(Collectors.toList()));
            gameScreen.setTools(table.getTools().stream().map(Identifiable::getId).collect(Collectors.toList()));
            gameScreen.setPlayers(table.getPlayers().stream().map(Player::getNickname).collect(Collectors.toList()));
            gameScreen.showAll();
        } catch (RemoteException e) {
        this.setOffline();
    }
    }

    @Override
    public void updateView(Player player, boolean connected) {
        try {
            gameScreen.setPlayerConnection(player.getNickname(), connected);
            if (player.getNickname().equals(this.nickname))
                gameScreen.setPrivateObjectives(player.getPrivateObjective().stream().map(Identifiable::getId).collect(Collectors.toList()));
            if (player.hasGlassWindow()) {
                gameScreen.setPlayerWindow(player.getNickname(), player.getGlassWindow().getId());
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 5; j++) {
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

    @Override
    public void endGame(List<Pair<Player, Integer>> scores) {
        this.scores = scores;
    }

    @Override
    public List<Pair<String, Integer>> getScores() throws RemoteException {
        return scores
                .stream()
                .map(p -> new Pair<>(p.getKey().getNickname(), p.getValue()))
                .collect(Collectors.toList());
    }

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

    @Override
    public Identifiable selectObject(List<Identifiable> options, Identifiable container, boolean canSkip, boolean undoEnabled) {
        return askClient(options, canSkip, undoEnabled, gameScreen::getInput, container.getId());
    }

    @Override
    public Identifiable chooseFrom(List<Identifiable> options, String message, boolean canSkip, boolean undoEnabled) {
        return askClient(options, canSkip, undoEnabled, gameScreen::getInputFrom, message);
    }


    private Identifiable askClient(List<Identifiable> options, boolean canSkip,
                                   boolean undoEnabled,
                                   BiFunctionRemExc<List, String, String> function,
                                   String string){
        if(isOffline)
            return CommunicationChannel.fakeResponse(canSkip, undoEnabled, options);
        List<String> use= options.stream().map(Identifiable::getId).collect(Collectors.toList());
        if(canSkip)
            use.add(StdId.SKIP.getId());
        if(undoEnabled)
            use.add(StdId.UNDO.getId());
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

    @Override
    public void setOffline() {
        logger.log(Level.WARNING, getNickname() + " is offline");
        //TODO -- disconnect properly
        isOffline = true;
    }

    @FunctionalInterface
    private interface BiFunctionRemExc<T, U, R>{
        R apply(T t, U u) throws RemoteException;
    }
}
