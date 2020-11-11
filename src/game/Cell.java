package game;

import java.io.Serializable;

public class Cell implements Serializable {

    public Cell(){

    }

    public int shot(){
        // returns -1 if shot was missed, otherwise the remaining health of the ship
        return -1;
    }

    @Override
    public String toString() {
        return "0";
    }
}
