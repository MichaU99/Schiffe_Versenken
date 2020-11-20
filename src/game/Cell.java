package game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

public class Cell extends Rectangle implements Serializable {
    public int x,y;
    public Cell(int x, int y){ // noch field dazu?
        super(30,30);
        this.x = x;
        this.y = y;
        setFill(Color.LIGHTGRAY);
        setStroke(Color.BLACK);

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
