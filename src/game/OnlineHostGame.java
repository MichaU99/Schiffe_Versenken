package game;

import JavaFx.GameOptions;
import JavaFx.GuiMain;
import enums.ProtComs;
import game.cells.Shot;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import ki.Ki;
import network.BattleshipProtocol;
import network.Server;

import java.io.*;

public class OnlineHostGame extends OnlineGame {
    private Server server;
    private int[] shipLengths; // the user has to specify the amount of each ship he wants to place before the game
    private Ki ki;

    public OnlineHostGame(int playFieldHeight, int playFieldLength, int portNumber, int[] shipLengths, GameOptions gameOptions) {
        super(playFieldHeight, playFieldLength);
        this.server = new Server();
        this.server.setPortNumber(portNumber);
        this.shipLengths = shipLengths;
        this.gameOptions = gameOptions;
    }
    public int getShipCount(){
        return shipLengths.length;
    }

    /**
     * Wartet auf eine Verbindung für ein normales Spiel
     */
    public boolean waitForConnection() {
        // wait for connection, when connection is established exchange game Configuration
        if (!this.server.waitForConnection())
            return false;
        System.out.println("Connected");

        this.server.writeLine(BattleshipProtocol.formatSize(this.getField().getLength()));
        System.out.println("SIZE CONFIG SENT");
        if (!this.server.readLine().equals("next"))
            return false;
        System.out.println("size config Ok");

        this.server.writeLine(BattleshipProtocol.formatShips(this.shipLengths));
        if (!this.server.readLine().equals("done"))
            return false;

        return true;
    }

    /**
     * Wartet für eine Verbindung um ein Spiel zu laden
     * @param saveName ID der gespeicherten Datei
     */
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

    /**
     * Teilt dem Gegner die Position eigener Schüsse im OnlineHostgame mit
     * @param position auf die zu schießende {@link Position}
     */
    public int shoot(Position position) {
        if(kiPlays){
            if(ki==null){
                Ki.makeShiplist4Ki(shipLengths); // für die strong ki
                ki=new Ki(this.enemyField,this.gameOptions.getKiStrength(),true);
            }
            this.ki.shoot();
            position=this.enemyField.lastShotPos();
            //ki.updateField3(position);
            System.out.println(position);
        }

        //shoot
        this.server.writeLine(BattleshipProtocol.formatShot(position.getX()+1, position.getY()+1));
        Object[] answer = BattleshipProtocol.processInput(this.server.readLine());

        if (answer[0] != ProtComs.ANSWER) {
            //failure -> stop connection
            this.enemyField.undoLastShot();
            this.server.closeConnection();
        } else {
            int code = (int)answer[1];
            if(ki!=null) ki.giveAnswer(code);
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

    /**
     * Verarbeitet Gegnerische Schüsse im OnlineHostgame
     */
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
            Platform.runLater(()->{
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Ihr Spielpartner hat das Speichern eingeleitet.\nIhr Spiel wird nun gespeichert und dann geschlossen\nFalls Sie weiterspielen wollen muss Ihr Partner das Spiel\nladen und Sie per NewGame->Multiplayer->JoinGame beitreten\nDer Spielstand wird automatisch gewählt");
                alert.showAndWait();
                freeSocket();
                GuiMain.stage.close();
            });
        }
    }

    @Override
    public void saveGame(String id) throws IOException {
        generateID();
        this.server.writeLine(BattleshipProtocol.formatSave(String.valueOf(ID)));
        //this.server.writeLine(BattleshipProtocol.formatSave(id));
        super.saveGame(id);
        freeSocket();
        GuiMain.stage.close();
    }

    public void saveGame(File file) throws IOException {
        generateID();
        this.server.writeLine(BattleshipProtocol.formatSave(String.valueOf(ID)));
        //this.server.writeLine(BattleshipProtocol.formatSave(file.getName()));
        super.saveGame(file.getAbsolutePath());
        freeSocket();
        GuiMain.stage.close();
    }

    /**
     * Dient dazu eine Speicheranfrage des Gegners als Host zu verarbeiten, wandelt ein Hostgame in ein Clientgame um, um es später laden zu können
     * @param filename Speicherort
     */
    public void saveGameAsClientGame(String filename) {
        OnlineClientGame clientGame = new OnlineClientGame(filename, server.getPortNumber());
        clientGame.gameOptions = this.gameOptions;
        clientGame.myTurn = this.myTurn;
        clientGame.field = this.field;
        clientGame.enemyField = this.enemyField;
        clientGame.kiPlays=this.kiPlays;
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
