package game.cells;

/**
 * Shot ist dazu da, Zellen zu markieren, auf die ein Spieler gefeuert hat.
 * Kann benutzt werden, um zu prüfen, ob der Nutzer auf die Stelle schießen darf, schon geschossen hat und
 * ob er getroffen hat
 * {@link game.Field} übernimmt das setzten, was die Zelle ist bzw. war, sollte also nicht manuell gemacht werden
 */
public class Shot extends Cell{
    private boolean wasShip = false;

    /**
     * Gibt zurück, ob die Zelle bevor auf sie geschossen wurde ein Schiff war oder nicht
     * @return {@code true} wenn die Zelle ein Schiff war, {@code false} wenn nicht
     */
    public boolean getWasShip() {
        return this.wasShip;
    }

    /**
     * Setter für wasShip.
     * @param wasShip boolean Wert ob die Zelle ein Schiff war
     */
    public void setWasShip(boolean wasShip) {
        this.wasShip = wasShip;
    }

    /**
     * Standardkonstruktor
     * Standard für wasShip = false
     */
    public Shot() {

    }

    /**
     * Überschriebener Konstruktor, kann benutzt werden um nicht manuell wasShip setzten zu müssen
     * Ruft {@link Shot#setWasShip(boolean)} auf
     * @param wasShip boolean Wert ob die Zelle ein Schiff war
     *
     * @see Shot
     * @see Shot#setWasShip(boolean)
     */
    public Shot(boolean wasShip) {
        this.setWasShip(wasShip);
    }

    @Override
    public String toString() {
        if (this.wasShip)
            return "l";
        return "m";
    }
}
