package game;

import network.BattleshipProtocol;
import network.Server;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class OnlineHostGame extends Game{
    private Server server;
    private int[] shipLengths;

    public OnlineHostGame(int playFieldHeight, int playFieldLength, int portNumber, int[] shipLengths) {
        super(playFieldHeight, playFieldLength);
        this.server = new Server();
        this.server.setPortNumber(portNumber);
        this.shipLengths = shipLengths;
    }

    public boolean waitForConnection() {
        // wait for connection, when connection is established exchange game Configuration
        if (!this.server.waitForConnection())
            return false;

        this.server.writeLine(BattleshipProtocol.formatSize(this.getField().getLength(), this.getField().getHeight()));
        if (!this.server.readLine().equals("done"))
            return false;

        this.server.writeLine(BattleshipProtocol.formatShips(this.shipLengths));
        if (!this.server.readLine().equals("done"))
            return false;

        return true;
    }

    public boolean startGame() throws InterruptedException {
        this.server.writeLine("ready");
        while (!this.server.readLine().equals("ready")) {
            TimeUnit.SECONDS.sleep(1);
        }
        return true;
    }

    @Override
    public void loadGame(String id) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(id + ".txt"));
        OnlineHostGame temp = (OnlineHostGame) in.readObject();
        this.field = temp.field;
        this.enemyField = temp.enemyField;
        this.server = temp.server;
    }
}
