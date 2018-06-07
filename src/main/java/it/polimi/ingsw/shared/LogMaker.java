package it.polimi.ingsw.shared;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.*;

public class LogMaker {

    private static final Level level = Level.ALL;
    private static FileHandler fh;
    private static ConsoleHandler ch;
    private static Formatter formatter;

    static {
        try {
            formatter = new Formatter() {
                @Override
                public String format(LogRecord record) {
                    String s = record.getMessage();
                    if(record.getParameters() != null){
                        s += " " + Arrays.toString(record.getParameters());
                    }
                    return s + "\n";
                }
            };
            fh = new FileHandler("logs/serverLog.log");
            fh.setLevel(level);
            fh.setFormatter(formatter);
            ch = new ConsoleHandler();
            ch.setLevel(level);
            ch.setFormatter(formatter);
        } catch (IOException e) {
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
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
            logger.addHandler(fh);
            logger.addHandler(ch);
        } catch (SecurityException | IOException e) {
        }
        return logger;
    }
}
