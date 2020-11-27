package game;

import java.io.*;

public abstract class AbstractGame implements Serializable, Game {
    protected FieldImpl field;
    protected FieldImpl enemyField;

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public Field getEnemyField() { return enemyField; }

    public AbstractGame() {

    }

    public AbstractGame(int playFieldHeight, int playFieldLength){
        this.field = new FieldImpl(playFieldHeight, playFieldLength);
        this.enemyField = new FieldImpl(playFieldHeight, playFieldLength);
    }

    @Override
    public void saveGame(String id) throws IOException {
        FileOutputStream fout = new FileOutputStream(id + ".txt");
        ObjectOutputStream out = new ObjectOutputStream(fout);
        out.writeObject(this);
        out.flush();
        out.close();
    }

}
