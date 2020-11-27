import game.OnlineClientGame;
import game.Position;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientMain {
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args) throws InterruptedException, IOException {
        OnlineClientGame game = new OnlineClientGame("localhost", 55555);
        game.establishConnection();
        game.getField().addShipRandom(new int[]{5, 4, 3});
        System.out.println("ships added");
        game.startGame();
        System.out.println("game started");

        while (true) {
            game.getEnemyField().printField();
            System.out.println("Enter coordinates x , y");
            String input = reader.readLine();
            game.shoot(new Position(Integer.parseInt(input.split(",")[0]), Integer.parseInt(input.split(",")[1])));
        }
    }
}
