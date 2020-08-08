package Werft;
import java.util.Arrays;

public class Schiffsverteilung {
    int i,j,k1,k2,k1_tmp,k2_tmp,lauf,lauf2;
    boolean [][] standartmeer=new boolean[10][10];

    public boolean[][] verteile_schiffe (int[] schiffe){
        Arrays.fill(standartmeer,true);
        return verteile_schiffe(standartmeer,schiffe);
    }

    public boolean[][] verteile_schiffe (boolean[][] meer, int[] schiffe) { //False im Meer bedeutet Schiff
        int pos_x=0, pos_y=0, pos_direction = 0;
        for (i = 0; i <= schiffe.length; i++) { //for-Schleife über die Schiffsanzahl
            for (k1 = 0; k1 <= meer[0].length;k1++) {   //for Schleife über die beiden Dimensionen
                for (k2 = 0; k2 <= meer[1].length;k2++) {
                    for (pos_direction = 0; pos_direction <= 3; pos_direction++) { //for Schleife über alle Richtungen
                        if (meer[k1][k2] == false) continue;
                        k1_tmp=k1;
                        k2_tmp=k2;
                        lauf2=0;
                        for (lauf=1;lauf<=schiffe[i];lauf++){
                            if (pos_direction == 0){
                                if(meer[k1_tmp++][k2_tmp] == false) break;
                            }
                            else if (pos_direction == 1) {
                                if (meer[k1_tmp][k2_tmp--] = false) break;
                            }
                            else if (pos_direction == 2) {
                                if (meer[k1_tmp--][k2_tmp] = false) break;
                            }
                            else if (pos_direction == 3){
                                if(meer[k1_tmp][k2_tmp++] = false) break;
                            }
                            lauf2++;
                        }
                        if (lauf2==schiffe[i]-1){
                            pos_x=k1;
                            pos_y=k2;
                        }
                    }
            }
        }
        for (j = 0; j <= schiffe[j]; j++) { //pos_direction: 0=right, 1=down, 2=left, 3=up
            if (pos_direction == 0) meer[pos_x++][pos_y] = false;
            if (pos_direction == 1) meer[pos_x][pos_y--] = false;
            if (pos_direction == 2) meer[pos_x--][pos_y] = false;
            if (pos_direction == 3) meer[pos_x][pos_y++] = false;
        }
    }

        return meer;
    }


}
