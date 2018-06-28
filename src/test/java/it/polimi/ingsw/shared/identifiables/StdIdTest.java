package it.polimi.ingsw.shared.identifiables;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class StdIdTest {

    @DisplayName("Finding StdId")
    @Test
    void getStdId() {
        Assertions.assertThrows(NoSuchElementException.class, ()->StdId.getStdId("test"));
    }
}