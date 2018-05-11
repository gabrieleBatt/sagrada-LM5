package it.polimi.ingsw;

import java.io.IOException;
import java.util.logging.*;

public class LogMaker {

    private LogMaker(){

    }

    public static Logger getLogger(String name, Level level) {
        Logger logger = Logger.getLogger(name);
        logger.setLevel(level);
        try {
            FileHandler fileHandler = new FileHandler("logs/"+name+".log");
            fileHandler.setLevel(level);
            logger.addHandler(fileHandler);
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (SecurityException | IOException e) {
            System.exit(-1);
        }
        return logger;
    }
}
