package it.polimi.ingsw.client.connection;

import it.polimi.ingsw.client.view.EndGameInfo;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Optional;

/**
 * Manages the view
 */
public interface ConnectionManager {

    /**
     * Handles the connection until the game ends
     * @return info representing the result of the game
     * @throws IOException communication failed
     * @throws ParseException communication failed
     * @throws InterruptedException communication failed
     */
    Optional<EndGameInfo> run() throws IOException, ParseException, InterruptedException;

    /**
     * performs the login and returns the result
     * @return true if the login was successful
     * @throws IOException communication failed
     * @throws ParseException communication failed
     * @throws NotBoundException communication failed
     */
    boolean login() throws IOException, ParseException, NotBoundException;

}
