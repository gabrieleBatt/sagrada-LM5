package it.polimi.ingsw.client.connection;

import it.polimi.ingsw.client.view.EndGameInfo;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Optional;

public abstract class ConnectionManager {

    public abstract Optional<EndGameInfo> run() throws IOException, ParseException, InterruptedException;

    public abstract boolean login() throws IOException, ParseException, NotBoundException;

}
