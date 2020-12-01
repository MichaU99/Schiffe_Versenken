package game;

import enums.KiStrength;
import ki.Ki;

import java.io.*;

public class LocalGame extends Game {
    private Ki ki;

    public LocalGame(int playFieldHeight, int playFieldLength, KiStrength kiStrength) {
        // ki hasn't placed ships yet because they are not known at this point in the game
        super(playFieldHeight, playFieldLength);
        this.ki = new Ki(this.field, this.enemyField, kiStrength);
    }

    public boolean startGame() {
        this.ki.addShipsToField(this.field.getShipLengths());
        //this.enemyField.addShipRandom(this.field.getShipLengths());
        return true;
    }

    public void shoot(Position position) {
        // Spieler kann schießen bis er nicht mehr trifft.
        // Wenn er verfehlt, kann die KI schießen bis sie nicht trifft

        if (this.getField().getShipCount() == 0)
            return;

        if (this.enemyField.registerShot(position) > 0)
            return;

        while (true) {
            if (this.getField().getShipCount() == 0)
                break;
            if (this.ki.shoot() == 0)
                break;
        }
    }

    public void loadGame(String id) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(id + ".txt"));
        LocalGame temp = (LocalGame) in.readObject();
        this.field = temp.field;
        this.enemyField = temp.enemyField;
        this.ki = temp.ki;
    }
}
