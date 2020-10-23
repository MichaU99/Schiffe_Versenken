package network;

import game.Field;
import game.Game;
import game.Position;

public class BattleshipProtocol {

    public BattleshipProtocol(){

    }

    public static String formatSize(int rows, int cols) {
        return "size " + rows + " " + cols;
    }

    public static String formatShips(int[] lengths) {
        StringBuilder temp = new StringBuilder("ships");
        for (int length: lengths) {
            temp.append(" ").append(length);
        }
        return temp.toString();
    }

    public static String formatShot(int row, int col) {
        return "shot " + row + " " + col;
    }

    public static String formatAnswer(int answer) {
        return "answer " + answer;
    }

    public static String formatSave(int id) {
        return "save " + id;
    }

    public static String formatLoad(int id) {
        return "load " + id;
    }

    public static void processInput(String input, Game game){
        String[] inputSplitted = input.split(" ");
        switch (inputSplitted[0]) {
            case "size":
                game.setField(new Field(Integer.parseInt(inputSplitted[2]), Integer.parseInt(inputSplitted[1])));
                break;

            case "ships":
                int[] shipLengths = new int[inputSplitted.length - 1];
                for (int i = 1; i < inputSplitted.length; i++) {
                    shipLengths[i - 1] = Integer.parseInt(inputSplitted[i]);
                }
                game.setShipLengths(shipLengths);
                break;

            case "shot":
                game.getField().registerShot(new Position(Integer.parseInt(inputSplitted[1]), Integer.parseInt(inputSplitted[2])));
                break;

            case "answer":
                //implement function to register hits on enemy Playfield
                break;

            case "save":
                game.saveGame(Integer.parseInt(inputSplitted[1]));
                break;

            case "load":
                game.loadGame(Integer.parseInt(inputSplitted[1]));
                break;

            default:
                break;
        }
    }
}
