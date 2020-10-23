package ki;

import enums.KiStrength;
import game.Field;
import game.Position;

import java.util.Arrays;
import java.util.Random;

public class Ki {
    private Field enemyField;
    private Field kiField;
    private int[] ships;
    private int [][] shotmap;  // 0 = not shot yet, 1 = shot
    private int kiLevel;

    public int randomCount = 0;

    public Ki(Field enemyField, Field kiField, int[] shiplengths, KiStrength kiStrength){
        this.enemyField = enemyField;
        this.kiField = kiField;
        this.kiField.addShipRandom(shiplengths);
        this.ships = shiplengths.clone();
        this.shotmap = new int[enemyField.getHeight()][enemyField.getLength()];
        this.initShotMap();

        switch (kiStrength) {
            case BEGINNER -> this.kiLevel = 0;
            case INTERMEDIATE -> this.kiLevel = 1;
            case STRONG -> this.kiLevel = 2;
            case HELL -> this.kiLevel = 3;
        }
    }

    private void initShotMap(){
        for (int[] ints : shotmap) {
            Arrays.fill(ints, 0);
        }
    }

    public void shootRandom(){
        Random random = new Random();
        int height;
        int length;

        do {
            height = random.nextInt(enemyField.getHeight());
            length = random.nextInt(enemyField.getLength());
            randomCount++;
        } while (this.shotmap[height][length] != 0);
        shotmap[height][length] = 1;

        int hit = enemyField.registerShot(new Position(length, height));
        // System.out.println(hit);
    }

    public void shootRows(){

    }

    public void printShotMap(){
        for (int [] ints: this.shotmap){
            for(int i: ints){
                System.out.print(i + "\t");
            }
            System.out.println();
        }
    }
}
