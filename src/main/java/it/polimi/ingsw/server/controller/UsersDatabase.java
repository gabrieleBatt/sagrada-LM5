package it.polimi.ingsw.server.controller;

import java.util.HashMap;

public class UsersDatabase extends HashMap<String, String> {

    public void newUser(String nickname, String password){
        if(!this.containsKey(nickname))
            this.put(nickname, password);
        else
            throw new IllegalArgumentException("User " + nickname + " already registered");
    }

    public boolean authentication(String nickname, String password){
        return containsKey(nickname) && get(nickname).equals(password);
    }

    public boolean userExists(String nickname){
        return this.containsKey(nickname);
    }

}
