package game;

import java.io.*;

public abstract class Game implements Serializable {
    protected Field field;
    protected Field enemyField;

    // Getter für das Spielfeld des Spielers
    public Field getField() {
        return field;
    }

    // Getter für das Spielfeld des Gegners
    public Field getEnemyField() { return enemyField; }

    public Game() {

    }

    public Game(int playFieldHeight, int playFieldLength){
        this.field = new Field(playFieldHeight, playFieldLength);
        this.enemyField = new Field(playFieldHeight, playFieldLength);
    }

    // Die Methode muss aufgerufen werden, bevor das Spiel gestartet wird
    // Funktion hängt von dem jeweiligen Typ des Spiels ab -> Ist dann genauer beschrieben
    public abstract boolean startGame();

    // Damit wird geschossen auf die übergebene Position
    // Funktion hängt auch wieder davon ab, welchen Typ das Spiel hat
    public abstract void shoot(Position position);

    // Damit wird das Spiel geladen, deswegen auch SerialInterface implementieren.
    // Wird in jeder Klasse überschrieben, damit immer die spezifischen Variablen geladen werden.
    public abstract void loadGame(String id) throws IOException, ClassNotFoundException;

    // Wie loadGame
    public void saveGame(String id) throws IOException {
        FileOutputStream fout = new FileOutputStream(id + ".txt");
        ObjectOutputStream out = new ObjectOutputStream(fout);
        out.writeObject(this);
        out.flush();
        out.close();
    }

}
