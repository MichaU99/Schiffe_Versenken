package game;

import JavaFx.GameOptions;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * {@link Game} ist eine abstrakte Implementierung eines OnlineSpiels, weil für das konkrete Spiel hier noch nicht alle
 * Parameter bekannt sind bzw. diese sich unterscheiden zwischen {@link OnlineHostGame} und {@link OnlineClientGame}.
 * Es implementiert einige abstrakte Methoden, die dann aufgerufen werden können, ohne unbedingt auf den
 * endgültigen Typ des Spiels casten zu müssen
 *
 * Game ist {@link Serializable} um es speichern zu können (Spielstand speichern)
 */
public abstract class OnlineGame extends Game {

    public GameOptions gameOptions;

    /**
     * Standardkonstruktor
     */
    public OnlineGame() {

    }

    /**
     * Ruft den Konstruktor {@link Game#Game(int, int)} auf.
     * @param playFieldHeight Höhe bzw. Anzahl Reihen der Felder
     * @param playFieldLength Länge bzw. Anzahl Splaten der Felder
     */
    public OnlineGame(int playFieldHeight, int playFieldLength) {
        super(playFieldHeight, playFieldLength);
    }

    /**
     * Beendet die Verbindung und gibt die Ports der Sockets wieder frei. Ist wichtig aufzurufen, wenn man die
     * z.B. als Host vorzeitig aufhört, auf eine Client Verbindung zu warten, da es sonst zu Exceptions kommt, sollte
     * man den gleichen Port nochmals verwenden
     */
    public abstract void freeSocket();

    /**
     * Damit wird auf einen Schuss des Gegners gewartet. Dieser wird dann ins eigene Feld registriert und einene
     * entsprechende Antwort an den Gegner zurückgegeben
     */
    public abstract void enemyShot();

    /**
     * Speichern eines Spiels, aber mit {@link File} als Übergabeparameter. Ist geschickt um mit dem JFileChooser
     * eine Datei zu wählen.
     * @param file Datei, in die gespeichert werden soll
     * @throws IOException Falls nicht geschrieben werden kann
     */
    public abstract void saveGame(File file) throws IOException;
}
