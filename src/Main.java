public class Main {
    public static void main(String[] args) {
        Field field = new Field(10, 10);
        Position[] posarr1 = {new Position(5, 5), new Position(5, 6)};

//        field.addShip(new Ship(posarr1));
//        field.addShip(new Ship(new Position[] {new Position(0, 0), new Position(0, 1), new Position(0, 2)}));

        int[] lengths = {5, 4, 4, 3, 3, 3, 2, 2, 2, 2};
        field.addShipRandom(lengths);
//        System.out.println(field.registerShot(new Position(5, 6)));
//        System.out.println(field.registerShot(new Position(5, 5)));

        field.printField();
    }
}