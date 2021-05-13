import enums.KiStrength;
import game.*;
import game.cells.Ship;
import ki.Ki;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    int[] stdLengths = {5, 4, 4, 3, 3, 3, 2, 2, 2, 2};

    public static int mainMenu() {
        System.out.println("Pick your poison");
        System.out.println("1. Game vs Ki");
        System.out.println("2. Game as Host");
        System.out.println("3. Game as Client");
        System.out.println("4. Options");
        System.out.println("5. Quit");
        try {
            return Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        LocalGame localGame = new LocalGame(5, 5, KiStrength.INTERMEDIATE);
        //localGame.getField().addShipRandom(new int[]{5, 4, 4, 3, 3, 3, 2, 2, 2, 2});
//        localGame.getField().addShip(new Ship(new Position[]{new Position(0, 0), new Position(1, 0), new Position(2, 0)}));
//        localGame.startGame();

        Position[] ship1 = new Position[]{new Position(0, 1), new Position(1, 1), new Position(2,1), new Position(3,1), new Position(4,1)};
        Position[] ship2 = new Position[]{new Position(0, 3), new Position(1, 3), new Position(2,3), new Position(3,3), new Position(4,3)};

        localGame.getField().addShip(new Ship(ship1));
        localGame.getField().addShip(new Ship(ship2));

        System.out.println(localGame.getField().addShipRandom(new int[]{5, 5, 5, 5}));

        localGame.getField().printField();

    }
}