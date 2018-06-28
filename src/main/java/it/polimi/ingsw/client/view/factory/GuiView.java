package it.polimi.ingsw.client.view.factory;

import it.polimi.ingsw.client.view.gui.GuiConnectionScreen;
import it.polimi.ingsw.client.view.gui.GuiEndScreen;
import it.polimi.ingsw.client.view.gui.GuiGameScreen;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class GuiView implements ViewAbstractFactory {


    public static final double WIDTH;
    public static final double HEIGHT;
    public static final double DEFAULT_HEIGHT;
    public static final double DEFAULT_WIDTH;
    public static final double BIG_SPACING;
    public static final double SMALL_SPACING;
    public static final double MEDIUM_SPACING;
    public static final double BIGGER_SPACING;
    public static final double MESSAGES_MIN_WIDTH;

    static {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        DEFAULT_HEIGHT = 680;
        DEFAULT_WIDTH  = 1280;



        if(DEFAULT_WIDTH/DEFAULT_HEIGHT < primaryScreenBounds.getWidth()/primaryScreenBounds.getHeight()) {
            HEIGHT = primaryScreenBounds.getHeight();
            WIDTH = DEFAULT_WIDTH * HEIGHT / DEFAULT_HEIGHT;
        }else {
            WIDTH = primaryScreenBounds.getWidth();
            HEIGHT = DEFAULT_HEIGHT * WIDTH / DEFAULT_WIDTH;
        }
        MESSAGES_MIN_WIDTH = 350/DEFAULT_WIDTH*WIDTH;
        BIGGER_SPACING = 130/DEFAULT_WIDTH*WIDTH;
        BIG_SPACING = 50/DEFAULT_WIDTH*WIDTH;
        SMALL_SPACING = 5/DEFAULT_WIDTH*WIDTH;
        MEDIUM_SPACING = 15/DEFAULT_WIDTH*WIDTH;
    }

    @Override
    public GameScreen makeGameScreen() {
        return new GuiGameScreen();
    }

    @Override
    public ConnectionScreen makeConnectionScreen() {
        return new GuiConnectionScreen();
    }

    @Override
    public EndScreen makeEndScreen() { return new GuiEndScreen();
    }

}
