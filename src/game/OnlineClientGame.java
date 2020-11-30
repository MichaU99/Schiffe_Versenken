package game;

import enums.ProtComs;
import game.cells.Shot;
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
        // hier ist beim erstellen des spiels nur der hostname und die portnummer bekannt
        this.client = new Client();
        this.client.setHostname(hostName);
        this.client.setPortnumber(portNumber);
    }

    public boolean establishConnection() {
        //open Connection and get game configuration
        // Verbindung wird versucht aufzubauen. Wenn die Methode zu ende ist, sind auch die Schifflängen verfügbar
        // Dann kann der User erst seine Schiffe plazieren.
        if (!this.client.openConnection())
            return false;

        Object[] size = BattleshipProtocol.processInput(this.client.readLine());
        if (size[0] != ProtComs.SIZE) {
            this.client.closeConnection();
            return false;
        }
        this.field = new Field((int) size[2], (int) size[1]);
        this.enemyField = new Field((int) size[2], (int) size[1]);
        this.client.writeLine("done");

        Object[] ships = BattleshipProtocol.processInput(this.client.readLine());
        if (ships[0] != ProtComs.SHIPS) {
            this.client.closeConnection();
            return false;
        }
        this.shipLengths = (int[]) ships[1];
        this.client.writeLine("done");

        return true;
    }

    public boolean startGame() {
        while (!this.client.readLine().equals("ready")) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.client.writeLine("ready");

        // also wait for first shot of enemy, because after that flow will be same as server
        while (true) {
            String line = this.client.readLine();
            Object[] answer2 = BattleshipProtocol.processInput(line);
            if (line.equals("next")) {
                break;
            }
            if (answer2[0] == ProtComs.SAVE) {
                try {
                    super.saveGame((String) answer2[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            if (answer2[0] != ProtComs.SHOT) {
                break;
            }
            Position enemyShot = (Position) answer2[1];
            this.client.writeLine(BattleshipProtocol.formatAnswer(this.field.registerShot(enemyShot)));
        }
        return true;
    }

    public void shoot(Position position) {
        // gleiche wie beim server
        //TODO Oberklasse für Online games erstellen?
        this.client.writeLine(BattleshipProtocol.formatShot(position.getX(), position.getY()));
        Object[] answer = BattleshipProtocol.processInput(this.client.readLine());
        if (answer[0] != ProtComs.ANSWER){
            return;
        }

        if ((int) answer[1] >= 1) {
            // User can shoot again
            this.enemyField.getPlayfield()[position.getY()][position.getX()] = new Shot(true);
        }
        else {
            // User missed -> enemy can shoot
            this.enemyField.getPlayfield()[position.getY()][position.getX()] = new Shot();
            this.client.writeLine("next");

            while (true) {
                String line = this.client.readLine();
                Object[] answer2 = BattleshipProtocol.processInput(line);
                if (line.equals("next")) {
                    break;
                }
                if (answer2[0] == ProtComs.SAVE) {
                    try {
                        super.saveGame((String) answer2[1]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                if (answer2[0] != ProtComs.SHOT) {
                    break;
                }
                Position enemyShot = (Position) answer2[1];
                this.client.writeLine(BattleshipProtocol.formatAnswer(this.field.registerShot(enemyShot)));
            }
        }
    }

    @Override
    public void saveGame(String id) throws IOException {
        super.saveGame(id);
        this.client.writeLine(BattleshipProtocol.formatSave(id));
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
