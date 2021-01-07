package game;

import game.cells.Block;
import game.cells.Cell;
import game.cells.Ship;
import game.cells.Shot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Field ist die Grundlage des Schiffeversenkens
 * Im Prinzip implementiert es das physische Spielbrett des Schiffeversenekens
 * Ein Spielfeld ist ein ein 2-dimensionales Array aus {@link Cell}, welches dann mit den
 * vorhandenen Methoden veraendert werden kann.
 * Ein Spielfeld weiss zu jeder Zeit seine hoehe und laenge und bewahrt selber einen korrekten Zustand, das heisst,
 * dass Schiffe korrekt plaziert sind und Schuesse auf das Feld korrekt erfasst werden.
 * Implementiert {@link Serializable} um einen Object Stream erzeugen zu koennen
 *
 * @author Matthias
 */
public class Field implements Serializable {
    private Cell[][] playfield;
    private int height;
    private int length;

    /**
     * Die Methode sollte nicht oft benoeigt werden. Wird nur benoetigt um Positionen neu zu setzen, wenn die Schiffe
     * auf dem Feld nicht bekannt sind. Dies ist nur der Fall im {@link OnlineGame}.
     * Wenn ueberprueft werden muss, ob eine Zelle einen bestimmten Typ hat: benutze {@link Field#getCell(Position)}
     *
     * @return <code>Cell[][]</code> Also die ganze Datenstruktur
     */
    public Cell[][] getPlayfield() {
        return playfield;
    }

    /**
     * Gibt die Hoehe des Feldes zurueck, in anderen Worten die Anzahl der Reihen des Spielfeldes
     * @return <code>int</code> Hoehe des Feldes
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gibt die Laenge des Feldes zurueck, in anderen Worten die Anzahl der Spalten des Spielfeldes
     * @return <code>int</code> Laenge des Feldes
     */
    public int getLength() {
        return length;
    }

    /**
     * Konstruktor fuer {@link Field}
     * Wertebereich fuer height und length muss zwischen 5 und 30 liegen nach vorgaben.
     * Setzt alle Cells des Field auf {@link Cell}. Also auf frei.
     *
     * @param height Anzahl der Reihen des Feldes
     * @param length Anzahl der Spalten des Feldes
     */
    public Field(int height, int length) {
        assert 5 <= height && height <= 30 : "height not in range 5-30";
        assert 5 <= length && length <= 30 : "length not in range 5-30";

        this.playfield = new Cell[height][length];
        this.height = height;
        this.length = length;
        this.resetField();
    }

