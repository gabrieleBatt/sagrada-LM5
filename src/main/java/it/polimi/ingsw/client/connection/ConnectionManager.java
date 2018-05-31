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

    Optional<EndGameInfo> run() throws IOException, ParseException, InterruptedException;

    boolean login() throws IOException, ParseException, NotBoundException;

}
