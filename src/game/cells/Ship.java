package game.cells;

import game.Position;

import java.util.ArrayList;

public class Ship extends Cell {
    // Ship ist das Schiff in Schiffeversenken.
    // Jedes Schiff hat alle Positionen, die zu diesem Schiff gehört
    // Damit weiß jede Position dieses Schiffes wie viele Leben usw. es noch übrig hat.
    // Die ID wird nur für interne Methoden des Felds benutzt.
    // Wenn die Länge 0 ist, wurden alle Positionen des Schiffs getroffen -> es ist zerstört

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

    public int getId() { return this.id; }

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
