package network;

import enums.ProtComs;
import game.Position;
import javafx.scene.control.ProgressIndicator;

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
        int lend = id.lastIndexOf(".");
        if (lend == -1)
            return "save " + id;
        else
            return "save " + id.substring(0, lend);
    }

    public static String formatLoad(String id) {
        int lend = id.lastIndexOf(".");
        if (lend == -1)
            return "load " + id;
        else
            return "load " + id.substring(0, lend);
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
                String id = inputSplit[1];
                int lend = id.lastIndexOf(".");
                if (lend == -1)
                    return new Object[]{ProtComs.SAVE, inputSplit[1]};
                else
                    return new Object[]{ProtComs.SAVE, id.substring(0, lend)};

            case "load":
                String id_ = inputSplit[1];
                int lend_ = id_.lastIndexOf(".");
                if (lend_ == -1)
                    return new Object[]{ProtComs.LOAD, inputSplit[1]};
                else
                    return new Object[]{ProtComs.LOAD, id_.substring(0, lend_)};

            case "next":
                return new Object[]{ProtComs.NEXT};

            default:
                return new Object[]{ProtComs.ERROR};
        }
    }
}
