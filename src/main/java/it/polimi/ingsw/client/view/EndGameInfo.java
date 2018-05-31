package it.polimi.ingsw.client.view;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public final class EndGameInfo {

    private final List<Pair<String,Integer>> ranking;

    public EndGameInfo(List<Pair<String, Integer>> ranking){
        this.ranking = ranking;
    }

    public List<Pair<String, Integer>> getRanking() {
        return new ArrayList<>(ranking);
    }
}
