package game.cells;

import java.io.Serializable;

public class Cell implements Serializable {

    public Cell(){

    }

    public int shot(){
        // returns 0 if shot was missed, 1 if hit, 2 if destroyed
        return 0;
    }

    @Override
    public String toString() {
        return "0";
    }
}
