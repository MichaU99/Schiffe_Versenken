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

    public void startGame() {
        this.ki.addShipsToField(this.field.getShipLengths());
    }

    public void shoot(Position position) {
        if (this.enemyField.registerShot(position) > 0)
            return;

        while (true) {
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
