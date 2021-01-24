package game;

import JavaFx.Controller_LoadingScreen;
import JavaFx.GameOptions;
import enums.KiStrength;
import enums.ProtComs;
import game.cells.Shot;
import ki.Ki;
import network.BattleshipProtocol;
import network.Client;

import java.io.*;

/**
 * Implementation von {@link OnlineGame} für ein Spiel, bei dem man selber als Client angefangen hat
 */
public class OnlineClientGame extends OnlineGame {
    private Client client;
    private int[] shipLengths;
    private KiStrength tmpkiStrengh=null; //Da das Feld erst nach dem Verbindungsaufbau verfügbar ist, muss die KiStrengh zwischengespeichert werden
    private Ki ki=null;
    public void setShipLengths(int[] shipLengths) {
        this.shipLengths = shipLengths;
    }

    /**
     * Konstruktor für das OnlineClientGame mit den nötigen Werten, um eine Verbindung zum Server initialisieren zu können
     * @param hostName Name des Hosts zu dem {@link Client} eine Verbindungs aufbauen soll
     * @param portNumber Nummer des Ports auf dem die Verbindung laufen soll
     */
    public OnlineClientGame(String hostName, int portNumber) {
        kiPlays=false;
        this.client = new Client();
        this.client.setHostname(hostName);
        this.client.setPortnumber(portNumber);
        this.myTurn = false;
    }
    public int getShipCount(){
        return shipLengths.length;
    }
    public OnlineClientGame(String hostName, int portNumber, KiStrength ki) {
        kiPlays=true;
        this.client = new Client();
        this.client.setHostname(hostName);
        this.client.setPortnumber(portNumber);
        this.myTurn = false;
        this.tmpkiStrengh=ki;
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
            Controller_LoadingScreen.wasSave=true;
            this.field = temp.field;
            if(kiPlays)this.ki=new Ki(this.enemyField,tmpkiStrengh,true);
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
        this.client.writeLine("next");

        Object[] ships = BattleshipProtocol.processInput(this.client.readLine());
        if (ships[0] != ProtComs.SHIPS) {
            this.client.closeConnection();
            return false;
        }
        this.shipLengths = (int[]) ships[1];
        if(kiPlays)Ki.makeShiplist4Ki(shipLengths);// für die strongKI
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

    /**
     * Shoot Methode für Onlinespiele als Client, kommuniziert über das Netzwerkprotokoll mit dem Gegner
     * @param position auf die zu schießende {@link Position}
     */
    public int shoot(Position position) {
        if(kiPlays && ki==null) this.ki=new Ki(enemyField,tmpkiStrengh,true);
        if(kiPlays){
            ki.shoot();
            position=enemyField.lastShotPos();
            //
        }
        //shoot
        this.client.writeLine(BattleshipProtocol.formatShot(position.getX()+1, position.getY()+1));
        Object[] answer = BattleshipProtocol.processInput(this.client.readLine());

        if (answer[0] != ProtComs.ANSWER) {
            //failure -> stop connection
            if(enemyField.lastShotPos()!=null)enemyField.undoLastShot();
            this.client.closeConnection();
        } else {
            int code = (int)answer[1];
            if(ki!=null) ki.giveAnswer(code);
            if (code == 0) {
                this.enemyField.getPlayfield()[position.getY()][position.getX()] = new Shot();
                this.myTurn = false;
                this.client.writeLine("next");
            } else {
                if(kiPlays && code==2) ki.updateField3(position);
                this.enemyField.getPlayfield()[position.getY()][position.getX()] = new Shot(true);
            }
            return code;
        }
        return -1;
    }

    /**
     * Verarbeitet Gegnerische Schüsse im Netzwerk
     */
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
        generateID();
        this.client.writeLine(BattleshipProtocol.formatSave(String.valueOf(ID)));
        super.saveGame(id);
    }

    public void saveGame(File file) throws IOException {
        generateID();
        this.client.writeLine(BattleshipProtocol.formatSave(String.valueOf(ID)));
        //this.client.writeLine(BattleshipProtocol.formatSave(file.getName()));
        super.saveGame(file.getAbsolutePath());
    }

    /**
     * Speichert falls man als Client das speichern initiiert den save als Hostgame ab um das Spiel später laden zu können.
     * Generiert über {@link #generateID()} eine zufällige long ID die dem Gegenspieler zum laden Mittgeteilt wird
     * @param file Speicherort der Datei
     */
    public void saveGameAsHostGame(File file) throws IOException {
        generateID();
        this.client.writeLine(BattleshipProtocol.formatSave(String.valueOf(ID)));
        OnlineHostGame game = OnlineClientGame.castToHost(this,ID);
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

    /**
     * Methode für {@link #saveGameAsHostGame(File)}, macht aus einem Clientgame ein Hostgame
     * @param clientGame clientGame das in ein Hostgame umgewandelt werden soll
     * @param ID Lange Ganzzahl die der identifizierung eines Saves dient
     */
    public static OnlineHostGame castToHost(OnlineClientGame clientGame,long ID) {
        OnlineHostGame hostGame = new OnlineHostGame(5, 5, clientGame.client.getPortnumber(), null, null);
        hostGame.field = clientGame.field;
        hostGame.enemyField = clientGame.enemyField;
        hostGame.myTurn = clientGame.myTurn;
        hostGame.gameOptions = clientGame.gameOptions;
        hostGame.ID=ID;
        return hostGame;
    }
}
