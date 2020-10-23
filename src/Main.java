import enums.MultiplayerMode;
import game.Field;
import game.Game;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
//        Field field = new Field(10, 10);
//        Position[] posarr1 = {new Position(5, 5), new Position(5, 6)};
//
//        field.addShip(new Ship(posarr1));
//        field.addShip(new Ship(new Position[] {new Position(0, 0), new Position(0, 1), new Position(0, 2)}));
//
        int[] lengths = {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2};
//        field.addShipRandom(lengths);
//        System.out.println(field.registerShot(new Position(5, 6)));
//        System.out.println(field.registerShot(new Position(5, 5)));
//
//        field.printField();
//
//        Ki ki = new Ki(field, lengths);
//
//        for (int i = 0; i < field.getHeight() * field.getLength(); i++) {
//            ki.shootRandom();
//        }
//        ki.printShotMap();
//        System.out.println(ki.randomCount);

//        for (int i = 0; i < 300; i++) {
//            System.out.println(i);
//            Field field = new Field(30, 30);
//            field.addShipRandom(lengths);
//            if (i == 299)
//                field.printField();
//        }
        Game game = new Game(10, 10);
        game.saveGame(12345);

//        Game game = new Game(MultiplayerMode.CLIENT, "localhost", 55555);
//        game.client_openConnection();
//        game.client_readLine();
    }
}