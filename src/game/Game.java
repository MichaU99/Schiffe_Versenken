package game;

import java.io.IOException;

public interface Game {
    // Getter für das Spielfeld des Spielers
    Field getField();

    // Getter für das Spielfeld des Gegners
    Field getEnemyField();

    // Die Methode muss aufgerufen werden, bevor das Spiel gestartet wird
    // Funktion hängt von dem jeweiligen Typ des Spiels ab -> Ist dann genauer beschrieben
    boolean startGame();

    // Damit wird geschossen auf die übergebene Position
    // Funktion hängt auch wieder davon ab, welchen Typ das Spiel hat
    void shoot(Position pos);

    // Damit wird das Spiel geladen, deswegen auch SerialInterface implementieren.
    // Wird in jeder Klasse überschrieben, damit immer die spezifischen Variablen geladen werden.
    void loadGame(String id) throws IOException, ClassNotFoundException;

    // Wie loadGame
    void saveGame(String id) throws IOException;
}
