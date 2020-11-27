package game;

import game.cells.Cell;
import game.cells.Ship;

public interface Field {
    // Damit bekommt man das Feld, über dieses kann man ganz normal iterieren (2d Array)
    // Die Typen sind Cell, aber dann natürlich auch alle Klassen, die davon erben, je nachdem was es dann eben ist
    Cell[][] getPlayfield();

    // Die Höhe des Spielfelds ist die erste Dimension
    int getHeight();

    // Die Länge des SPielfelds ist die zweite Dimension
    int getLength();

    // Damit wird das Spielfeld auf eine neue Größe gebracht.
    // Alle Schiffe, die es schon gibt und die innerhalb des neuen Felds liegen werden übernommen.
    // Alle anderen Schiffe werden rausgeworfen.
    void resizeField(int newHeight, int newLength);

    // Fügt ein Schiff hinzu an den Positionen, die in dem Schiff stehen.
    boolean addShip(Ship ship);

    // Nimmt eine Länge und fügt dann ein neues Schiff an einer zufälligen Position im Feld hinzu
    boolean addShipRandom(int length);

    // Nimmt eine Array von Längen und fügt diese als neue Schiffe DER REIHE NACH
    boolean addShipRandom(int[] lengths);

    // Gibt zurück, wie viele Schiffe es im Moment auf dem Feld gibt
    int getShipCount();

    // Gibt die Länge aller Schiffe auf dem Feld absteigend sortiert in einem Array zurück
    int[] getShipLengths();

    // Damit wird auf eine Position des Feldes geschossen. Rückgabe ist dann entsprechend der implemntierung in der
    // jeweiligen Zelle
    int registerShot(Position position);

    // Gibt das Feld auf der Konsole aus
    void printField();

    // Gibt das Feld auf der Konsole aus, verbirgt aber Schiffe und zeigt nur Schüsse
    void printFieldConcealed();
}
