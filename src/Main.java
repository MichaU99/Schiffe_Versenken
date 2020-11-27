import enums.KiStrength;
import game.*;
import game.cells.Ship;
import ki.Ki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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

    public static void main(String[] args) throws IOException {
        OnlineHostGame game = new OnlineHostGame(6, 6, 55555, new int[]{5, 4, 3});
        game.waitForConnection();
        game.getField().addShipRandom(new int[]{5, 4, 3});
        game.startGame();

        while (true) {
            game.getEnemyField().printField();
            System.out.println("Enter coordinates x , y");
            String input = reader.readLine();
            game.shoot(new Position(Integer.parseInt(input.split(",")[0]), Integer.parseInt(input.split(",")[1])));
        }
    }
}