package Werft;
import java.util.Arrays;

public class Schiffsverteilung extends Hilfsmethoden{
    int i,j,k1,k2,k1_tmp,k2_tmp,lauf,lauf2;
    boolean [][] standartmeer=new boolean[10][10];

    public boolean[][] verteile_schiffe (int[] schiffe){
        return verteile_schiffe(standartmeer,schiffe);
    }

    public boolean[][] verteile_schiffe (boolean[][] meer, int[] schiffe) { //False im Meer bedeutet Schiff
        meer=fill(meer,true);
        int pos_x=0, pos_y=0, pos_direction = 0;
        for (i = 0; i < schiffe.length; i++) { //for-Schleife über die Schiffsanzahl

            Positionssuche:
            for (k1 = 0; k1 < meer[0].length;k1++) {   //for Schleife über die beiden Dimensionen
                for (k2 = 0; k2 < meer[1].length;k2++) {
                    for (pos_direction = 0; pos_direction <= 3; pos_direction++) { //for Schleife über alle Richtungen
                        if (meer[k1][k2] == false) continue;
                        lauf2=0;
                        for (lauf=0;lauf<=schiffe[i];lauf++){//Überprüft ob die Länge des Schiffs frei ist
                            if (pos_direction == 0){
                                if(k2+lauf>=meer[1].length||meer[k1][k2+lauf] == false||gefaehrlicheNaehe(meer,k1,k2)) break;
                            }
                            else if (pos_direction == 1) {
                                if (k1+lauf>=meer[0].length||meer[k1+lauf][k2] == false||gefaehrlicheNaehe(meer,k1,k2)) break;
                            }
                            else if (pos_direction == 2) {
                                if (k2-lauf<0||meer[k1][k2-lauf] == false||gefaehrlicheNaehe(meer,k1,k2)) break;
                            }
                            else if (pos_direction == 3){
                                if(k1-lauf<0||meer[k1-lauf][k2] == false||gefaehrlicheNaehe(meer,k1,k2)) break;
                            }
                            lauf2++; //Checkt ob es Überschneidungen mit Schiffen gab
                        }
                        if (lauf2==schiffe[i]+1){
                            pos_x=k2;
                            pos_y=k1;
                            break Positionssuche;
                        }
                    }
            }
        }
        for (j = 0;j < schiffe[i]; j++) { //pos_direction: 0=right, 1=down, 2=left, 3=up
            if (pos_direction == 0) meer[pos_y][pos_x++] = false;
            else if (pos_direction == 1) meer[pos_y++][pos_x] = false;
            else if (pos_direction == 2) meer[pos_y][pos_x--] = false;
            else if (pos_direction == 3) meer[pos_y--][pos_x] = false;
        }
    }

        return meer;
    }
    public boolean gefaehrlicheNaehe(boolean[][] meer,int x,int y){

        if(!(k1+1>=meer[0].length) && meer[k1+1][k2]==false) return true;
        if(!(k2-1<0) && meer[k1][k2-1]==false) return true;
        if(!(k1-1<0) && meer[k1-1][k2]==false) return true;
        if(!(k2+1>=meer[1].length) && meer[k1][k2+1]==false) return true;

        return false;
    }


}
