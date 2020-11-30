package game.cells;

public class Block extends Cell {
    // Block ist dafür da, Zellen zu markieren, welche gesperrt sind, d.h. wo keine Schiffe plaziert werden dürfen
    // Das ist für die addShip Methoden wichtig, um diese Bedingung zu erfüllen
    // Blokiert werden alle Zellen um ein Schiff.

    public Block(){
        super();
    }

    @Override
    public String toString() {
        return "x";
    }
}
