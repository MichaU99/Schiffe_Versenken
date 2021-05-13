package game;

import java.io.Serializable;

/**
 * Positon im Feld bestehen aus x = x-te Spalte und y = y-te Reihe
 *
 * Implementiert {@link Serializable} um einen Object Stream erzeugen zu koennen
 */
public class Position implements Serializable {

    private final int x;
    private final int y;

    /**
     * Getter für x
     * @return int x
     */
    public int getX() {
        return x;
    }

    /**
     * Getter für y
     * @return int y
     */
    public int getY() {
        return y;
    }

    /**
     * Konstruktor für eine Position mit int Parametern
     * @param x x-Wert der Position
     * @param y y-Wert der Position
     */
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Konstruktor für eine Position mit String Parametern
     * Es wird nicht geprüft, ob die Strings wirklich numerisch sind. Muss davor gemacht werden.
     * Ruft {@link Position#Position(int, int)} auf mit {@link Integer#parseInt(String)}
     * @param x x-Wert der Position
     * @param y y-Wert der Position
     *
     * @see Position#Position(int, int)
     */
    public Position(String x, String y) {
        this(Integer.parseInt(x), Integer.parseInt(y));
    }

    /**
     * Konstruktor für Position aus String Array.
     * Ist nützlich wenn man einem Objekt eine id gibt und daraus dann mit {@link String#split(String)} eine Position erstellen will.
     * Ruft {@link Position#Position(String, String)} auf
     * @param pos String array mit pos[0] = x und pos[1] = y
     *
     * @see Position#Position(String, String)
     * @see Position#Position(int, int)
     */
    public Position(String[] pos) {
        this(pos[0], pos[1]);
    }

    /**
     * Prüft ob Objekt gleich ist. Vergleich auf x und y
     * @param obj Zu vergleichendes Objekt
     * @return {@code true} falls x und y gleich sind, sonst {@code false}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position pos2 = ((Position) obj);
            return this.getX() == pos2.getX() && this.getY() == pos2.getY();
        }
        return false;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