    /**
     * Setzt alle Cells des {@link Field} auf {@link Cell}, also auf frei zurueck.
     */
    public void resetField() {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.length; j++) {
                this.playfield[i][j] = new Cell();
            }
        }
    }

    /**
     * Gibt eine Liste der Schiffe zurueck, die sich innerhalb der bounds der Uebergabeparameter befinden
     * @param newHeight Hoehe des Feldes, auf der Schiffe extrahiert werden sollen
     * @param newLength Laenge des Feldes, auf der Schiffe extrahiert werden sollen
     * @return <code>{@link ArrayList}</code> mit Typ Ship. Darin enthalten sind alle Schiffe, die auf die neue Groesse des Feldes passen
     */
    public ArrayList<Ship> extractShips(int newHeight, int newLength) {
        int height;
        int length;
        ArrayList<Ship> ships = new ArrayList<>();
        height = Math.min(this.height, newHeight);
        length = Math.min(this.length, newLength);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                if (this.playfield[i][j] instanceof Ship) {
                    Ship ship = (Ship) this.playfield[i][j];
                    boolean inBounds = true;
                    for (Position position: ship.getPositions()) {
                        if (position.getX() >= newLength || position.getY() >= newHeight) {
                            inBounds = false;
                            break;
                        }
                    }
                    if (inBounds && !ships.contains(ship))
                        ships.add(ship);
                }
            }
        }
        return ships;
    }

    /**
     * Vergroessert bzw. verkleinert das Feld auf die neue Hoehe und Laenge.
     * Wenn die neue Laenge und Hoehe gleich der alten sind, bleibt das Feld unveraendert.
     * Alle Schiffe die innerhalb der neuen bounds sind, werden uebernommen, ansonst werden sie entfernt
     * @param newHeight neue Hoehe des Feldes
     * @param newLength neue Laenge des Feldes
     */
    public void resizeField(int newHeight, int newLength) {
        if (newHeight == this.height && newLength == this.length)
            return;

        Cell[][] tempField = new Cell[newHeight][newLength];
        ArrayList<Ship> ships = this.extractShips(newHeight, newLength);

        this.playfield = tempField;
        this.height = newHeight;
        this.length = newLength;
        this.resetField();

        for (Ship ship: ships) {
            this.addShip(ship);
        }
    }

    /**
     * Interne Methode um Feldes um {@link Ship} zu blockieren, damit dort keine Schiffe mehr plaziert werden koennen.
     * Blockierte Felder sind durch {@link Block} dargestellt.
     * @param ship Das Schiff, um das die Felder blockiert werden sollen
     */
    private void blockFields(Ship ship) {
        for(Position position: ship.getPositions()) {
            if (position.getX() > 0 && this.playfield[position.getY()][position.getX() - 1].getClass() == Cell.class)
                this.playfield[position.getY()][position.getX() - 1] = new Block();
            if (position.getX() < this.length - 1 &&
                    this.playfield[position.getY()][position.getX() + 1].getClass() == Cell.class)
                this.playfield[position.getY()][position.getX() + 1] = new Block();
            if (position.getY() > 0 &&
                    this.playfield[position.getY() - 1][position.getX()].getClass() == Cell.class)
                this.playfield[position.getY() - 1][position.getX()] = new Block();
            if (position.getY() < this.playfield.length - 1 &&
                    this.playfield[position.getY() + 1][position.getX()].getClass() == Cell.class)
                this.playfield[position.getY() + 1][position.getX()] = new Block();
            if (position.getX() > 0 && position.getY() > 0 &&
                    this.playfield[position.getY() - 1][position.getX() - 1].getClass() == Cell.class)
                this.playfield[position.getY() - 1][position.getX() - 1] = new Block();
            if (position.getX() < this.length - 1 && position.getY() < this.playfield.length - 1 &&
                    this.playfield[position.getY() + 1][position.getX() + 1].getClass() == Cell.class)
                this.playfield[position.getY() + 1][position.getX() + 1] = new Block();
            if (position.getX() > 0 && position.getY() < this.playfield.length - 1 &&
                    this.playfield[position.getY() + 1][position.getX() - 1].getClass() == Cell.class)
                this.playfield[position.getY() + 1][position.getX() - 1] = new Block();
            if (position.getX() < this.length - 1 && position.getY() > 0 &&
                    this.playfield[position.getY() - 1][position.getX() + 1].getClass() == Cell.class)
                this.playfield[position.getY() - 1][position.getX() + 1] = new Block();
        }
    }

    /**
     * Gibt die Zelle zurueck, die durch die uebergebene {@link Position} spezifiziert ist
     * Nuetzlich um den Typ der Zelle mit instanceof zu vergleichen.
     * @param position Position der Zelle, welche zurueckgegeben werden soll
     * @return <code>{@link Cell}</code> welche angefragt wurde
     */
    public Cell getCell(Position position) {
        try {
            return this.playfield[position.getY()][position.getX()];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }

    }

    /**
     * Fuegt ein Schiff auf dem Feld hinzu, sollte dessen Positionen noch nicht besetzt sein
     * @param ship Schiff, welches plaziert werden soll
     * @return <code>true</code> Falls das Schiff hinzugefuegt werden konnte <br>
     *     <code>false</code> Falls das Schiff nicht hinzugefuegt werden konnte
     */
    public boolean addShip(Ship ship){
        // check if any position of ship is already occupied
        for(Position position: ship.getPositions()){
            if (this.playfield[position.getY()][position.getX()].getClass() == Shot.class)
                continue;
            if (this.playfield[position.getY()][position.getX()].getClass() != Cell.class)
                return false;
        }

        // place ship on playfield
        for(Position position: ship.getPositions()){
            this.playfield[position.getY()][position.getX()] = ship;
        }

        // block fields arround ship
        this.blockFields(ship);
        return true;
    }

    /**
     * Fuegt ein Schiff des uebergebenen Laenge zufaellig auf das Feld ein. Zufaellig sind Startposition und Richtung, in die
     * das Schiff zeigt.
     * @param length Laenge des neuen Schiffs
     * @return <code>true</code> falls das Schiff hinzugefuegt werden konnte <br> <code>false</code> falls das Schiff nach 10000 Versuchen immer
     * noch keinen Platz gefuden hat
     */
    public boolean addShipRandom(int length){
        Random r = new Random();
        boolean placed = false;
        char [] directions = {'n', 'e', 's', 'w'};
        int loopcount = 0;

        while (!placed){
            if (loopcount > 10000) {
                return false;
            }

            Position startPos = new Position(r.nextInt(this.length), r.nextInt(this.height));
            Position[] positions = new Position[length];
            char direction = directions[r.nextInt(4)];

            switch (direction) {
                case 'n' -> {
                    if (startPos.getY() - length < 0) {
                        continue;
                    }
                    positions[0] = startPos;
                    for (int i = 1; i < length; i++) {
                        positions[i] = new Position(startPos.getX(), startPos.getY() - i);
                    }
                    placed = this.addShip(new Ship(positions));
                }
                case 's' -> {
                    if (startPos.getY() + length > this.height) {
                        continue;
                    }
                    positions[0] = startPos;
                    for (int i = 1; i < length; i++) {
                        positions[i] = new Position(startPos.getX(), startPos.getY() + i);
                    }
                    placed = this.addShip(new Ship(positions));
                }
                case 'w' -> {
                    if (startPos.getX() - length < 0) {
                        continue;
                    }
                    positions[0] = startPos;
                    for (int i = 1; i < length; i++) {
                        positions[i] = new Position(startPos.getX() - i, startPos.getY());
                    }
                    placed = this.addShip(new Ship(positions));
                }
                case 'e' -> {
                    if (startPos.getX() + length > this.length) {
                        continue;
                    }
                    positions[0] = startPos;
                    for (int i = 1; i < length; i++) {
                        positions[i] = new Position(startPos.getX() + i, startPos.getY());
                    }
                    placed = this.addShip(new Ship(positions));
                }
            }
            loopcount++;
        }
        return true;
    }

    /**
     * Fuegt das array der uebergebenen Laengen der Reihe nach dem Feld hinzu. Sollte dies nicht funktionieren,
     * wird das Feld zurueckgesetzt und es nochmal probiert
     * @param lengths int array, der Schiffslaengen, welche hinzugefuegt werden sollen
     * @return <code>true</code> wenn alle Schiffe hinzugefuert werden konnten
     *
     * @see Field#addShipRandom(int)
     */
    public boolean addShipRandom(int[] lengths) {
        int failCounter = 0;
        for (int i = 0; i < lengths.length; i++) {
            if (!addShipRandom(lengths[i])) {
                this.resetField();
                i = -1;
                failCounter++;
                if (failCounter >= 10)
                    return false;
            }
        }
        assert this.getShipCount() == lengths.length : "Nicht alle Schiffe plaziert";
        return true;
    }

    /**
     * genau wie {@link Field#addShipRandom(int[])} nur das ein Integer[] erwartet wird
     * @param lengths Integer array, der Schiffslaengen, welche hinzugefuegt werden sollen
     * @return <code>true</code> wenn alle Schiffe hinzugefuert werden konnten
     *
     * @see Field#addShipRandom(int[])
     */
    public boolean addShipRandom(Integer[] lengths) {
        int[] newLengths = new int[lengths.length];
        for (int i = 0; i < lengths.length; i++) {
            newLengths[i] = lengths[i];
        }
        return this.addShipRandom(newLengths);
    }

    /**
     * genau wie {@link Field#addShipRandom(int[])} nur das eine Integer ArrayList erwartet wird
     * @param lengths Integer Liste, der Schiffslaengen, welche hinzugefuegt werden sollen
     * @return <code>true</code> wenn alle Schiffe hinzugefuert werden konnten
     *
     * @see Field#addShipRandom(int[])
     */
    public boolean addShipRandom(ArrayList<Integer> lengths) {
        int[] newLengths = new int[lengths.size()];
        for (int i = 0; i < lengths.size(); i++) {
            newLengths[i] = lengths.get(i);
        }
        return this.addShipRandom(newLengths);
    }

    /**
     * Fuegt das array der uebergebenen Laengen der Reihe nach dem Feld hinzu. Sollte dies nicht funktionieren,
     * wird das Feld zurueckgesetzt und es nochmal probiert. Allerdings werden bereits plazierte Schiffe zwischengespeichert
     * und dann als erstes wieder plaziert, bevor die neuen Schiffe hinzugefuegt werden. So bleiben bereits plazierte Schiffe
     * erhalten
     * @param lengths int array, der Schiffslaengen, welche hinzugefuegt werden sollen
     * @return <code>true</code> wenn alle Schiffe hinzugefuert werden konnten
     *
     * @see Field#addShipRandom(int[])
     */
    public boolean addShipRandomKeepShips(int[] lengths) {
        int failCounter = 0;
        ArrayList<Ship> curShips = this.extractShips(this.height, this.length);
        for (int i = 0; i < lengths.length; i++) {
            if (!this.addShipRandom(lengths[i])) {
                //failed to place Ship
                this.resetField();
                for (Ship ship: curShips)
                    this.addShip(ship);
                i = -1;
                failCounter++;
                if (failCounter >= 10)
                    return false;
            }
        }
        return true;
    }

    /**
     * genau wie {@link Field#addShipRandomKeepShips(int[])} nur das eine Integer ArrayList erwartet wird
     * @param lengths Integer Liste, der Schiffslaengen, welche hinzugefuegt werden sollen
     * @return <code>true</code> wenn alle Schiffe hinzugefuert werden konnten
     *
     * @see Field#addShipRandomKeepShips(int[])
     */
    public boolean addShipRandomKeepShips(ArrayList<Integer> lengths) {
        int[] newLengths = new int[lengths.size()];
        for (int i = 0; i < lengths.size(); i++) {
            newLengths[i] = lengths.get(i);
        }
        return this.addShipRandomKeepShips(newLengths);
    }

    /**
     * Prueft ob die uebergebene Position ein Schiff ist. sollte dies der Fall sein, entfernt sie alle Position des Schiffes
     * vom Feld. Danach gibt sie die Cells um das Schiff ebenfalls wieder frei.
     * @param position Position, auf der gegebenenfalls das zu entfernente Schiff ist
     * @return <code>true</code> falls ein Schiff entfernt wurde <br> <code>false</code> falls kein Schiff entfernt wurde
     */
    public boolean removeShip(Position position) {
        if (this.playfield[position.getY()][position.getX()].getClass() != Ship.class)
            return false;

        Ship s = ((Ship) this.playfield[position.getY()][position.getX()]);
        for (Position pos: s.getPositions()) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    try {
                        this.playfield[pos.getY() + i][pos.getX() + j] = new Cell();
                    }
                    catch (ArrayIndexOutOfBoundsException ignored) {
                    }
                }
            }
        }
        for (Ship ship: this.extractShips(this.getHeight(), this.getLength())) {
            this.blockFields(ship);
        }
        return true;
    }

    /**
     * Setzt das Spielfeld zurück
     * @see Field#resetField()
     */
    public void removeAllShips() {
        this.resetField();
    }

    /**
     * Anzahl der Schiff, die sich auf dem Feld befinden
     * Bsp: 1x5er Schiff, 3x3er Schiff, 2x2er Schiff = 6
     * @return Die Anzahl der Schiffe, die sich momentan auf dem Feld befinden
     */
    public int getShipCount() {
        // returns how many ships are on the current field
        return this.extractShips(this.height, this.length).size();
    }

    /**
     * Gibt zurueck, wie viele 5er, 4er, 3er, 2er Schiffe sich auf dem Feld befinden
     * Bsp: 1x5er Schiff, 2x4er Schiff, 3x3er Schiff, 2x2er Schiff = [1, 2, 3, 2]
     * @return
     */
    public Integer[] getShipCountByClass() {
        Integer[] classCount = new Integer[] {0, 0, 0, 0};
        for(Integer i: this.getShipLengths()) {
            if (i == 5) {
                classCount[0]++;
            }
            else if (i == 4) {
                classCount[1]++;
            }
            else if (i == 3) {
                classCount[2]++;
            }
            else if (i == 2) {
                classCount[3]++;
            }
        }
        return classCount;
    }

    /**
     * Gibt die Laengen aller Schiffe die sich momentan auf dem Feld befinden in einem Array zurueck in absteigend sortierter
     * Reihenfolge
     * Bsp: 1x5er Schiff, 2x4er Schiff, 3x3er Schiff, 2x2er Schiff = [5, 4, 4 , 3, 3, 3, 2, 2]
     * @return Absteigend sortiertes Integer Array mit den Laengen aller Schiffe
     */
    public Integer[] getShipLengths() {
        // returns the lengths of every ship on the current field as array
        ArrayList<Ship> extractShips = this.extractShips(this.height, this.length);
        Integer[] shipLengths = new Integer[extractShips.size()];
        for (int i = 0; i < extractShips.size(); i++) {
            shipLengths[i] = extractShips.get(i).getLength();
        }

        Arrays.sort(shipLengths, Collections.reverseOrder());

        return shipLengths;
    }

    /**
     * Implementierung eines Schusses. Dabei wird die jeweilige {@link Cell#shot()} Methode aufgerufen, und der Rueckgabewert
     * dieser Cell zurueckgegeben. Wenn ein Schiff getroffen wurde, wird Shot.wasShip auf {@code true} gesetzt
     * @param position Positon, auf die geschossen werden soll
     * @return 0 falls Cell, 1 falls Ship, 2 falls Ship destroyed
     */
    public int registerShot(Position position){
        Cell cell = this.playfield[position.getY()][position.getX()];
        this.playfield[position.getY()][position.getX()] = new Shot();

        if (cell instanceof Ship) {
            Shot s = (Shot) this.playfield[position.getY()][position.getX()];
            s.setWasShip(true);
        }

        return cell.shot();
    }

    /**
     * Ausgabe des Feldes auf der Konsole
     */
    public void printField(){
        for (int i = 65; i < this.length + 65; i++) {
            System.out.print("\t" + (char)i);
        }
        System.out.println();
        for (int i = 0; i < this.height; i++) {
            System.out.print(i + 1);
            for (Cell cell: this.playfield[i]){
                System.out.print("\t" + cell.toString());
            }
            System.out.println();
        }
    }

    /**
     * Ausgabe des Feldes auf der Konsole, verbirgt allerdings Schiffe
     */
    public void printFieldConcealed() {
        for (int i = 65; i < this.length + 65; i++) {
            System.out.print("\t" + (char)i);
        }
        System.out.println();
        for (int i = 0; i < this.height; i++) {
            System.out.print(i + 1);
            for (Cell cell: this.playfield[i]){
                if (cell instanceof Shot) {
                    System.out.print("\t" + cell.toString());
                }
                else {
                    System.out.print("\t 0");
                }
            }
            System.out.println();
        }
    }
}
