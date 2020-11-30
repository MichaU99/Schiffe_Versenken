package network;

import enums.ProtComs;
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

    public static String formatSave(String id) {
        return "save " + id;
    }

    public static String formatLoad(String id) {
        return "load " + id;
    }

    public static Object[] processInput(String input){
        String[] inputSplit = input.split(" ");
        switch (inputSplit[0]) {
            case "size":
                return new Object[]{ProtComs.SIZE, Integer.parseInt(inputSplit[1]), Integer.parseInt(inputSplit[2])};

            case "ships":
                int[] shipLengths = new int[inputSplit.length - 1];
                for (int i = 1; i < inputSplit.length; i++) {
                    shipLengths[i - 1] = Integer.parseInt(inputSplit[i]);
                }
                return new Object[]{ProtComs.SHIPS, shipLengths};

            case "shot":
                return new Object[]{ProtComs.SHOT, new Position(Integer.parseInt(inputSplit[1]), Integer.parseInt(inputSplit[2]))};

            case "answer":
                return new Object[]{ProtComs.ANSWER, Integer.parseInt(inputSplit[1])};

            case "save":
                return new Object[]{ProtComs.SAVE, inputSplit[1]};

            case "load":
                return new Object[]{ProtComs.LOAD, inputSplit[1]};

            default:
                return new Object[]{ProtComs.ERROR};
        }
    }
}
