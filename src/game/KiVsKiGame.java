package game;

import enums.KiStrength;
import ki.Ki;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class KiVsKiGame extends Game{
    private Ki ki1;
    private Ki ki2;

    /**
     * Konstruktor des KivsKiGames
     */
    public KiVsKiGame(int playFieldHeight, int playFieldWidth, KiStrength kiStrength1, KiStrength kiStrength2) {
        super(playFieldHeight, playFieldWidth);
        this.ki1 = new Ki(this.enemyField, kiStrength1);
        this.ki2 = new Ki(this.field, kiStrength2);
    }

    /**
     * Fügt beiden Felden ihre Schiffe zufällig hinzu, besitzt feste Schiffsanzahl und Länge
     * @return
     */
    @Override
    public boolean startGame() {
        this.field.addShipRandom(new Integer[]{5, 4, 4, 3, 3, 3, 2, 2, 2, 2});
        this.enemyField.addShipRandom(new Integer[]{5, 4, 4, 3, 3, 3, 2, 2, 2, 2});
        return true;
    }

    /**
     * Fügt beiden Felden ihre Schiffe zufällig hinzu, Schiffe werden per Liste übergeben
     * @param shipLengths Liste der Form 2 3 4 ,für Schiffe der Längen 2,3 und 4
     * @return
     */
    public boolean startGame(ArrayList<Integer> shipLengths) {
        this.field.addShipRandom(shipLengths);
        this.enemyField.addShipRandom(shipLengths);
        return true;
    }

    /**
     * Shoot Methode der KivsKiGame Klasse
     * @param position auf die zu schießende {@link Position}
     * @return
     */
    @Override
    public int shoot(Position position) {
        if (this.myTurn) {
            int rc = ki1.shoot();
            if (rc == 0)
                this.myTurn = false;
        }
        else {
            int rc = ki2.shoot();
            if (rc == 0)
                this.myTurn = true;
        }
        return 0;
    }

    public static KiVsKiGame loadGame(String filePath) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
        return ((KiVsKiGame) in.readObject());
    }
}
