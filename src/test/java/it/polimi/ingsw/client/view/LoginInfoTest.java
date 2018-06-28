package it.polimi.ingsw.client.view;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginInfoTest {

    @DisplayName("Login info creation")
    @Test
    void create() {
        LoginInfo loginInfo = new LoginInfo("connection", "nick", 1, "ip", "pass");
        Assertions.assertEquals("ip", loginInfo.ip);
        Assertions.assertEquals("connection", loginInfo.connectionType);
        Assertions.assertEquals("nick", loginInfo.nickname);
        Assertions.assertEquals(1, loginInfo.portNumber);
        Assertions.assertEquals("pass", loginInfo.password);
        Assertions.assertEquals("connection nick 1 ip", loginInfo.toString());
    }
}