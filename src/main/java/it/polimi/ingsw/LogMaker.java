package it.polimi.ingsw;

import java.io.IOException;
import java.util.logging.*;

public class LogMaker {

    private static final Level level = Level.FINE;
    private static FileHandler fh;
    private static ConsoleHandler ch;

    static {
        try {
            fh = new FileHandler("logs/serverLog.log");
            fh.setLevel(level);
            fh.setFormatter(new SimpleFormatter());
            ch = new ConsoleHandler();
            ch.setLevel(level);
        } catch (IOException e) {
            System.exit(-1);
        }
    }

    private LogMaker(){
    }

    public static Logger getLogger(String name, Level level) {
        Logger logger = Logger.getLogger(name);
        logger.setLevel(level);
        try {
            FileHandler fileHandler = new FileHandler("logs/"+name+".log");
            fileHandler.setLevel(level);
            logger.addHandler(fileHandler);
            logger.addHandler(fh);
            logger.addHandler(ch);
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (SecurityException | IOException e) {
            System.exit(-1);
        }
        return logger;
    }
}
