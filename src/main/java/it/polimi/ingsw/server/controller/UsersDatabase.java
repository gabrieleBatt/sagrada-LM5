package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.shared.LogMaker;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Map of users password and nickname
 */
class UsersDatabase extends HashMap<String, String> {

    private static Logger logger = LogMaker.getLogger(UsersDatabase.class.getName(), Level.ALL);
    private static UsersDatabase usersDatabase = new UsersDatabase();

    private UsersDatabase(){
    }

    static void newUser(String nickname, String password){
        if(!usersDatabase.containsKey(nickname)) {
            usersDatabase.put(nickname, password);
            logger.log(Level.FINE, "New User:" + nickname + ", " + password);
        }else
            throw new IllegalArgumentException("User " + nickname + " already registered");
    }

    static boolean authentication(String nickname, String password){
        boolean ret = usersDatabase.containsKey(nickname) && usersDatabase.get(nickname).equals(password);
        logger.log(Level.FINE, "Authentication", ret);
        return ret;
    }

    static boolean userExists(String nickname){
        return usersDatabase.containsKey(nickname);
    }

    static boolean createOrAuthenticate(String nickname, String password) {
        if (!userExists(nickname)) {
            UsersDatabase.newUser(nickname, password);
            return true;
        } else return authentication(nickname, password);
    }
}
