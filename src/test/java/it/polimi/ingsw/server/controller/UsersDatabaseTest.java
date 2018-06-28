package it.polimi.ingsw.server.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsersDatabaseTest {

    @DisplayName("New user and authentication")
    @Test
    void newUser() {
        UsersDatabase.newUser("test1", "password");
        Assertions.assertTrue(UsersDatabase.userExists("test1"));
        Assertions.assertFalse(UsersDatabase.userExists("test2"));
        Assertions.assertTrue(UsersDatabase.authentication("test1", "password"));
        Assertions.assertFalse(UsersDatabase.authentication("test1", "wrongPass"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> UsersDatabase.newUser("test1", "test"));
        Assertions.assertTrue(UsersDatabase.createOrAuthenticate("test2", "pass"));
        Assertions.assertFalse(UsersDatabase.createOrAuthenticate("test2", "noPass"));
    }
}