package it.polimi.ingsw.server.controller;

import java.util.HashMap;

/**
 * Map of users password and nickname
 */
public class UsersDatabase extends HashMap<String, String> {

    private static UsersDatabase usersDatabase = new UsersDatabase();

    private UsersDatabase(){

    }

    public static void newUser(String nickname, String password){
        if(!usersDatabase.containsKey(nickname))
            usersDatabase.put(nickname, password);
        else
            throw new IllegalArgumentException("User " + nickname + " already registered");
    }

    public static boolean authentication(String nickname, String password){
        return usersDatabase.containsKey(nickname) && usersDatabase.get(nickname).equals(password);
    }

    public static boolean userExists(String nickname){
        return usersDatabase.containsKey(nickname);
    }

}
