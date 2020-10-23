package game;

import enums.KiStrength;
import enums.MultiplayerMode;
import ki.Ki;
import network.BattleshipProtocol;
import network.Client;
import network.Server;

import java.io.*;

public class Game implements Serializable {
    private Field field;
    private Field enemyField;
    private Client client;
    private Server server;
    private Ki ki;
    private int[] shipLengths;

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public void setShipLengths(int[] shipLengths) {
        this.shipLengths = shipLengths;
    }

    public Game(int playFieldHeight, int playFieldLength){
        this.field = new Field(playFieldHeight, playFieldLength);
        this.enemyField = new Field(playFieldHeight, playFieldLength);
    }

    public Game(int playFieldHeight, int playFieldLength, int[] shipLengths){
        this(playFieldHeight, playFieldLength);
        this.shipLengths = shipLengths;
    }

    public Game(int playFieldHeight, int playFieldLength, int[] shipLengths, KiStrength kiStrength){
        this(playFieldHeight, playFieldLength, shipLengths);
        this.ki = new Ki(this.field, this.enemyField, this.shipLengths, kiStrength);
    }

    public Game(Object... args){
        assert args[0].getClass().isEnum() : "First Parameter has to be enum";
        switch (((MultiplayerMode) args[0])) {
            case CLIENT -> {
                assert args[1].getClass() == String.class: "Second parameter is hostname (String)";
                assert args[2].getClass() == Integer.class: "Thrid parameter is portnumber (Int)";

                this.client = new Client();
                this.client.setHostname((String) args[1]);
                this.client.setPortnumber((Integer) args[2]);
            }
            case SERVER -> {
                this.server = new Server();
                this.server.setPortNumber((Integer) args[4]);
                this.field = new Field((Integer) args[1], (Integer) args[2]);
                this.enemyField = new Field((Integer) args[1], (Integer) args[2]);
                this.shipLengths = (int[]) args[3];

                this.server.waitForConnection();
                this.server.writeLine(BattleshipProtocol.formatSize(this.field.getLength(), this.field.getHeight()));
                this.server.writeLine(BattleshipProtocol.formatShips(this.shipLengths));
            }
        }
    }

    public void loadGame(int id){
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(id + ".txt"));
            Game temp = (Game)in.readObject();
            this.field = temp.field;
            this.enemyField = temp.enemyField;
            this.ki = temp.ki;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveGame(int id){
        try {
            FileOutputStream fout = new FileOutputStream(id + ".txt");
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(this);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
