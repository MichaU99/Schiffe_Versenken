package game;

import game.cells.Ship;

public interface FieldInterface {

    boolean addShip(Ship ship);
    boolean addShipRandom(int length);
    boolean addShipRandom(int[] lengths);
    int registerShot(Position position);
    void printField();

}
