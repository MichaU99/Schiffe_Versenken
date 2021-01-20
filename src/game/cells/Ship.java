package game.cells;

import game.Position;
import game.cells.Cell;

import java.util.ArrayList;

/**
 * Stellt ein Schiff auf dem Spielfeld dar. Ein Objekt von {@link Ship} kann dabei an verschiedenen Positionen im
 * Feld sein. Diese Position stehen in dem Position[] position array.
 * Ein Schiff weiss immer wie oft es getroffen wurde, also seine HP und kann damit immer sagen, ob des zerstoert wurde.
 */
public class Ship extends Cell {

    private Position[] positions;
    private int length;
    private int id;

    private static int idCounter = 0;

    /**
     * gibt die Positionen zurueck, auf denen das Schiff plaziert ist.
     * @return Position array seiner Positionen
     */
    public Position[] getPositions() {
        return positions;
    }

    /**
     * Gibt die aktuelle Laenge des Schiffs, d.h. die Anzahl seiner HP zurueck
     * @return aktuelle laenge als int
     */
    public int getLength() {
        return length;
    }

    public int getId() { return this.id; }

    /**
     * Konstruktur fuer ein Ship.
     * Laenge des Schiffes wird berechnet durch die Groesse des Positionen Arrays
     * @param positions Positionen Array, die in dem Schiff eingetragen werden, also wo das Schiff steht.
     */
    public Ship(Position[] positions){
        super();
        this.positions = positions;
        this.length = positions.length;

        this.id = idCounter;
        idCounter++;
    }

    /**
     * Konstruktur fuer ein Ship.
     * Wird benutzt damit die KiStrong Blockfelder setzen kann
     * @param Ki
     * @param positions
     */
    public Ship(boolean Ki,ArrayList<Position> positions){
        super();
        this.positions = new Position[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            this.positions[i] = positions.get(i);
        }
        this.length = positions.size();
    }
    /**
     * Konstruktur fuer ein Ship.
     * Laenge des Schiffes wird berechnet durch die Laenge des Positionen Arrays
     * @param positions Positionen ArrayList, die in dem Schiff eingetragen werden, also wo das Schiff steht.
     */
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

    /**
     * Wird aufgerufen, wenn auf die Positon geschossen wird. Passt die Laenge des Schiffes an und gibt zurueck,
     * ob das Schiff getroffen oder zerstoert wurde
     * @return 1 wenn Schiff getroffen wurde, 2 wenn zerstoert
     */
    public int shot(){
        this.length--;
        if (this.destroyed())
            return 2;
        else
            return 1;
    }

    /**
     * prueft ob das Schiff zerstoert ist
     * @return {@code true} wenn Laenge des Schiffes 0, sonst {@code false}
     */
    public boolean destroyed(){
        return this.length <= 0;
    }

    @Override
    public String toString() {
        return "s";
    }
}
