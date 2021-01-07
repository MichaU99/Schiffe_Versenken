package game;

import JavaFx.GameOptions;
import enums.KiStrength;
import enums.ProtComs;
import game.cells.Shot;
import guiLogic.WaitingForConnectionForm;
import network.BattleshipProtocol;
import network.Client;

import java.io.*;

/**
 * Implementation von {@link OnlineGame} für ein Spiel, bei dem man selber als Client angefangen hat
 */
public class OnlineClientGame extends OnlineGame {
    private Client client;
    private int[] shipLengths;

    public void setShipLengths(int[] shipLengths) {
        this.shipLengths = shipLengths;
    }

    /**
     * Konstruktor für das OnlineClientGame mit den nötigen Werten, um eine Verbindung zum Server initialisieren zu können
     * @param hostName Name des Hosts zu dem {@link Client} eine Verbindungs aufbauen soll
     * @param portNumber Nummer des Ports auf dem die Verbindung laufen soll
     */
    public OnlineClientGame(String hostName, int portNumber) {
        this.client = new Client();
        this.client.setHostname(hostName);
        this.client.setPortnumber(portNumber);
        this.myTurn = false;
    }

    /**
     * Aufbauen der Verbindung zum
     * @return
     */
    public boolean establishConnection() {
        //open Connection and get game configuration
        // Verbindung wird versucht aufzubauen. Wenn die Methode zu ende ist, sind auch die Schifflängen verfügbar
        // Dann kann der User erst seine Schiffe plazieren.
        try {
            if (!this.client.openConnection())
                return false;
        } catch (IOException e) {
            return false;
        }

        Object[] size = BattleshipProtocol.processInput(this.client.readLine());
        if (size[0] == ProtComs.LOAD) {
            OnlineClientGame temp;
            try {
                temp = OnlineClientGame.loadGame(size[1].toString() + ".csave");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            WaitingForConnectionForm.wasSave = true;
            this.field = temp.field;
            this.enemyField = temp.enemyField;
            //this.client = temp.client;
            this.myTurn = temp.myTurn;
            //this.shipLengths = temp.shipLengths;
            this.gameOptions = temp.gameOptions;

            this.client.writeLine("done");
            if (!this.client.readLine().equals("ready"))
                return false;
            this.client.writeLine("ready");
            return true;
        }

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

        int five = 0, four = 0, three = 0, two = 0;
        for (int i: shipLengths) {
            if (i == 5)
                five++;
            else if (i == 4)
                four++;
            else if (i == 3)
                three++;
            else if (i == 2)
                two++;
        }
        gameOptions = new GameOptions(this.field.getHeight(), KiStrength.INTERMEDIATE, five, four, three, two);

        this.client.writeLine("done");

        return true;
    }

    public boolean startGame() {
        if (!this.client.readLine().equals("ready"))
            return false;

        this.client.writeLine("ready");
        return true;
    }

    public int shoot(Position position) {
        //shoot
        this.client.writeLine(BattleshipProtocol.formatShot(position.getX(), position.getY()));
        Object[] answer = BattleshipProtocol.processInput(this.client.readLine());

        if (answer[0] != ProtComs.ANSWER) {
            //failure -> stop connection
            this.client.closeConnection();
        } else {
            int code = (int)answer[1];
            if (code == 0) {
                this.enemyField.getPlayfield()[position.getY()][position.getX()] = new Shot();
                this.myTurn = false;
                this.client.writeLine("next");
            } else {
                this.enemyField.getPlayfield()[position.getY()][position.getX()] = new Shot(true);
            }
            return code;
        }
        return -1;
    }

    public void enemyShot() {

        Object[] answer = BattleshipProtocol.processInput(this.client.readLine());
        if (answer[0] == ProtComs.NEXT) {
            this.myTurn = true;
        }
        else if (answer[0] == ProtComs.SHOT) {
            Position enemyShot = ((Position) answer[1]);
            int rc = this.field.registerShot(enemyShot);
            this.client.writeLine(BattleshipProtocol.formatAnswer(rc));
        }
        else if (answer[0] == ProtComs.SAVE) {
            try {
                super.saveGame("./" + answer[1].toString() + ".csave");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveGame(String id) throws IOException {
        this.client.writeLine(BattleshipProtocol.formatSave(id));
        super.saveGame(id);
    }

    public void saveGame(File file) throws IOException {
        this.client.writeLine(BattleshipProtocol.formatSave(file.getName()));
        super.saveGame(file.getAbsolutePath());
    }

    public void saveGameAsHostGame(File file) throws IOException {
        this.client.writeLine(BattleshipProtocol.formatSave(file.getName()));
        OnlineHostGame game = OnlineClientGame.castToHost(this);
        FileOutputStream fout = new FileOutputStream(file.getAbsolutePath());
        ObjectOutputStream out = new ObjectOutputStream(fout);
        out.writeObject(game);
        out.flush();
        out.close();
        fout.close();
    }

    public static OnlineClientGame loadGame(String filePath) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
        return ((OnlineClientGame) in.readObject());
    }

    @Override
    public void freeSocket() {
        this.client.closeConnection();
    }

    public static OnlineHostGame castToHost(OnlineClientGame clientGame) {
        OnlineHostGame hostGame = new OnlineHostGame(5, 5, clientGame.client.getPortnumber(), null, null);
        hostGame.field = clientGame.field;
        hostGame.enemyField = clientGame.enemyField;
        hostGame.myTurn = clientGame.myTurn;
        hostGame.gameOptions = clientGame.gameOptions;
        return hostGame;
    }
}
