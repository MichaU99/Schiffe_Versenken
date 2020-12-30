package game.cells;

import java.io.Serializable;

/**
 * Cell stellt eine Zelle des Spielfelds dar.
 * z.B. Das Spielfeld an der Position A0 ist eine Zelle.
 * Cell ist die Oberklasse aller Unterklassen {@link Block} {@link Ship} {@link Shot}.
 * {@link Cell} selber stellt eine leere Zelle dar. In der realen Welt also Wasser
 *
 * Implementiert {@link Serializable} um einen Object Stream erzeugen zu koennen
 */
public class Cell implements Serializable {

    /**
     * Standardkonstruktor fuer eine Zelle
     */
    public Cell(){

    }

    /**
     * Wird aufgerufen, wenn ein Spieler auf eine Zelle feuert, wird von {@link Ship} überschrieben, um passend
     * zurückzugeben, ob ein Schiff getroffen 1 oder versenkt 2 wurde. Sonst 0.
     * @return 0, leere Zelle wurde getroffen
     */
    public int shot(){
        // returns 0 if shot was missed, 1 if hit, 2 if destroyed
        return 0;
    }

    @Override
    public String toString() {
        return "0";
    }
}
