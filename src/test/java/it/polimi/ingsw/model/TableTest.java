package it.polimi.ingsw.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    //TODO
    @Test
    void setPublicObjective() {
        Table t = new Table(new HashSet<>());
        PublicObjective p1 = new PublicObjective(){};
        PublicObjective p2 = new PublicObjective(){};
        PublicObjective p3 = new PublicObjective(){};
        Set<PublicObjective> publicObjectiveSet = new HashSet<>();
        publicObjectiveSet.add(p3);
        publicObjectiveSet.add(p2);
        publicObjectiveSet.add(p1);
        t.setPublicObjective(publicObjectiveSet);


        Assertions.assertEquals(publicObjectiveSet, t.getPublicObjectives());
    }

    @Test
    void setTools() {
    }

    @Test
    void getPlayers() {
    }

    @Test
    void getPublicObjectives() {
    }

    @Test
    void getTools() {
    }

    @Test
    void getDiceBag() {
    }

    @Test
    void getPool() {
    }

    @Test
    void getRoundTrack() {
    }

    @Test
    void getActiveEffects() {
    }

    @Test
    void addEffect() {
    }
}