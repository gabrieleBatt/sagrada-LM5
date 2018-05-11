package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.table.glassWindow.GlassWindow;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class used for testing; returns a plausible 
 */
public class MockComunicationChannell extends CommunicationChannel{
    String nickName;

    public  MockComunicationChannell(String nickName){
        this.nickName=nickName;
    }
    @Override
    public String getNickname() {
        return this.nickName;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void updateView() {
    }

    @Override
    public GlassWindow chooseWindow(List<GlassWindow> glassWindows) {
        int index = ThreadLocalRandom.current().nextInt(0, glassWindows.size());
        return glassWindows.get(index);
    }

    @Override
    public String selectOption(List<String> ids) {
        int index = ThreadLocalRandom.current().nextInt(0, ids.size());
        return ids.get(index);
    }

    @Override
    public String chooseFrom(List<String> options) {
        int index = ThreadLocalRandom.current().nextInt(0, options.size());
        return options.get(index);
    }
}
