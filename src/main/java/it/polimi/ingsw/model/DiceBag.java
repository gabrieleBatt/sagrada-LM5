package it.polimi.ingsw.model;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
// red = 1, green = 2, yellow = 3; cyan = 4; magenta = 5;


public class DiceBag {

    private List<Die> bag;


    public DiceBag(){

       bag = new ArrayList<>();
       for(int i=0; i<18; i++){
           bag.add(new Die(DieColor.RED, i));
           bag.add(new Die(DieColor.GREEN, 18 + i));
           bag.add(new Die(DieColor.YELLOW, 36+i));
           bag.add(new Die(DieColor.MAGENTA, 54+i));
           bag.add(new Die(DieColor.CYAN, 72+i));
       }

    }

    public List<Die> drawDice(int num){
        List<Die> ret = new ArrayList<>();
        for(int i=0; i < num ; i++){
            ret.add(drawDie());
        }
        return ret;
    }

    private Die drawDie(){
        int randomNum = ThreadLocalRandom.current().nextInt(0, bag.size() );
        Die ret = bag.get(randomNum);
        bag.remove(randomNum);
        return ret;
    }

    public void placeDie (Die die){
        bag.add(die);
    }


}
