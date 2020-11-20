package game.cells;

import game.Position;
import game.cells.Cell;

import java.util.ArrayList;

public class Ship extends Cell {
    private Position[] positions;
    private int length;
    private int id;

    private static int idCounter = 0;

    public Position[] getPositions() {
        return positions;
    }

    public int getLength() {
        return length;
    }


    // hier musste ich ändern zu
    //@Override
    //public final int getId() { return this.id; }

    public Ship(Position[] positions){
        super();
        this.positions = positions;
        this.length = positions.length;

        this.id = idCounter;
        idCounter++;
    }

    public Ship(ArrayList<Position> positions) {
        super();
        this.positions = new Position[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            this.positions[i] = positions.get(i);
        }
        this.length = positions.size();

        this.id = idCounter;
        idCounter++;
    }

    public int shot(){
        this.length--;
        if (this.destroyed())
            return 2;
        else
            return 1;
    }

    public boolean destroyed(){
        return this.length <= 0;
    }

    @Override
    public String toString() {
        return "s";
    }
}
