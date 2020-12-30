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
        LocalGame localGame = new LocalGame(10, 10, KiStrength.INTERMEDIATE);
        //localGame.getField().addShipRandom(new int[]{5, 4, 4, 3, 3, 3, 2, 2, 2, 2});
        localGame.getField().addShip(new Ship(new Position[]{new Position(0, 0), new Position(1, 0), new Position(2, 0)}));
        localGame.startGame();

        FileWriter fileWriter = new FileWriter("shipdata.csv");

        String csvHeader = "y,x,value";
        fileWriter.write(csvHeader + "\n");
//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < 10; j++) {
//                csvHeader = csvHeader.concat(i + "|" + j + ",");
//            }
//        }
//        System.out.println(csvHeader.substring(0, csvHeader.lastIndexOf(",")));
//        fileWriter.write(csvHeader.substring(0, csvHeader.lastIndexOf(",")) + "\n");
//        fileWriter.flush();

        for (int x = 0; x < 10000; x++) {
            String data = "";
            Field field = new Field(10, 10);
            field.addShipRandom(new int[]{5, 4, 4, 3, 3, 3, 2, 2, 2, 2});

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (field.getCell(new Position(j, i)) instanceof Ship) {
                        fileWriter.write(i + "," + j + ",1\n");
                        fileWriter.flush();
                    }
                    else {
                        fileWriter.write(i + "," + j + ",0\n");
                        fileWriter.flush();
                    }
                }
            }
        }
        fileWriter.close();
        //localGame.getField().printField();
    }
}