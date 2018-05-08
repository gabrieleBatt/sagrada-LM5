package it.polimi.ingsw.model.table.dashboard;

import it.polimi.ingsw.model.exception.DeckTooSmallException;
import it.polimi.ingsw.model.table.Deck;

import java.util.ArrayList;
import java.util.List;

public class DashBoardDeck implements Deck {

    private static DashBoardDeck dashBoardDeck = new DashBoardDeck();
    private List<DashBoardCard> dashBoardCards;


    //TODO

    private DashBoardDeck(){

    }

    @Override
    public List<DashBoard> draw(int num) throws DeckTooSmallException {
        return new ArrayList<>();
    }

    public static DashBoardDeck getDashBoardDeck() {
        return dashBoardDeck;
    }



    class DashBoardCard{
        private DashBoard d1;
        private DashBoard d2;

        public DashBoardCard(DashBoard d1, DashBoard d2){
            this.d1 = d1;
            this.d2 = d2;
        }

        public DashBoard getD1() {
            return d1;
        }

        public DashBoard getD2() {
            return d2;
        }
    }
}
