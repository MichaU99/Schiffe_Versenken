package JavaFx;

import enums.KiStrength;

import java.io.Serializable;
import java.util.ArrayList;

public class GameOptions implements Serializable {
    private int fieldSize;
    private KiStrength kiStrength;
    private int carrier;    // length = 5
    private int battleship; // length = 4
    private int cruiser;    // length = 3
    private int destroyer;  // length = 2

    /**
     *Gibt eine ArrayList mit allen Schiffen gemäß ihrer Länge, der Länge absteigend sortiert zurück
    **/
    public ArrayList<Integer> getShipList(){
        ArrayList<Integer> ShipList = new ArrayList<>();
        for (int i = 0; i < this.carrier; i++) {
            ShipList.add(5);
        }
        for (int i = 0; i < this.battleship; i++) {
            ShipList.add(4);
        }
        for (int i = 0; i < this.cruiser; i++) {
            ShipList.add(3);
        }
        for (int i = 0; i < destroyer; i++) {
            ShipList.add(2);
        }

        return ShipList;
    }
    public int getFieldSize() {
        return fieldSize;
    }

    public void setFieldSize(int fieldSize) {
        this.fieldSize = fieldSize;
    }

    public KiStrength getKiStrength() {
        return kiStrength;
    }

    public void setKiStrength(KiStrength kiStrength) {
        this.kiStrength = kiStrength;
    }

    public int getCarrier() {
        return carrier;
    }

    public void setCarrier(int carrier) {
        this.carrier = carrier;
    }

    public int getBattleship() {
        return battleship;
    }

    public void setBattleship(int battleship) {
        this.battleship = battleship;
    }

    public int getCruiser() {
        return cruiser;
    }

    public void setCruiser(int cruiser) {
        this.cruiser = cruiser;
    }

    public int getDestroyer() {
        return destroyer;
    }

    public void setDestroyer(int destroyer) {
        this.destroyer = destroyer;
    }

    public GameOptions() {
        loadDefault();
    }

    public GameOptions(int fieldSize, KiStrength kiStrength, int carrier, int battleship, int cruiser, int destroyer) {
        this.fieldSize = fieldSize;
        this.kiStrength = kiStrength;
        this.carrier = carrier;
        this.battleship = battleship;
        this.cruiser = cruiser;
        this.destroyer = destroyer;
    }

    public void loadDefault() {
        this.fieldSize = 10;
        this.kiStrength = KiStrength.INTERMEDIATE;
        this.carrier = 1;
        this.battleship = 2;
        this.cruiser = 3;
        this.destroyer = 4;
    }
}
