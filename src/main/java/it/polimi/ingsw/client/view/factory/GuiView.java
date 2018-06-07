package it.polimi.ingsw.client.view.factory;

import it.polimi.ingsw.client.view.gui.GuiConnectionScreen;
import it.polimi.ingsw.client.view.gui.GuiEndScreen;
import it.polimi.ingsw.client.view.gui.GuiGameScreen;

public class GuiView implements ViewAbstractFactory {


    @Override
    public GameScreen makeGameScreen() {
        return new GuiGameScreen();
    }

    @Override
    public ConnectionScreen makeConnectionScreen() {
        return new GuiConnectionScreen();
    }

    @Override
    public EndScreen makeEndScreen() {
        return new GuiEndScreen();
    }

    public static void waitFor(Object o){
        Thread t = new Thread(() -> {
            while(o == null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
