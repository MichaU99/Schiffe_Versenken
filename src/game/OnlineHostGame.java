package game;

import enums.ProtComs;
import game.cells.Shot;
import gui.GameOptions;
import network.BattleshipProtocol;
import network.Server;

import java.io.*;

public class OnlineHostGame extends OnlineGame {
    private Server server;
    private int[] shipLengths; // the user has to specify the amount of each ship he wants to place before the game

    public OnlineHostGame(int playFieldHeight, int playFieldLength, int portNumber, int[] shipLengths, GameOptions gameOptions) {
        super(playFieldHeight, playFieldLength);
        this.server = new Server();
        this.server.setPortNumber(portNumber);
        this.shipLengths = shipLengths;
        this.gameOptions = gameOptions;
    }

    public boolean waitForConnection() {
        // wait for connection, when connection is established exchange game Configuration
        if (!this.server.waitForConnection())
            return false;
//        System.out.println("Connected");

        this.server.writeLine(BattleshipProtocol.formatSize(this.getField().getLength(), this.getField().getHeight()));
//        System.out.println("SIZE CONFIG SENT");
        if (!this.server.readLine().equals("done"))
            return false;
//        System.out.println("size config Ok");

        this.server.writeLine(BattleshipProtocol.formatShips(this.shipLengths));
        if (!this.server.readLine().equals("done"))
            return false;

        return true;
    }

    public boolean waitForConnectionLoadSave(String saveName) {
        if (!this.server.waitForConnection())
            return false;

        this.server.writeLine(BattleshipProtocol.formatLoad(saveName));

        if (!this.server.readLine().equals("done"))
            return false;

        this.server.writeLine("ready");
        return this.server.readLine().equals("ready");
    }

    public boolean startGame() {
        // sende nach Protokoll ready -> spiel kann gestartet werden
        this.server.writeLine("ready");
        return this.server.readLine().equals("ready");
    }

    public int shoot(Position position) {

        //shoot
        this.server.writeLine(BattleshipProtocol.formatShot(position.getX(), position.getY()));
        Object[] answer = BattleshipProtocol.processInput(this.server.readLine());

        if (answer[0] != ProtComs.ANSWER) {
            //failure -> stop connection
            this.server.closeConnection();
        } else {
            int code = (int)answer[1];
            if (code == 0) {
                this.enemyField.getPlayfield()[position.getY()][position.getX()] = new Shot();
                this.myTurn = false;
                this.server.writeLine("next");
            } else {
                this.enemyField.getPlayfield()[position.getY()][position.getX()] = new Shot(true);
            }
            return code;
        }
        return -1;
    }

    public void enemyShot() {

        Object[] answer = BattleshipProtocol.processInput(this.server.readLine());
        if (answer[0] == ProtComs.NEXT) {
            this.myTurn = true;
        }
        else if (answer[0] == ProtComs.SHOT) {
            Position enemyShot = ((Position) answer[1]);
            int rc = this.field.registerShot(enemyShot);
            this.server.writeLine(BattleshipProtocol.formatAnswer(rc));
        }
        else if (answer[0] == ProtComs.SAVE) {
            this.saveGameAsClientGame(answer[1].toString());
        }
    }

    @Override
    public void saveGame(String id) throws IOException {
        this.server.writeLine(BattleshipProtocol.formatSave(id));
        super.saveGame(id);
    }

    public void saveGame(File file) throws IOException {
        this.server.writeLine(BattleshipProtocol.formatSave(file.getName()));
        super.saveGame(file.getAbsolutePath());
    }

    public void saveGameAsClientGame(String filename) {
        OnlineClientGame clientGame = new OnlineClientGame("", 1);
        clientGame.gameOptions = this.gameOptions;
        clientGame.myTurn = this.myTurn;
        clientGame.field = this.field;
        clientGame.enemyField = this.enemyField;

        try {
            FileOutputStream fout = new FileOutputStream("./" + filename + ".csave");
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(clientGame);
            out.flush();
            out.close();
            fout.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static OnlineHostGame loadGame(String filePath) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
        return ((OnlineHostGame) in.readObject());
    }

    @Override
    public void freeSocket() {
        this.server.closeConnection();
    }
}
