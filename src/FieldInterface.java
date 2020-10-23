public interface FieldInterface {

    void resetField();
    boolean addShip(Ship ship);
    boolean addShipRandom(int length);
    boolean addShipRandom(int[] lengths);
    int registerShot(Position position);
    void printField();

}
