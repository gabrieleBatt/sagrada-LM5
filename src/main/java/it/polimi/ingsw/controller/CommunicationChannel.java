package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.table.glassWindow.GlassWindow;

import java.util.List;

public abstract class CommunicationChannel{

    public abstract String getNickname();

    public abstract boolean isConnected();

    public abstract void updateView();

    public abstract GlassWindow chooseWindow(List<GlassWindow> glassWindows);

    public abstract String selectOption(List<String> ids);

    public abstract String chooseFrom(List<String> options);
}