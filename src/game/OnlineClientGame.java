package game;

import network.BattleshipProtocol;
import network.Client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.TimeUnit;

public class OnlineClientGame extends Game{
    private Client client;
    private int[] shipLengths;

    public void setShipLengths(int[] shipLengths) {
        this.shipLengths = shipLengths;
    }

    public OnlineClientGame(String hostName, int portNumber) {
        this.client = new Client();
        this.client.setHostname(hostName);
        this.client.setPortnumber(portNumber);
    }

    public boolean establishConnection() {
        //open Connection and get game configuration
        if (!this.client.openConnection())
            return false;

        BattleshipProtocol.processInput(this.client.readLine(), this);
        this.client.writeLine("done");

        BattleshipProtocol.processInput(this.client.readLine(), this);
        this.client.writeLine("done");

        return true;
    }

    public boolean startGame() throws InterruptedException {
        while (!this.client.readLine().equals("ready")) {
            TimeUnit.SECONDS.sleep(1);
        }
        this.client.writeLine("ready");
        return true;
    }

    @Override
    public void loadGame(String id) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(id + ".txt"));
        OnlineClientGame temp = (OnlineClientGame) in.readObject();
        this.field = temp.field;
        this.enemyField = temp.enemyField;
        this.client = temp.client;
    }
}
