package game;

import enums.KiStrength;
import ki.Ki;

import java.io.*;

public class LocalGame extends Game {
    private Ki ki;

    /**
     * Standartkonstruktor der Klasse LocalGame
     */
    public LocalGame(int playFieldHeight, int playFieldLength, KiStrength kiStrength) {
        // ki hasn't placed ships yet because they are not known at this point in the game
        super(playFieldHeight, playFieldLength);
        this.ki = new Ki(this.field, kiStrength);
    }

    /**
     * Fügt dem Gegnerfeld, anghand der auf dem eigenen Feld gesetzten Schiffe, zufällig seine hinzu
     */
    public boolean startGame() {
        this.enemyField.addShipRandom(this.field.getShipLengths());
        return true;
    }

    /**
     * Gleich {@link LocalGame#startGame()}, lässt allerdings eine Ki gewählter Stärke deine Züge machen
     * @param kiStrength Gewählte Kistärke
     */
    public boolean startGame(KiStrength kiStrength) {
        this.enemyField = new Field(this.field.getHeight(), this.field.getLength());
        this.ki = new Ki(this.field, kiStrength);
        this.enemyField.addShipRandom(this.field.getShipLengths());
        return true;
    }

    /**
     * Shoot Methode für Lokale Spiele
     * @param position auf die zu schießende {@link Position}
     */
    public int shoot(Position position) {
        int rc;
        if (myTurn) {
            rc = this.enemyField.registerShot(position);
            if (rc == 0)
                myTurn = false;
        }
        else {
            rc = this.ki.shoot();
            if (rc == 0)
                myTurn = true;
        }
        return rc;
    }

    public static LocalGame loadGame(String filePath) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
        return ((LocalGame) in.readObject());
    }
}
