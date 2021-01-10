package game;

import java.io.*;

/**
 * {@link Game} ist eine abstrakte Implementierung eines Spiels, weil für das konkrete Spiel hier noch nicht alle
 * Parameter bekannt sind bzw. diese sich unterscheiden zwischen den verschiedenen Spielmodi
 * Es implementiert einige abstrakte Methoden, die dann aufgerufen werden können, ohne unbedingt auf den
 * endgültigen Typ des Spiels casten zu müssen
 *
 * Game ist {@link Serializable} um es speichern zu können (Spielstand speichern)
 */
public abstract class Game implements Serializable {
    protected Field field;
    protected Field enemyField;
    protected boolean myTurn = true;
    public static boolean kiPlays=false;

    /**
     * Gibt das Spielfeld des Spielers zurück. bzw. im KiVKi game das Feld der ersten KI
     * @return {@link Field} des Spielers
     */
    public Field getField() {
        return field;
    }


    /**
     * Gibt das Spielfeld des Gegners zurück. bzw. im KiVKi game das Feld der zweiten KI
     * @return {@link Field} des Gegners
     */
    public Field getEnemyField() { return enemyField; }

    /**
     * Gibt zurück wer gerade seinen nächsten Zug machen darf
     * @return {@code true} falls Spieler 1 an der Reihe ist, sonst {@code false}
     */
    public boolean isMyTurn() { //volatile
        return myTurn;
    }

    /**
     * Standardkonstruktor für Game
     */
    public Game() {

    }

    /**
     * Konstruktor für game, welcher zwei Felder erzeugt mit vorgegebener Höhe und Länge
     * @param playFieldHeight Höhe bzw. Anzahl Reihen der Felder
     * @param playFieldLength Länge bzw. Anzahl Splaten der Felder
     */
    public Game(int playFieldHeight, int playFieldLength){
        this.field = new Field(playFieldHeight, playFieldLength);
        this.enemyField = new Field(playFieldHeight, playFieldLength);
    }

    /**
     * Muss aufgerufen werden, bevor das Spiel gestartet wird. Implementation abhängig von abgeleiteter Klasse
     * @return {@code true} wenn das Spiel gestartet werden kann, sonst {@code false}
     */
    public abstract boolean startGame();

    /**
     * Schuss auf die übergebene Position des gegnerischen Feldes. Implementation abhängig von abgeleiteter Klasse
     * @param position auf die zu schießende {@link Position}
     * @return 0 falls nicht getroffen, 1 falls getroffen, 2 falls versenkt
     */
    public abstract int shoot(Position position);

    /**
     * Gibt zurück ob das Spiel beendet ist
     * @return 1 falls Gegner gewonnen hat, 0 falls Spieler gewonnen hat, -1 falls Spiel noch nicht beendet ist
     */
    public int whoWon() {
        if (this.field.getShipCount() == 0)
            return 1;
        if (this.enemyField.getShipCount() == 0)
            return 0;
        return -1;
    }

    public boolean didYouLose() {
        return this.field.getShipCount() == 0;
    }

    /**
     * Dumpt das komplette Spiel Objekt an den übergebenen Ort
     * @param id String Pfad zu der Datei, bzw. Speicherort des Objekts
     * @throws IOException falls Datei nicht geschrieben werden kann
     */
    public void saveGame(String id) throws IOException {
        FileOutputStream fout = new FileOutputStream(id);
        ObjectOutputStream out = new ObjectOutputStream(fout);
        out.writeObject(this);
        out.flush();
        out.close();
        fout.close();
    }
}
