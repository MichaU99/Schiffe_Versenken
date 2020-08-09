package Werft;

public class Hilfsmethoden {
    int k1, k2;

    public boolean[][] fill(boolean[][] array, boolean wert) {
        for (k1 = 0; k1 < array[0].length; k1++) {   //for Schleife über die beiden Dimensionen
            for (k2 = 0; k2 < array[1].length; k2++) {
                array[k1][k2]=wert;
            }
        }
        return array;
    }
}