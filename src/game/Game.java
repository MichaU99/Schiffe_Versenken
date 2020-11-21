package game;

import game.cells.Ship;

import java.io.*;

public abstract class Game implements Serializable {
    protected Field field;
    protected Field enemyField;

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Field getEnemyField() { return enemyField; }

    public Game() {

    }

    public Game(int playFieldHeight, int playFieldLength){
        this.field = new Field(playFieldHeight, playFieldLength);
        this.enemyField = new Field(playFieldHeight, playFieldLength);
    }

    public boolean addShip(Position[] positions) {
        return this.field.addShip(new Ship(positions));
    }

    public boolean addShipRandom(int[] shipLengths) {
        return this.field.addShipRandom(shipLengths);
    }

    // public abstract void shoot();

    public abstract void loadGame(String id) throws IOException, ClassNotFoundException;

    public void saveGame(String id) throws IOException {
        FileOutputStream fout = new FileOutputStream(id + ".txt");
        ObjectOutputStream out = new ObjectOutputStream(fout);
        out.writeObject(this);
        out.flush();
        out.close();
    }

}
