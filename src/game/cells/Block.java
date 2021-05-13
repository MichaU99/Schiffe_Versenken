package game.cells;

/**
 * {@link Block} ist dazu da, einzelne Zellen zu markieren, damit die addShip Methode dort keine Schiffe plaziert.
 * Wird sonst nicht benötigt und sollte nicht benutzt werden
 */
public class Block extends Cell {

    /**
     * Standardkonstruktur, ruft Konstruktor von {@link Cell} auf
     */
    public Block(){
        super();
    }

    @Override
    public String toString() {
        return "x";
    }
}
